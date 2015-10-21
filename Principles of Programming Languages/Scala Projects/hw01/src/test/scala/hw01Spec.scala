import org.scalatest._
import hw01._


class hw01Spec extends FlatSpec {

  // Check that abs works for negative, positive and zero values

  "abs" should "evaluate to the absolute value of the argument" in {
     assert(abs(2) === 2)
     assert(abs(-2) === 2)
     assert(abs(0) === 0)
  }

  // Check that swap works

  "swap" should "should swap the components of its argument" in {
    assert(swap(1,2) == (2,1))
    assert(swap(5,4) == (4,5))
    assert(swap(0,0) == (0,0))
  } 
  
  // Check that repeat works

  "repeat" should "evaluate to repeated concatenation of a string" in {
    assert(repeat("a", 3) === "aaa")
    assert(repeat("abc", 3) === "abcabcabc")
    assert(repeat("a", 1) === "a")
    assert(repeat("abc", 1) === "abc")
    assert(repeat("abc", 4) === "abcabcabcabc")
    assert(repeat("", 5) === "")
  }

  "repeat" should "evaluate to an empty string when repeated zero times" in {
    assert(repeat("abc", 0) === "")
    assert(repeat("", 0) === "")
  }

  // Check that repeat requires a non-negative repetition amount

  "repeat" should "throw an exception when a negative number of repetitions is expected" in {
    intercept[java.lang.IllegalArgumentException] {
      repeat("abc", -3)
    }
  }

  // Check that sqrt works.  This requires steps for sqrtStep, sqrtN, and sqrtErr. 

  "sqrtStep" should "evaluate to one iteration of Newton's method" in {
    assert(sqrtStep(4, 1) === 2.5)
    assert(sqrtStep(1, 1) === 1.0)
    assert(sqrtStep(5, 8) === 4.3125)
  }

  "sqrtN" should "perform several iterations of sqrtStep" in {
    assert(sqrtN(4,1,2) === 2.05)
    assert(sqrtN(4,1,6) === 2.0)
    assert(sqrtN(4,20,2) === 5.248019801980198)
    assert(sqrtN(4,20,6) === 2.0000105791285385)
  }

  "sqrtN" should "evaluate to x0 if n is zero" in {
    assert(sqrtN(4,1,0) === 1.0)
    assert(sqrtN(4,20,0) === 20.0)
  }
 
  "sqrtErr" should "perform iterations until the error is within epsilon" in {
    assert(sqrtErr(4, 1, 0.1) === 2.000609756097561)
    assert(sqrtErr(4, 1, 0.0001) === 2.0000000929222947)
    assert(sqrtErr(4, 1, 0.00000001) === 2.000000000000002)
  } 


  // Make sure that the functions have the correct requires

  "sqrtN" should "throw an exception when n is negative" in {
    intercept[java.lang.IllegalArgumentException] {
      sqrtN(4, 1, -10)
    }
  }

  "sqrtErr" should "throw an exception when using a non-positive epsilon" in {
    intercept[java.lang.IllegalArgumentException] {
      sqrtErr(4, 1, -0.01)
    }
    intercept[java.lang.IllegalArgumentException] {
      sqrtErr(4, 1, 0.0)
    }
  }  
}
