object hw04 extends js.util.JsApp {
  import js.hw04.ast._
  import js.hw04._

  /*
   * CSCI-UA.0480-003: Homework 4
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
  def get(env: Env, x: String): Val = env(x)
  def extend(env: Env, x: String, v: Val): Env = env + (x -> v)

  /* Some useful Scala methods for working with Scala values include:
   * - Double.NaN
   * - s.toDouble (for s: String)
   * - n.isNaN (for n: Double)
   * - n.isWhole (for n: Double)
   * - s (for n: Double)
   * - s format n (for s: String [a format string like for printf], n: Double)
   */

  /*
   * All types to convert:
   * - Num(n: Double)
   * - Bool(b: Boolean)
   * - Str(s: String)
   * Error:
   * - Undefined
   */

  def toNum(v: Val): Double = {
    (v: @unchecked) match {
      // Num(n: Double)
      case Num(a) => a
      // Bool(b: Boolean) => Num. Bool -> False => 0, Bool -> True => 1.
      case Bool(false) => 0
      case Bool(true) => 1
      // Str(s: String) => Double.
      case Str(a) => {
        // Using s.toDouble (for s: String). Try catch to see if it's possible.
        try {
          a.toDouble
        } catch {
          // Don't throw an exception. Just exit out of the try catch.
          case e: NumberFormatException =>
        }
        // If the conversion is not possible then return Double.NaN.
        Double.NaN
      }
      // Undefined. Returns Double.NaN.
      case Undefined => Double.NaN
      case _ => Double.NaN
    }
  }

  def toBool(v: Val): Boolean = {
    (v: @unchecked) match {
      // Bool(b: Boolean)
      case Bool(a) => a
      // Num(n: Double). If n == 0 => false, n != 0 => true.
      case Num(0) => false
      case Num(a) => true
      // Str(s: String). If s == EmptyString or "" => false.
      // S == NonEmpty => true
      case Str("") => false
      case Str(a) => true
      // Undefined. Returns false.
      case Undefined => false
      case _ => false
    }
  }

  def toStr(v: Val): String = {
    (v: @unchecked) match {
      // Str(s: String)
      case Str(a) => a
      // Num(n: Double)
      // Handle single digit and Double digit case
      case Num(a) => a.toString
      // Bool(b: Boolean)
      case Bool(false) => "false"
      case Bool(true) => "true"
      // Undefined. Returns "undefined"
      case Undefined => "undefined"
    }
  }

