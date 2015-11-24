object hw09 extends js.util.JsApp {
  import js.hw09.ast._
  import js.hw09._
  import scala.util.parsing.input.NoPosition
  /*
   * CSCI-UA.0480-003: Homework 9
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

  def mut(m: PMode): Mut = m match {
    case PName | PConst => MConst
    case PVar | PRef => MVar
  }

  def typeInfer(env: Map[String, (Mut, Typ)], e: Expr): Typ = {
    // Some shortcuts for convenience
    def typ(e1: Expr) = typeInfer(env, e1)
    def err[T](tgot: Typ, e1: Expr): T = throw StaticTypeError(tgot, e1)
    def locerr[T](e1: Expr): T = throw LocTypeError(e1)
    def checkTyp(texp: Typ, e1: Expr): Typ = {
      val tgot = typ(e1)
      if (texp == tgot) texp else err(tgot, e1)
    }

    e match {
      case Print(e1) =>
        typ(e1); TUndefined
      case Num(_) => TNumber
      case Bool(_) => TBool
      case Undefined => TUndefined
      case Str(_) => TString
      case Var(x) => env(x)._2
      case Decl(mut, x, e1, e2) =>
        typeInfer(env + (x -> (mut, typ(e1))), e2)
      case UnOp(UMinus, e1) => typ(e1) match {
        case TNumber => TNumber
        case tgot => err(tgot, e1)
      }
      case UnOp(Not, e1) =>
        checkTyp(TBool, e1)
      case BinOp(bop, e1, e2) =>
        bop match {
          case Plus =>
            typ(e1) match {
              case TNumber => checkTyp(TNumber, e2)
              case TString => checkTyp(TString, e2)
              case tgot => err(tgot, e1)
            }
          case Minus | Times | Div =>
            checkTyp(TNumber, e1)
            checkTyp(TNumber, e2)
          case Eq | Ne => typ(e1) match {
            case t1 if !hasFunctionTyp(t1) =>
              checkTyp(t1, e2); TBool
            case tgot => err(tgot, e1)
          }
          case Lt | Le | Gt | Ge =>
            typ(e1) match {
              case TNumber => checkTyp(TNumber, e2)
              case TString => checkTyp(TString, e2)
              case tgot => err(tgot, e1)
            }
            TBool
          case And | Or =>
            checkTyp(TBool, e1)
            checkTyp(TBool, e2)
          case Seq =>
            typ(e1); typ(e2)
          case Assign =>
            ???
        }
      case If(e1, e2, e3) =>
        checkTyp(TBool, e1)
        val t2 = typ(e2)
        checkTyp(t2, e3)
      case Function(p, xs, tann, e1) => {
        // Bind to env1 an environment that extends env with an appropriate binding if
        // the function is potentially recursive.
        val env1 = (p, tann) match {
          case (Some(f), Some(tret)) =>
            val tp = TFunction(xs map (_._2), tret)
            env + (f -> (MConst, tp))
          case (None, _) => env
          case _ => err(TUndefined, e1)
        }
        // Bind to env2 an environment that extends env1 with bindings for xs.
        val bindxs = xs.map {
          // Mut -> MConst, MVar
          // MConst
          case (pmode, ((PConst | PName), typ)) => pmode -> (MConst, typ)
          // MVar
          case (pmode, (_, typ)) => pmode -> (MVar, typ)
        }
        val env2 = env1 ++ bindxs
        // Infer the type of the body e1
        val t1 = typeInfer(env2, e1)
        // Check the return type if it is specified.
        tann foreach (tret => if (t1 != tret) err(t1, e1))
        // Return the inferred type
        TFunction(xs map (_._2), t1)
      }
      case Call(e1, es) => typ(e1) match {
        case TFunction(txs, tret) if (txs.length == es.length) => {
          (txs, es).zipped.foreach {
            // Use PRef
            (p, es1) =>
              p match {
                case (pmode, t) => {
                  if (pmode != PRef) {
                    val es1Type = typ(es1)
                    if (es1Type == t) es1Type
                    else err(es1Type, es1)
                  } else {
                    err(t, es1)
                  }
                }
              }
          }
          tret
        }
        case tgot => err(tgot, e1)
      }
      case Addr(_) | UnOp(Deref, _) =>
        throw new IllegalArgumentException("Gremlins: Encountered unexpected expression %s.".format(e))
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
   * Substitutions e[er/x]
   */
  def subst(e: Expr, x: String, er: Expr): Expr = {
    require(closed(er))
    /* Simple helper that calls substitute on an expression
     * with the input value v and variable name x. */
    def substX(e: Expr): Expr = subst(e, x, er)
    /* Body */
    e match {
      case Num(_) | Bool(_) | Undefined | Str(_) | Addr(_) => e
      case Var(y) => if (x == y) er else e
      case Print(e1) => Print(substX(e1))
      case UnOp(uop, e1) => UnOp(uop, substX(e1))
      case BinOp(bop, e1, e2) => BinOp(bop, substX(e1), substX(e2))
      case If(b, e1, e2) => If(substX(b), substX(e1), substX(e2))
      case Call(e0, es) =>
        Call(substX(e0), es map substX)
      case Decl(mut, y, ed, eb) =>
        Decl(mut, y, substX(ed), if (x == y) eb else substX(eb))
      case Function(p, ys, tann, eb) =>
        if (p == Some(x) || (ys exists (_._1 == x))) e
        else Function(p, ys, tann, substX(eb))
    }
  }

  /*
   * Interpreter for JakartaScript with mutable variables and parameter passing modes
   */
  def eval(m: Mem, e: Expr): (Mem, Val) = {
    require(closed(e), "eval called on non-closed expression:\n" + e.prettyJS)
    /* Some helper functions for convenience. */
    def eToVal(e: Expr): (Mem, Val) = eval(m, e)
    def eToNum(m: Mem, e: Expr): (Mem, Double) = {
      val (mp, v) = eval(m, e)
      (mp, toNum(v))
    }
    def eToBool(m: Mem, e: Expr): (Mem, Boolean) = {
      val (mp, v) = eval(m, e)
      (mp, toBool(v))
    }
    e match {
      case v: Val => (m, v)

      case Print(e) =>
        val (mp, v) = eToVal(e)
        println(v.prettyVal);
        (mp, Undefined)

      case UnOp(UMinus, e1) =>
        val (mp, n) = eToNum(m, e1)
        (mp, Num(-n))

      case UnOp(Not, e1) =>
        val (mp, n) = eToBool(m, e1)
        (mp, Bool(!n))

      case UnOp(Deref, a: Addr) => {
        ???
      }

      case BinOp(Plus, e1, e2) => {
        val (mp1, n1) = eToVal(e1)
        val (mp2, n2) = eToVal(e2)
        (e1, e2) match {
          case (Str(x), Str(y)) => (mp1, Str(x + y))
          case (Num(x), Num(y)) => (mp1, Num(x + y))
        }
      }

      case BinOp(bop @ (Minus | Times | Div), e1, e2) => {
        val (mp1, n1) = eToNum(m, e1)
        val (mp2, n2) = eToNum(m, e2)
        bop match {
          case Minus => (mp1, Num(n1 - n2))
          case Times => (mp1, Num(n1 * n2))
          case Div => (mp1, Num(n1 / n2))
        }
      }

      case BinOp(And, e1, e2) => {
        val (mp1, n1) = eToBool(m, e1)
        if (n1) eToVal(e2)
        else (mp1, Bool(false))
      }

      case BinOp(Or, e1, e2) => {
        val (mp1, n1) = eToBool(m, e1)
        if (n1) (mp1, Bool(true))
        else eToVal(e2)
      }

      case BinOp(Seq, e1, e2) => {
        val (mp1, n1) = eToVal(e1)
        eToVal(e2)
      }

      case BinOp(Assign, UnOp(Deref, a: Addr), e2) =>
        ???

      case BinOp(bop @ (Eq | Ne | Lt | Gt | Le | Ge), e1, e2) => {
        val (mp1, n1) = eToVal(e1)
        val (mp2, n2) = eToVal(e2)
        bop match {
          case (Eq) =>(mp1, Bool(n1 == n2))
          case (Ne) => (mp1, Bool(n1 != n2))
          case (Lt | Gt | Le | Ge) => (mp1, Bool(inequalityVal(bop, n1, n2)))
        }
      }

      case If(e1, e2, e3) =>
        val (mp1, n1) = eToBool(m, e1)
        if (n1) eToVal(e2)
        else eToVal(e3)

      case Decl(MConst, x, ed, eb) =>
        val (mp, vd) = eval(m, ed)
        eval(mp, subst(eb, x, vd))

      case Decl(MVar, x, ed, eb) => {
        ???
      }

      case Call(e0, es) =>
        val (mp, v0) = eval(m, e0)
        v0 match {
          /** EvalCallRec */
          case v0 @ Function(Some(x), _, _, _) =>
            val v0p = subst(v0.copy(p = None), x, v0)
            eval(mp, Call(v0p, es))
          /** EvalCallConst, EvalCallName, EvalCallRef, EvalCallVar */
          case v0 @ Function(None, (x, (mode, _)) :: xs, tann, eb) =>
            (mode, es) match {
              /** EvalCallConst */
              case (PConst, e :: es) => {
                ???
              }
              /** EvalCallName, EvalCallRef */
              case (PName | PRef, e :: es) =>
                ???
              /** EvalCallVar */
              case (PVar, e :: es) =>
                ???
              case _ => throw StuckError(e)
            }
          /** EvalCall */
          case Function(None, Nil, _, eb) =>
            eval(mp, eb)
          case _ => throw StuckError(e)
        }

      case Var(_) | UnOp(Deref, _) | BinOp(_, _, _) =>
        throw StuckError(e) // this should never happen
    }
  }

  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def evaluate(e: Expr): Val = eval(Mem.empty, e)._2

  def evaluate(s: String): Val = eval(Mem.empty, parse.fromString(s))._2

  def inferType(s: String): Typ = typeInfer(Map.empty, parse.fromString(s))

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
      val (_, v) = eval(Mem.empty, expr)
      println(v.prettyVal)
    }
  }
}
