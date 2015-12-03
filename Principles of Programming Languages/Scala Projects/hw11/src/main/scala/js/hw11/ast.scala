package js.hw11

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
  
  /* memory contents */
  sealed abstract trait Con
  
  /* Literals and Values */
  sealed abstract class Val extends Expr with Con
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
  type Params = List[(String, Typ)]
  case class Function(p: Option[String], xs: Params, t: Option[Typ], e: Expr) extends Val
  case class Call(e1: Expr, es: List[Expr]) extends Expr
    
  /* Objects */
  type Fld = String
  case class Obj(fes: Map[Fld, (Mut, Expr)]) extends Expr
  case class FldDeref(f: Fld) extends Uop /* .f */
  
  /* Object values (to be stored in memory) */
  case class ObjVal(fvs: Map[Fld, Val]) extends Con
  
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
  case class TFunction(ts: List[Typ], tret: Typ) extends Typ
  case class TObj(fts: Map[Fld, (Mut, Typ)]) extends Typ
  
  
  /* Memory */
  class Mem private (map: Map[Addr, Con], nextAddr: Int) {
    def apply(a: Addr): Con = map(a)
    def get(a: Addr): Option[Con] = map.get(a)
    def +(ak: (Addr, Con)): Mem = new Mem(map + ak, nextAddr)
    def contains(key: Addr): Boolean = map.contains(key)
    
    def alloc(k: Con): (Mem, Addr) = {
      val fresha = Addr(nextAddr)
      (new Mem(map + (fresha -> k), nextAddr + 1), fresha)
    }
    
    override def toString: String = map.toString
  }
  object Mem {
    def empty = new Mem(Map.empty, 1)
    def alloc(k: Con): State[Mem, Addr] = State(_.alloc(k))
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
  
  def hasFunction(e: Expr): Boolean = e match {
    case Function(_, _, _, _) => true
    case BinOp(_, e1, e2) => hasFunction(e1) || hasFunction(e2)
    case UnOp(_, e1) => hasFunction(e1)
    case Print(e1) => hasFunction(e1)
    case Decl(_, _, e1, e2) => hasFunction(e1) || hasFunction(e2)
    case Obj(fs) => fs exists { case (_, (_, e)) => hasFunction(e) }
    case Call(e, args) => (e :: args) exists hasFunction
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
    case Obj(fs) =>       
      fs.values.foldLeft(Set.empty: Set[String]){ case(acc, (_, e)) => acc | fv(e) }
    
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