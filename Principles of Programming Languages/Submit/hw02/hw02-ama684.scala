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
      *   1. Traverse to the less-most node
          2. Return an instance of BSTree and minimum value
            - New instance with new updated left with the same data and right.
    */
    require(t != Empty)
    (t: @unchecked) match {
      case Node(Empty, data, right) => (right, data)
      case Node(left, data, right) =>
        // Returns a tuple
        val (left1, min) = deleteMin(left); (Node(left1, data, right), min)
    }
  }

  def delete(t: BSTree, n: Int): BSTree = {
    /*
      * Removes the First node with data value equal to n.
      * If no such node exists, the tree should be returned unmodified.
      * Cases:
      *   1. N equals data -> method to delete current node and return true
          2. N less than data -> traverse left
          3. N greater than or equal to data -> traverse right

      * Designing a function deleteCurrentNode that takes a Tree and outputs
        a new Tree.
    */

    // A way to delete the current node
    def deleteCurrentNode(tree: BSTree): BSTree = {
      /*
        * Case 1: Empty tree
        * Case 2: Reached a leaf
        * Case 3: Traverse left
        * Case 4: Traverse right
        * Case 5: Part that removes the element
        *   1. Delete minimum right element
            2. Get new right and left element
            3. Return a new Node with a new left, data, and right
      */
      tree match {
        // Case 1
        case Empty => Empty
        // Case 2
        case Node(Empty, data, Empty) => Empty
        // Case 3
        case Node(left, data, Empty) => left
        // Case 4
        case Node(Empty, data, right) => right
        // Case 5
        case Node(left, data, right) =>
          val (rt, l) = deleteMin(right); Node(left, l, rt)
      }
    }

    // Searches through the Binary Search tree
    t match {
      case Empty => Empty
      case Node(left, data, right) => {
        // Separate cases:
        // If the current element that we are at is the element
        // we want to delete:
        if (n == data) deleteCurrentNode(t)
        // If n is less than data then traverse left:
        else if (n < data) Node(delete(left, n), data, right)
        // If n is greater or equal than data then traverse right:
        else Node(left, data, delete(right, n))
      }
    }

  }

  /* JakartaScript */

  def eval(e: Expr): Double = e match {
    /*
      * in file ast.scala
      * sealed abstract class Expr extends Positional
      * sealed abstract class Val extends Expr
      * case class Num(n: Double) extends Val
      * Num(n) n
    */
    case Num(n) => n
    /* 
      * case class UnOp(op: Uop, e1: Expr) extends Expr
      * UnOp(uop,e1) uop e1
      * case object UMinus extends Uop, Uminus -
    */
    case UnOp(UMinus, e) => -eval(e)
    /*
      * case class BinOp(op: Bop, e1: Expr, e2: Expr) extends Expr
      * BinOp(bop,e1,e2) e1 bop e2
      * sealed abstract class Bop
      * case object Plus extends Bop
      * Plus +
    */
    case BinOp(Plus, e1, e2) => eval(e1) + eval(e2)
    /*
      * case object Minus extends Bop
      * Minus -
    */
    case BinOp(Minus, e1, e2) => eval(e1) - eval(e2)

    /*
      * case object Times extends Bop
      * Times *
    */
    case BinOp(Times, e1, e2) => eval(e1) * eval(e2)

    /*
      * case object Div extends Bop
      * Div /
    */
    case BinOp(Div, e1, e2) => eval(e1) / eval(e2)

    // Base case
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
