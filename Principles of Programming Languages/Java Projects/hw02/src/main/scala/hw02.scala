object hw02 extends js.util.JsApp {
  import js.hw02.ast._
  import js.hw02._

  /*
   * CSCI-UA.0480-003: Homework 2
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

  /* Binary Search Tree */

  sealed abstract class BSTree
  case object Empty extends BSTree
  case class Node(left: BSTree, data: Int, right: BSTree) extends BSTree

  def repOk(t: BSTree): Boolean = {
    def check(t: BSTree, min: Int, max: Int): Boolean = t match {
      case Empty => true
      /*
        * Checks that an instance of BSTree is a valid binary search tree.
        * Checks using a traversal of the tree the ordering invariant.
        *   1. Make sure that all the values are in the min/max bound.
            2. Traverse through the tree. Traverse left half then right half.
              - While adjusting min/max.
      */
      case Node(left, data, right) => {
        data >= min && data <= max && check(left, min, data) && check(right, data, max)
      }
    }
    check(t, Int.MinValue, Int.MaxValue)
  }

  def insert(t: BSTree, n: Int): BSTree = {
    /*
      * Inserts an integer into the binary search tree.
      * Use pattern matching to understand if the Tree is empty or not. 
      * Construct and return a new output tree that is the input tree t with
        the additional integer n as opposed to destructively updating the 
        input tree.
        *   1. If value to insert is less than root value then traverse left
            2. If value to insert is more or equal than root value 
              then traverse right
            3. Keep traversing either left or right until find where it 
              should be inserted
            4. Return the new Node.
    */
    t match {
      case Empty => {
        Node(Empty, n, Empty)
      }
      case Node(left, data, right) => {
        if (n < data) Node(insert(left, n), data, right)
        else Node(left, data, insert(right, n))
      }
    }
  }

  def deleteMin(t: BSTree): (BSTree, Int) = {
    /*
      * Deletes the smallest data element in the search tree.
      * It returns both the updated tree and the data value of 
        the deleted node.
      *   1. 
          2. Return an instance of BSTree and minimum value
    */
    require(t != Empty)
    (t: @unchecked) match {
      case Node(Empty, data, right) => (right, data)
      case Node(left, data, right) =>
        val (left1, min) = deleteMin(left)
        // Returns a tuple
        ???
    }
  }

  def delete(t: BSTree, n: Int): BSTree = ???

  /* JakartaScript */

  def eval(e: Expr): Double = e match {
    case Num(n) => ???
    case _ => ???
  }

  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def eval(s: String): Double = eval(parse.fromString(s))

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
      println(v)
    }
  }
}
