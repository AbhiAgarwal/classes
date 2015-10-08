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
    }
  }
  
  def toStr(v: Val): String = {
    (v: @unchecked) match {
      // Str(s: String)
      case Str(a) => a
      // Num(n: Double)
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
      //case Num(a) => e
      //case Bool(a) => e
      //case Str(a) => e
      case Var(a) => get(env, a)
      //case Undefined => Undefined
      case v: Val =>  v
      
      /* Inductive Cases */
      case Print(e) => println(eToVal(e).prettyVal); Undefined

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
