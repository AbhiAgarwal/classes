package js.hw04

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
  case class Bool(b: Boolean) extends Val
  case class Str(s: String) extends Val
  case object Undefined extends Val
  
  /* Variables */
  case class Var(x: String) extends Expr
  
  /* Declarations */
  case class ConstDecl(x: String, ed: Expr, eb: Expr) extends Expr
  
  /* Unary and Binary Operators */
  case class UnOp(op: Uop, e1: Expr) extends Expr
  case class BinOp(op: Bop, e1: Expr, e2: Expr) extends Expr

  sealed abstract class Uop
  case object UMinus extends Uop /* - */
  case object Not extends Uop /* ! */

  sealed abstract class Bop
  case object Plus extends Bop /* + */
  case object Minus extends Bop /* - */
  case object Times extends Bop /* * */
  case object Div extends Bop /* / */

  case object Eq extends Bop /* === */
  case object Ne extends Bop /* !== */
  case object Lt extends Bop /* < */
  case object Le extends Bop /* <= */
  case object Gt extends Bop /* > */
  case object Ge extends Bop /* >= */
  
  case object And extends Bop /* && */
  case object Or extends Bop /* || */
  
  case object Seq extends Bop /* , */
  
  /* Control constructs */
  case class If(e1: Expr, e2: Expr, e3: Expr) extends Expr
  
  /* I/O */
  case class Print(e1: Expr) extends Expr
  
  /* Define values. */
  def isValue(e: Expr): Boolean = e match {
    case _: Val => true
    case _ => false
  }
  
  /* Define statements (used for pretty printing). */
  def isStmt(e: Expr): Boolean = e match {
    case Undefined | ConstDecl(_, _, _) | 
         Print(_) => true
    case BinOp(Seq, _, e2) => isStmt(e2)
    case _ => false
  }
  
  /* Get the free variables of e. */
  def fv(e: Expr): Set[String] = e match {
    case Var(x) => Set(x)
    case ConstDecl(x, ed, eb) => fv(ed) | (fv(eb) - x)
    case Num(_) | Bool(_) | Undefined | Str(_) => Set.empty
    case UnOp(_, e1) => fv(e1)
    case BinOp(_, e1, e2) => fv(e1) | fv(e2)
    case If (e1, e2, e3) => fv(e1) | fv(e2) | fv(e3)
    case Print(e1) => fv(e1)
  }
  
  /* Check whether the given expression is closed. */
  def closed(e: Expr): Boolean = fv(e).isEmpty
  
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