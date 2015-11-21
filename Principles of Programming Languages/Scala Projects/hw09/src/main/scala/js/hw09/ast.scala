package js.hw09

import scala.util.parsing.input.Positional
import js.util._

object ast {
  sealed abstract class Expr extends Positional {
    // pretty print as AST
    override def toString(): String = print.prettyAST(this)
    // pretty print as JS expression
    def prettyJS(): String = print.prettyJS(this)
    // pretty print as value
    def prettyVal(): String = 
      print.prettyVal(this)
  }
  
  /* Literals and Values */
  sealed abstract class Val extends Expr
  case class Num(n: Double) extends Val
  case class Bool(b: Boolean) extends Val
  case class Str(s: String) extends Val
  case object Undefined extends Val
  case class Addr private[ast] (a: Int) extends Val
  
  /* Variables */
  case class Var(x: String) extends Expr
  
  /* Declarations */
  case class Decl(mut: Mut, x: String, ed: Expr, eb: Expr) extends Expr
  
  /* Mutabilities */
  sealed abstract class Mut
  case object MConst extends Mut
  case object MVar extends Mut
  
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
  
  /* Addresses and Mutation */
  case object Assign extends Bop /* = */
  case object Deref extends Uop /* * */
  
  /* Control constructs */
  case class If(e1: Expr, e2: Expr, e3: Expr) extends Expr
  
  /* I/O */
  case class Print(e1: Expr) extends Expr
  
  /* Functions */
  type Params = List[(String, (PMode, Typ))]
  case class Function(p: Option[String], xs: Params, t: Option[Typ], e: Expr) extends Val
  case class Call(e1: Expr, es: List[Expr]) extends Expr
  
  /* Parameter Passing Modes */
  sealed abstract class PMode
  case object PConst extends PMode
  case object PName extends PMode
  case object PVar extends PMode
  case object PRef extends PMode
  
  /* Types */
  sealed abstract class Typ {
    // pretty print as AST
    override def toString(): String = print.prettyAST(this)
    // pretty print as JS expression
    def pretty(): String = print.prettyTyp(this)
  }
  case object TNumber extends Typ
  case object TBool extends Typ
  case object TString extends Typ
  case object TUndefined extends Typ
  case class TFunction(ts: List[(PMode,Typ)], tret: Typ) extends Typ

  /* Memory */
  class Mem private (map: Map[Addr, Val], nextAddr: Int) {
    def apply(key: Addr): Val = map(key)
    def get(key: Addr): Option[Val] = map.get(key)
    def +(kv: (Addr, Val)): Mem = new Mem(map + kv, nextAddr)
    def contains(key: Addr): Boolean = map.contains(key)
    
    def alloc(v: Val): (Mem, Addr) = {
      val fresha = Addr(nextAddr)
      (new Mem(map + (fresha -> v), nextAddr + 1), fresha)
    }
    
    override def toString: String = map.toString
  }
  object Mem {
    def empty = new Mem(Map.empty, 1)
  }
  
  
  /* Define values. */
  def isValue(e: Expr): Boolean = e match {
    case _: Val => true
    case _ => false
  }
  
  /* Define statements (used for pretty printing). */
  def isStmt(e: Expr): Boolean = e match {
    case Undefined | Decl(_, _, _, _) | 
         Print(_) => true
    case BinOp(Seq, _, e2) => isStmt(e2)
    case _ => false
  }
  
  /* Get the free variables of e. */
  def fv(e: Expr): Set[String] = e match {
    case Var(x) => Set(x)
    case Decl(_, x, ed, eb) => fv(ed) | (fv(eb) - x)
    case Num(_) | Bool(_) | Undefined | Str(_) | Addr(_) => Set.empty
    case UnOp(_, e1) => fv(e1)
    case BinOp(_, e1, e2) => fv(e1) | fv(e2)
    case If (e1, e2, e3) => fv(e1) | fv(e2) | fv(e3)
    case Print(e1) => fv(e1)
    case Call(e1, es) => fv(e1) | (es.toSet flatMap fv)
    case Function(p, xs, _, e) => fv(e) -- p -- (xs map (_._1))
  }
  
  /* Check whether the given expression is closed. */
  def closed(e: Expr): Boolean = fv(e).isEmpty
  
  /*
  * Dynamic Type Error exception.  Throw this exception to signal a dynamic type error.
  * 
  *   throw DynamicTypeError(e)
  * 
  */
  case class DynamicTypeError(e: Expr) extends JsException("Dynamic Type Error", e.pos)
  
  /*
   * Static Type Error exception.  Throw this exception to signal a static
   * type error.
   * 
   *   throw StaticTypeError(tbad, e)
   * 
   */
  /*
  case class StaticTypeError(texp: Typ, tbad: Typ, e: Expr) extends 
    JsException("Type Error: expected type: " + texp.pretty + " but got: " + tbad.pretty(), e.pos)
  */
  case class StaticTypeError(tbad: Typ, e: Expr) extends 
    JsException("Type Error: unexpected type: " + tbad.pretty(), e.pos)

  /*
   * Location Expression Error exception.  Throw this exception 
   * to signal a location expression error.
   * 
   *   throw LocExprError(e)
   * 
   */
  case class LocTypeError(e: Expr) extends 
    JsException("Type Error: expected location expression", e.pos)
  
  /*
  * Stuck Type Error exception.  Throw this exception to signal getting
  * stuck in evaluation.  This exception should not get raised if
  * evaluating a well-typed expression.
  * 
  *   throw StuckError(e)
  * 
  */
  case class StuckError(e: Expr) extends JsException("stuck while evaluating expression", e.pos)

}