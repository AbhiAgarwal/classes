package js.hw02

import scala.util.parsing.input.Positional

object ast {
  sealed abstract class Expr extends Positional {
    // pretty print as AST
    override def toString(): String = print.prettyAST(this)
    // pretty print as JS expression
    def prettyJS(): String = print.prettyJS(this)
    // pretty print as value
    def prettyVal(): String = print.prettyVal(this)
  }
  
  /* Literals and Values */
  sealed abstract class Val extends Expr
  case class Num(n: Double) extends Val
  
  /* Unary and Binary Operators */
  case class UnOp(op: Uop, e1: Expr) extends Expr
  case class BinOp(op: Bop, e1: Expr, e2: Expr) extends Expr

  sealed abstract class Uop
  
  case object UMinus extends Uop /* - */

  sealed abstract class Bop
  
  case object Plus extends Bop /* + */
  case object Minus extends Bop /* - */
  case object Times extends Bop /* * */
  case object Div extends Bop /* / */

  /* Define values. */
  def isValue(e: Expr): Boolean = e match {
    case _: Val => true
    case _ => false
  }
  
  /*
   * Pretty-print values.
   * 
   */
  def pretty(v: Expr): String = {
    require(isValue(v))
    (v: @unchecked) match {
      case Num(n) => n.toString
    }
  }
}