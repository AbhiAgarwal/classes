object hw08 extends js.util.JsApp {
  import js.hw08.ast._
  import js.hw08._
  import scala.util.parsing.input.NoPosition
  /*
   * CSCI-UA.0480-003: Homework 8
   * Abhi Agarwal
   * 
   * Partner: Bob Gardner
   * Collaborators: <Any Collaborators>
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   * 
   * Replace the '???' expression with your code in each function.
   *
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   * 
   * Your solution will _not_ be graded if it does not compile!!
   * 
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert.  Simply put in a
   * '???' as needed to get something that compiles without error.
   *
   */

  /* Type Inference */

  // A helper function to check whether a JS type has a function type in it.
  // While this is completely given, this function is worth studying to see
  // how library functions are used.
  def hasFunctionTyp(t: Typ): Boolean = t match {
    case TFunction(_, _) => true
    case _ => false
  }

  def typeInfer(env: Map[String, Typ], e: Expr): Typ = {
    // Some shortcuts for convenience
    def typ(e1: Expr) = typeInfer(env, e1)
    def err[T](tgot: Typ, e1: Expr): T = throw StaticTypeError(tgot, e1)

    e match {
      case Print(e1) =>
        typ(e1); TUndefined
      case Num(_) => TNumber
      case Bool(_) => TBool
      case Undefined => TUndefined
      case Str(_) => TString
      case Var(x) => env(x)
      case ConstDecl(x, e1, e2) =>
        typeInfer(env + (x -> typ(e1)), e2)
      case UnOp(UMinus, e1) => typ(e1) match {
        case TNumber => TNumber
        case tgot => err(tgot, e1)
      }
      case UnOp(Not, e1) => typ(e1) match {
        case TBool => TBool
        case tgot => err(tgot, e1)
      }
      case BinOp(bop, e1, e2) =>
        bop match {
          case Plus => (typ(e1), typ(e2)) match {
            // Normal cases either Number or String
            case (TNumber, TNumber) => TNumber
            case (TString, TString) => TString
            // _ works, but certain check String or Number since this is Plus. Throws correct error.
            case (TString | TNumber, tgot) => err(tgot, e2)
            case (tgot, _) => err(tgot, e1)
          }
          case Minus | Times | Div => (typ(e1), typ(e2)) match {
            // Only number
            case (TNumber, TNumber) => TNumber
            // _ works, but certain check String or Number since this is Minus | Times | Div. Throws correct error.
            case (TNumber, tgot) => err(tgot, e2)
            case (tgot, _) => err(tgot, e1)
          }
          // Uses hasFunctionTyp
          case Eq | Ne =>
            // Check if either of them are functions
            if (hasFunctionTyp(typ(e1))) err(typ(e1), e1)
            else if (hasFunctionTyp(typ(e2))) err(typ(e2), e2)
            // If they are both equal then it's a Boolean else error
            (typ(e1), typ(e2)) match {
              case (tgot1, tgot2) =>
                if (tgot1 == tgot2) TBool
                // Throws correct error.
                else err(tgot2, e2)
            }
          case Lt | Le | Gt | Ge => (typ(e1), typ(e2)) match {
            // Check if it is either Number or String
            case (TNumber, TNumber) => TBool
            case (TString, TString) => TBool
            // _ works, but certain check String or Number since this is Lt | Le | Gt | Ge. Throws correct error.
            case (TString | TNumber, tgot) => err(tgot, e2)
            case (tgot, _) => err(tgot, e1)
          }
          case And | Or => (typ(e1), typ(e2)) match {
            // Check if both of them are Boolean
            case (TBool, TBool) => TBool
            // _ works, but to be certain check Boolean since this is And | Or. Throws correct error.
            case (TBool, tgot) => err(tgot, e2)
            case (tgot, _) => err(tgot, e1)
          }
          // Just returns type of e2.
          case Seq => typ(e2)
        }
      case If(e1, e2, e3) => typ(e1) match {
        case TBool => {
          val e2_t = typ(e2)
          val e3_t = typ(e3)
          if (e2_t == e3_t) e2_t
          else err(e3_t, e3)
        }
        case tgot => err(tgot, e1)
      }
      case Function(p, xs, tann, e1) => {
        // Bind to env1 an environment that extends env with an appropriate binding if
        // the function is potentially recursive.
        val env1 = (p, tann) match {
          case (Some(f), Some(tret)) =>
            val tprime = TFunction(xs map (_._2), tret)
            env + (f -> tprime)
          case (None, _) => env
          case _ => err(TUndefined, e1)
        }
        // Bind to env2 an environment that extends env1 with bindings for xs. Merge using ++
        val env2 = env1 ++ xs
        // Match on whether the return type is specified.
        // case class TFunction(ts: List[Typ], tret: Typ) extends Typ.
        // tret is a Typ.
        tann match {
          case None => TFunction(xs map (_._2), typeInfer(env2, e1))
          case Some(tret) => {
            val newVal = typeInfer(env2, e1)
            if (tret == newVal) TFunction(xs map (_._2), tret)
            else err(newVal, e1)
          }
        }
      }
      case Call(e1, es) => typ(e1) match {
        // TFunction(ts: List[Typ], tret: Typ) extends Typ
        case TFunction(txs, tret) if (txs.length == es.length) => {
          (txs, es).zipped.foreach {
            case (txs1, es1) => {
              val txs2 = typ(es1)
              if (txs2 == txs1) txs1
              else err(txs2, es1)
            }
          }
          tret
        }
        case tgot => err(tgot, e1)
      }
    }
  }

  /* JakartaScript Interpreter */

  def toNum(v: Val): Double = v match {
    case Num(n) => n
    case _ => throw StuckError(v)
  }

  def toBool(v: Val): Boolean = v match {
    case Bool(b) => b
    case _ => throw StuckError(v)
  }

  def toStr(v: Val): String = v match {
    case Str(s) => s
    case _ => throw StuckError(v)
  }

  /*
   * Helper function that implements the semantics of inequality
   * operators Lt, Le, Gt, and Ge on values.
   */
  def inequalityVal(bop: Bop, v1: Val, v2: Val): Boolean = {
    require(bop == Lt || bop == Le || bop == Gt || bop == Ge)
    (v1, v2) match {
      case (Str(s1), Str(s2)) =>
        (bop: @unchecked) match {
          case Lt => s1 < s2
          case Le => s1 <= s2
          case Gt => s1 > s2
          case Ge => s1 >= s2
        }
      case _ =>
        val (n1, n2) = (toNum(v1), toNum(v2))
        (bop: @unchecked) match {
          case Lt => n1 < n2
          case Le => n1 <= n2
          case Gt => n1 > n2
          case Ge => n1 >= n2
        }
    }
  }

  /* 
   * Substitutions e[v/x]
   */
  def subst(e: Expr, x: String, v: Val): Expr = {
    require(closed(v))
    /* Simple helper that calls substitute on an expression
     * with the input value v and variable name x. */
    def substX(e: Expr): Expr = subst(e, x, v)
    /* Body */
    e match {
      case Num(_) | Bool(_) | Undefined | Str(_) => e
      case Var(y) => if (x == y) v else e
      case Print(e1) => Print(substX(e1))
      case UnOp(uop, e1) => UnOp(uop, substX(e1))
      case BinOp(bop, e1, e2) => BinOp(bop, substX(e1), substX(e2))
      case If(b, e1, e2) => If(substX(b), substX(e1), substX(e2))
      case Call(e0, es) => Call(substX(e0), es.map(substX))
      case ConstDecl(y, ed, eb) =>
        ConstDecl(y, substX(ed), if (x == y) eb else substX(eb))
      // Change was to add tann, but the code was the same.
      case Function(p, ys, tann, eb) => {
        if (ys.exists { _ == x } || Some(x) == p) Function(p, ys, tann, eb)
        else Function(p, ys, tann, substX(eb))
      }
    }
  }

  /*
   * This code is a reference implementation of JakartaScript without
   * functions and big-step static binding semantics.
   */
  def eval(e: Expr): Val = {
    require(closed(e), "eval called on non-closed expression:\n" + e.prettyJS)
    /* Some helper functions for convenience. */
    def eToNum(e: Expr): Double = toNum(eval(e))
    def eToBool(e: Expr): Boolean = toBool(eval(e))
    e match {
      /* Base Cases */
      case v: Val => v

      /* Inductive Cases */
      case Print(e) =>
        println(eval(e).prettyVal); Undefined

      case UnOp(UMinus, e1) => Num(-eToNum(e1))

      case UnOp(Not, e1) => Bool(!eToBool(e1))

      case BinOp(Plus, e1, e2) => (eval(e1), eval(e2)) match {
        case (Str(s1), v2) => Str(s1 + toStr(v2))
        case (v1, Str(s2)) => Str(toStr(v1) + s2)
        case (v1, v2) => Num(toNum(v1) + toNum(v2))
      }

      case BinOp(Minus, e1, e2) => Num(eToNum(e1) - eToNum(e2))

      case BinOp(Times, e1, e2) => Num(eToNum(e1) * eToNum(e2))

      case BinOp(Div, e1, e2) => Num(eToNum(e1) / eToNum(e2))

      case BinOp(And, e1, e2) =>
        val v1 = eval(e1)
        if (toBool(v1)) eval(e2) else v1

      case BinOp(Or, e1, e2) =>
        val v1 = eval(e1)
        if (toBool(v1)) v1 else eval(e2)

      case BinOp(Seq, e1, e2) =>
        eval(e1); eval(e2)

      case BinOp(bop, e1, e2) =>
        bop match {
          case Eq | Ne =>
            val v1 = eval(e1)
            val v2 = eval(e2)
            (bop: @unchecked) match {
              case Eq => Bool(v1 == v2)
              case Ne => Bool(v1 != v2)
            }
          case _ => Bool(inequalityVal(bop, eval(e1), eval(e2)))
        }

      case If(e1, e2, e3) => if (eToBool(e1)) eval(e2) else eval(e3)

      case ConstDecl(x, ed, eb) => eval(subst(eb, x, eval(ed)))

      case Call(e0, es) =>
        val v0 = eval(e0)
        v0 match {
          case Function(p, xs, _, eb) =>
            val ebp = p match {
              case None => eb
              case Some(x0) => subst(eb, x0, v0)
            }
            val vs = es.map(eval).padTo(xs.size, Undefined)
            // compute common substitutions for rules EvalCall and EvalCallRec
            val ebpp = (xs, vs).zipped.foldRight(ebp) {
              case (((xi, _), vi), ebpp) => subst(ebpp, xi, vi)
            }
            eval(ebpp)
          case _ => throw StuckError(e)
        }

      case Var(_) => throw StuckError(e) // this should never happen
    }
  }

  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def evaluate(s: String): Val = eval(parse.fromString(s))

  /* Interface to run your interpreter from the command line.  You can ignore the code below. */

  def processFile(file: java.io.File) {
    if (debug) {
      println("============================================================")
      println("File: " + file.getName)
      println("Parsing ...")
    }

    val expr = handle(fail()) {
      parse.fromFile(file)
    }

    if (debug) {
      println("Parsed expression:")
      println(expr)
    }

    handle() {
      val v = eval(expr)
      println(v.prettyVal)
    }
  }
}