  def eval(env: Env, e: Expr): Val = {
    /* Some helper functions for convenience. */
    def eToVal(e: Expr): Val = eval(env, e)
    e match {
      /* Base Cases */
      // sealed abstract class Val extends Expr
      case v: Val => v
      // case class Var(x: String) extends Expr
      case Var(a) => get(env, a)

      /* Inductive Cases */
      // case class Print(e: Expr) extends Expr
      case Print(e) =>
        println(eToVal(e).prettyVal); Undefined

      // case class ConstDecl(x: String, ed: Expr, eb: Expr) extends Expr
      case ConstDecl(x, ed, eb) => {
        val newExprVal = eToVal(ed)
        val extendedEnv = extend(env, x, newExprVal)
        eval(extendedEnv, eb)
      }

      // case class UnOp(uop: Uop, e: Expr) extends Expr
      // case object UMinus extends Uop −
      case UnOp(UMinus, e) =>
        val newExprVal = eToVal(e); Num(-1 * toNum(newExprVal))

      // case object Not extends Uop !
      case UnOp(Not, e) =>
        val newExprVal = toBool(eToVal(e)); Bool(!newExprVal)

      // case class BinOp(bop: Bop, e1: Expr, e2: Expr) extends Expr
      // case object Plus extends Bop +
      case BinOp(Plus, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        // Many cases. Can either be StrStr, NumNum, NumStr, StrNum
        // Match cases
        (newExprValE1, newExprValE2) match {
          case (Str(a), b) => {
            val strValueReturn = Str("%s%s" format (a, toStr(b)))
            Str(toStr(strValueReturn))
          }
          case (a, Str(b)) => {
            val strValueReturn = Str("%s%s" format (toStr(a), b))
            Str(toStr(strValueReturn))
          }
          // Base case
          case _ => Num(toNum(newExprValE1) + toNum(newExprValE2))
        }
      }

      // case object Minus extends Bop −
      case BinOp(Minus, e1, e2) => {
        val newExprValE1 = toNum(eToVal(e1))
        val newExprValE2 = toNum(eToVal(e2))
        Num(newExprValE1 - newExprValE2)
      }

      // case object Times extends Bop ∗
      case BinOp(Times, e1, e2) => {
        val newExprValE1 = toNum(eToVal(e1))
        val newExprValE2 = toNum(eToVal(e2))
        Num(newExprValE1 * newExprValE2)
      }

      // case object Div extends Bop /
      case BinOp(Div, e1, e2) => {
        val newExprValE1 = toNum(eToVal(e1))
        val newExprValE2 = toNum(eToVal(e2))
        Num(newExprValE1 / newExprValE2)
      }

      // case object Eq extends Bop ===
      case BinOp(Eq, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        Bool(newExprValE1 == newExprValE2)
      }

      // case object Ne extends Bop !==
      case BinOp(Ne, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        Bool(newExprValE1 != newExprValE2)
      }

      // case object Lt extends Bop <
      // String case
      case BinOp(Lt, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        (newExprValE1, newExprValE2) match {
          case (Str(a), Str(b)) =>
            val boolValue = a < b; Bool(boolValue)
          case _ => Bool(toNum(newExprValE1) < toNum(newExprValE2))
        }
      }

      // case object Le extends Bop <=
      case BinOp(Le, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        (newExprValE1, newExprValE2) match {
          case (Str(a), Str(b)) =>
            val boolValue = a <= b; Bool(boolValue)
          case _ => Bool(toNum(newExprValE1) <= toNum(newExprValE2))
        }
      }

      // case object Gt extends Bop >
      case BinOp(Gt, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        (newExprValE1, newExprValE2) match {
          case (Str(a), Str(b)) =>
            val boolValue = a > b; Bool(boolValue)
          case _ => Bool(toNum(newExprValE1) > toNum(newExprValE2))
        }
      }

      // case object Ge extends Bop >=
      case BinOp(Ge, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        (newExprValE1, newExprValE2) match {
          case (Str(a), Str(b)) =>
            val boolValue = a >= b; Bool(boolValue)
          case _ => Bool(toNum(newExprValE1) >= toNum(newExprValE2))
        }
      }

      // case object And extends Bop &&
      case BinOp(And, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        if (toBool(newExprValE1)) eToVal(e2)
        else newExprValE1
      }

      // case object Or extends Bop ||
      case BinOp(Or, e1, e2) => {
        val newExprValE1 = eToVal(e1)
        val newExprValE2 = eToVal(e2)
        (newExprValE1, newExprValE2) match {
          case (Bool(a), b) => {
            val newExpr1 = toBool(newExprValE1)
            if (newExpr1 == true) Bool(true)
            else newExprValE2
          }
          case _ => newExprValE1
        }
      }

      // case object Seq extends Bop , ;
      case BinOp(Seq, e1, e2) =>
        eToVal(e1); eToVal(e2)

      // case class If(e1: Expr, e2: Expr, e3: Expr) extends Expr
      case If(e1, e2, e3) => {
        val newExprValE1 = toBool(eToVal(e1))
        if (newExprValE1) eToVal(e2)
        else eToVal(e3)
      }

      case _ => ???
    }
  }

  // Interface to run your interpreter starting from an empty environment.
  def eval(e: Expr): Expr = eval(emp, e)

  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def eval(s: String): Val = eval(emp, parse.fromString(s))

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
