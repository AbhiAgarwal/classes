object hw03 extends App {  
  /*
   * CSCI-UA.0480-003: Homework 3
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
  
   sealed abstract class List
   case class Cons(hd: Int, tl: List) extends List
   case object Nil extends List
  
   def filter(n: Int, l: List): List = ???
}
