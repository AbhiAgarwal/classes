object hw06 extends js.util.JsApp {
  import js.hw06.ast._
  import js.hw06._
  import scala.util.parsing.input.NoPosition
  /*
   * CSCI-UA.0480-003: Homework 6
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

  /* JakartaScript */

  // Value environments
  type Env = Map[String, Val]
  def emp: Env = Map()
  def get(env: Env, x: String): Val = env.getOrElse(x, Undefined)
  def extend(env: Env, x: String, v: Val): Env = env + (x -> v)

  /* Some useful Scala methods for working with Scala values include:
   * - Double.NaN
   * - s.toDouble (for s: String)
   * - n.isNaN (for n: Double)
   * - n.isWhole (for n: Double)
   * - s (for n: Double)
   * - s format n (for s: String [a format string like for printf], n: Double)
   */

  def toNum(v: Val): Double = {
    v match {
      case Num(n) => n
      case Bool(false) => 0
      case Bool(true) => 1
      case Undefined => Double.NaN
      case Str(s) => try s.toDouble catch { case _: Throwable => Double.NaN }
      case Function(_, _, _) => Double.NaN
    }
  }

  def toBool(v: Val): Boolean = {
    v match {
      case Num(n) if (n compare 0.0) == 0 || (n compare -0.0) == 0 || n.isNaN => false
      case Num(_) => true
      case Bool(b) => b
      case Undefined => false
      case Str("") => false
      case Str(_) => true
      case Function(_, _, _) => true
    }
  }

  def toStr(v: Val): String = {
    v match {
      case Num(n) => if (n.isWhole) "%.0f" format n else n.toString
      case Bool(b) => b.toString
      case Undefined => "undefined"
      case Str(s) => s
      case Function(_, _, _) => "function"
    }
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
   * This code is a reference implementation of JakartaScript without
   * functions (i.e., Homework 4). You are welcome to replace it with your
   * code from Homework 4.
   */
  def eval(env: Env, e: Expr): Val = {
    /* Some helper functions for convenience. */
    def eToNum(e: Expr): Double = toNum(eval(env, e))
    def eToBool(e: Expr): Boolean = toBool(eval(env, e))
    def eToVal(e: Expr): Val = eval(env, e)
    e match {
      /* Base Cases */
      case v: Val => v
      case Var(x) => get(env, x)

      /* Inductive Cases */
      case Print(e) =>
        println(eToVal(e).prettyVal); Undefined

      case UnOp(UMinus, e1) => Num(-eToNum(e1))
      case UnOp(Not, e1) => Bool(!eToBool(e1))

      case BinOp(Plus, e1, e2) => (eToVal(e1), eToVal(e2)) match {
        case (Str(s1), v2) => Str(s1 + toStr(v2))
        case (v1, Str(s2)) => Str(toStr(v1) + s2)
        case (v1, v2) => Num(toNum(v1) + toNum(v2))
      }
      case BinOp(Minus, e1, e2) => Num(eToNum(e1) - eToNum(e2))
      case BinOp(Times, e1, e2) => Num(eToNum(e1) * eToNum(e2))
      case BinOp(Div, e1, e2) => Num(eToNum(e1) / eToNum(e2))

      case BinOp(bop @ (Eq | Ne), e1, e2) => {
        def checkFunction(e: Expr): Bool = e match {
          case Function(_, _, _) => throw DynamicTypeError(e)
          case _ => return Bool(true)
        }
        val e1Value = eToVal(e1)
        checkFunction(e1Value)
        val e2Value = eToVal(e2)
        checkFunction(e2Value)
        bop match {
          case Eq => Bool(e1Value == e2Value)
          case Ne => Bool(e1Value != e2Value)
        }
      }

      case BinOp(bop @ (Lt | Le | Gt | Ge), e1, e2) =>
        Bool(inequalityVal(bop, eToVal(e1), eToVal(e2)))

      case BinOp(And, e1, e2) =>
        val v1 = eToVal(e1)
        if (toBool(v1)) eToVal(e2) else v1
      case BinOp(Or, e1, e2) =>
        val v1 = eToVal(e1)
        if (toBool(v1)) v1 else eToVal(e2)

      case BinOp(Seq, e1, e2) =>
        eToVal(e1); eToVal(e2)

      case If(e1, e2, e3) => if (eToBool(e1)) eToVal(e2) else eToVal(e3)

      case ConstDecl(x, ed, eb) => eval(extend(env, x, eToVal(ed)), eb)

      case Call(e1, e2) =>
        val v1 = eToVal(e1)
        v1 match {
          case Function(p, x, e) =>
            val env1 = p match {
              case None =>
                extend(env, x, eToVal(e2))
              case Some(x1) =>
                extend(extend(env, x1, v1), x, eToVal(e2))
            }
            eval(env1, e)
          case _ => throw DynamicTypeError(e)
        }
    }
  }

  // Interface to run your big-step interpreter starting from an empty environment and print out
  // the test input if debugging.
  def evaluate(e: Expr): Val = {
    require(closed(e))
    if (debug) {
      println("------------------------------------------------------------")
      println("Evaluating with eval ...")
      println(s"\nExpression:\n $e")
    }
    val v = eval(emp, e)
    if (debug) {
      println(s"Value: $v")
    }
    v
  }

  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def evaluate(s: String): Val = eval(emp, parse.fromString(s))

  /* Small-Step Interpreter with Static Scoping */

  def subst(e: Expr, x: String, v: Val): Expr = {
    require(closed(v))
    /* Simple helper that calls substitute on an expression
     * with the input value v and variable name x. */
    def substX(e: Expr): Expr = subst(e, x, v)
    /* Body */
    e match {
      case Num(_) | Bool(_) | Undefined | Str(_) => e
      case Print(e1) => Print(substX(e1))
      case UnOp(op, e1) => UnOp(op, substX(e1))
      case BinOp(op, e1, e2) => BinOp(op, substX(e1), substX(e2))
      case Call(e1, e2) => Call(substX(e1), substX(e2))
      case If(e1, e2, e3) => If(substX(e1), substX(e2), substX(e3))
      case ConstDecl(a, ed, eb) => ConstDecl(a, substX(ed), eb)
      case Function(None, a, e1) => Function(None, a, substX(e1))
      case Function(Some(b), a, e1) => Function(Some(b), a, substX(e1))
      case Var(_) => v
    }
  }

  def step(e: Expr): Expr = {
    e match {
      /* Base Cases: Do Rules */
      case Print(v: Val) =>
        println(v.prettyVal); Undefined
      case UnOp(UMinus, v: Val) => Num(-toNum(v))
      case UnOp(Not, v: Val) => Bool(!toBool(v))
      case BinOp(Seq, v1: Val, e2) => e2
      case BinOp(bop @ (Eq | Ne), v1: Val, Function(p, x, e)) =>
        throw DynamicTypeError(Function(p, x, e))
      case BinOp(bop @ (Eq | Ne), Function(p, x, e1), e2) =>
        throw DynamicTypeError(Function(p, x, e1))
      case BinOp(bop @ (Plus), v1: Val, v2: Val) => (v1, v2) match {
        case (Str(v1), v2) => Str(v1 + toStr(v2))
        case (v1, Str(v2)) => Str(toStr(v1) + v2)
        case (v1, v2) => Num(toNum(v1) + toNum(v2))
      }
      case BinOp(bop @ (Minus | Times | Div), v1: Val, v2: Val) => {
        val v1Num = toNum(v1)
        val v2Num = toNum(v2)
        bop match {
          case Minus => Num(v1Num - v2Num)
          case Times => Num(v1Num * v2Num)
          case Div => Num(v1Num / v2Num)
        }
      }
      case BinOp(bop @ (Eq | Ne | Ge | Gt | Le | Lt), v1: Val, v2: Val) => (bop) match {
        // inequalityVal works with anything but Eq/Ne.
        case Eq => Bool(v1 == v2)
        case Ne => Bool(v1 != v2)
        case bop => Bool(inequalityVal(bop, v1, v2))
      }
      case BinOp(And, v1: Val, e2) => {
        if (toBool(v1)) e2
        else v1
      }
      case BinOp(Or, v1: Val, e2) => {
        if (toBool(v1)) v1
        else e2
      }
      case If(v1: Val, e2, e3) => {
        if (toBool(v1)) e2
        else e3
      }
      case ConstDecl(x, v1: Val, e1) => subst(e1, x, v1)
      case Call(v1: Val, v2: Val) => v1 match {
        case Function(None, x1, e1) => subst(e1, x1, v2)
        case Function(Some(x1), x2, e1) =>
          subst(subst(e1, x1, v1), x2, v2)
        case _ => throw DynamicTypeError(e)
      }

      /* Inductive Cases: Search Rules */
      case Print(e) => Print(step(e))
      case UnOp(uop, e) => UnOp(uop, step(e))
      case BinOp(op, v1: Val, e1) if ((v1 != Function) && ((op == Eq) || (op == Ne))) => BinOp(op, v1, step(e1))
      case BinOp(op, v1: Val, e1) if ((op != And) && (op != Or) && (op != Eq) && (op != Ne) && (op != Seq)) => BinOp(op, v1, step(e1))
      case BinOp(op, e1, e2) => BinOp(op, step(e1), e2)
      case If(e1, e2, e3) => If(step(e1), e2, e3)
      case ConstDecl(x, e1, e2) => ConstDecl(x, step(e1), e2)
      case Call(e1, e2) => Call(step(e1), e2)
      case Call(v1: Val, e1) => Call(v1, step(e1))

      /* Cases that should never match. Your cases above should ensure this. */
      case Var(_) => throw new AssertionError("Gremlins: internal error, not a closed expression:\n%s".format(e))
      case v: Val => throw new AssertionError("Gremlins: internal error, step should not be called on values:\n%s".format(e));
    }
  }

  // Interface to run your small-step interpreter and print out the steps of evaluation if debugging. 
  def iterateStep(e: Expr): Val = {
    require(closed(e))
    def loop(e: Expr, n: Int): Val = {
      if (debug) { println(s"Step $n: $e") }
      e match {
        case v: Val => v
        case e =>
          // take a step
          val ep = step(e)
          // preserve source code position of e if possible
          val epp = if (ep.pos != NoPosition) ep else ep.setPos(e.pos)
          // and continue evaluation
          loop(epp, n + 1)
      }
    }
    if (debug) {
      println("------------------------------------------------------------")
      println("Evaluating with step ...")
    }
    val v = loop(e, 0)
    if (debug) {
      println("Value: " + v)
    }
    v
  }

  // Convenience to pass in a js expression as a string.
  def iterateStep(s: String): Val = iterateStep(parse.fromString(s))

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
      val v = evaluate(expr)
      println(v.prettyVal)
    }

    handle() {
      val v1 = iterateStep(expr)
      println(v1.prettyVal)
    }
  }
}
