object hw04 extends js.util.JsApp {
  import js.hw04.ast._
  import js.hw04._
  
  /*
   * CSCI-UA.0480-003: Homework 4
   * <Your Name>
   * 
   * Partner: <Your Partner's Name>
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

  def toNum(v: Val): Double = {
    (v: @unchecked) match {
      case Num(n) => n
      case _ => ???
    }
  }
  
  def toBool(v: Val): Boolean = {
    (v: @unchecked) match {
      case Bool(b) => b
      case _ => ???
    }
  }
  
  def toStr(v: Val): String = {
    (v: @unchecked) match {
      case Str(s) => s
      case Undefined => "undefined"
      case _ => ???
    }
  }

  
  def eval(env: Env, e: Expr): Val = {
    /* Some helper functions for convenience. */
    def eToVal(e: Expr): Val = eval(env, e)
    e match {
      /* Base Cases */
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
