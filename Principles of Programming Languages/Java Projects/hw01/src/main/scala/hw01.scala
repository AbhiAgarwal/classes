object hw01 extends App {
  /*
   * CSCI-UA.0480-003: Homework 1
   * Abhi Agarwal
   * 
   * Partner: Bob Gardner
   * Collaborators: <Any Collaborators>
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   * 
   * Replace the '???' expressions with your code in each function.
   * 
   * Do not make other modifications to this template, such as
   * leaving any failing asserts.
   * 
   * Your solution will _not_ be graded if it does not compile!!
   * 
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert.  Simply put in a
   * '???' as needed to get something that compiles without error.
   */

  /*
   * Example with a Unit Test
   * 
   * A convenient, quick-and-dirty way to experiment, especially with small code
   * fragments, is to use the interactive Scala interpreter.
   * 
   * To run a selection in the interpreter in Eclipse, highlight the code of interest
   * and type Ctrl+Shift+X (on Windows/Linux) or Cmd+Shift+X (on Mac).
   * 
   * Highlight the next few lines below to try it out.  The assertion passes, so
   * it appears that nothing happens.  You can uncomment the "bad test specification"
   * and see that a failed assert throws an exception.
   * 
   * You can try calling 'plus' with some arguments, for example, plus(1,2).  You
   * should get a result something like 'res0: Int = 3'.
   * 
   * As an alternative, the testPlus2 function takes an argument that has the form
   * of a plus function, so we can try it with different implementations.  For example,
   * uncomment the "testPlus2(badplus)" line, and you will see an assertion failure.
   * 
   * Our convention is that these "test" functions are testing code that are not part
   * of the "production" code.
   * 
   * While writing such testing snippets is convenient, it is not ideal.  For example,
   * the 'testPlus1()' call is run whenever this object is loaded, so in practice,
   * it should probably be deleted for "release".  A more robust way to maintain
   * unit tests is in a separate file.  For us, we use the convention of writing
   * tests in a file called HWXSpec.scala (i.e., hw01Spec.scala for Homework 1).
   */

  def plus(x: Int, y: Int): Int = x + y
  def testPlus1() {
    assert(plus(1, 1) == 2)
    //assert(plus(1,1) == 3) // bad test specification
  }
  testPlus1()

  def badplus(x: Int, y: Int): Int = x - y
  def testPlus2(plus: (Int, Int) => Int) {
    assert(plus(1, 1) == 2)
  }
  //testPlus2(badplus)

  /* Exercises */

  def abs(n: Double): Double = if (n < 0) -1 * n else n

  def swap(p: (Int, Int)): (Int, Int) = (p._2, p._1)

  // A Scala-simple idiomatic solution could be this (if we don't use tail recursion):
  // def repeat(s: String, n: Int): String = {
  //   if (n < 0) throw new IllegalArgumentException("Input n must be greater than zero")
  //   else s * n
  // }

  def repeat(s: String, n: Int): String = {
    val emptyString = ""
    if (n < 0) throw new IllegalArgumentException("Input n must be greater than zero")
    else if (n == 0) emptyString
    def formString(s_2: String, n_2: Int): String = {
      if (n_2 == 0) s_2
      else formString(s_2 + s, n_2 - 1)
    }
    formString(s, n - 1)
  }

  def sqrtStep(c: Double, xn: Double): Double = {
    val minus = ((xn * xn) - c) / (2 * xn)
    xn - minus
  }

  def sqrtN(c: Double, x0: Double, n: Int): Double = {
    // should throw an exception when n is negative
    if (n < 0) throw new IllegalArgumentException("Input n must be greater than zero")
    else if (n == 0) x0
    else if (n == 1) sqrtStep(c, x0)
    else sqrtN(c, sqrtStep(c, x0), n - 1)
  }

  def sqrtErr(c: Double, x0: Double, epsilon: Double): Double = {
    if (epsilon <= 0.00) throw new IllegalArgumentException("Input epsilon must be non-negative")
    else if (abs(x0 * x0 - c) < epsilon) x0
    else sqrtErr(c, sqrtStep(c, x0), epsilon)
  }

  def sqrt(c: Double): Double = {
    require(c >= 0)
    if (c == 0) 0 else sqrtErr(c, 1.0, 0.0001)
  }

}
