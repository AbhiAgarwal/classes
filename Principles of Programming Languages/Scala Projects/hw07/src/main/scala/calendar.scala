import scala.language.postfixOps

object calendar extends App {
  /*
   * CSCI-UA.0480-003: Homework 7, Voluntary Problem 3
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
  
  
  // 1 line: reduce, ++, ::
  def unlines(lines: List[List[Char]]): List[Char] = ???
    
  /**
   * The weekday of January 1st in year y, represented
   * as an Int. 0 is Sunday, 1 is Monday, etc.
   */
  def firstOfJan(y: Int): Int = {
    val x = y - 1
    (365*x + x/4 - x/100 + x/400 + 1) % 7
  }

  def isLeapYear(y: Int) =
    if (y % 100 == 0) (y % 400 == 0) else (y % 4 == 0)

  def mlengths(y: Int): List[Int] = {
    val feb = if (isLeapYear(y)) 29 else 28
    List(31, feb, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
  }

  // 1 line: take, sum
  def firstDay(m: Int, y: Int): Int = ???
   
  case class Picture(height: Int, width: Int, pxx: List[List[Char]]) {
    def showIt: String = unlines(pxx).mkString

    // 1 line, +, ++
    def above(q: Picture) = {
      require(width == q.width)
      ???
    }

    // 1 line: zip, map, ++
    def beside(q: Picture) = {
      require(height == q.height)
      ???
    }
  }

  def pixel(c: Char) = Picture(1, 1, List(List(c)))

  // 1 line: reduce
  def stack(pics: List[Picture]): Picture = ???

  // 1 line: reduce
  def spread(pics: List[Picture]): Picture = ???

  // 1 line: map
  def tile(pxx: List[List[Picture]]): Picture = ???

  
  // 1 line: reverse, padTo, reverseMap
  def rightJustify(w: Int)(chars: List[Char]): Picture = {
    require(chars.length <= w)
    ???
  }

  // 1 line: grouped, toList
  def group[T](n: Int, xs: List[T]): List[List[T]] = ???
    
  // 4 lines: to (of class RichInt), map, toString, toList, ++
  def dayPics(d: Int, s: Int): List[Picture] =  ???
  
  // 3 lines: map, toList, ++
  def calendar(year: Int, month: Int): Picture = ???

  /** Your code for testing */
 
  println("2015 is leap year: " + isLeapYear(2015))

  println(unlines(List(List('f','e','i','s','t','y'),List('f','a','w','n'))))

}
