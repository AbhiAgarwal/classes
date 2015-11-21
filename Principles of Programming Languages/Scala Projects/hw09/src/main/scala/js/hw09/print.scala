package js.hw09

import org.kiama.output.PrettyPrinter
import ast._

object print extends PrettyPrinter {
  override val defaultIndent = 2
  
  /*
   * Pretty-print values.
   * 
   * We do not override the toString method so that the abstract syntax can be printed
   * as is.
   */
  def prettyVal(v: Expr): String = {
    require(isValue(v))
    (v: @unchecked) match {
      case Num(n) => n.toString
      case Bool(b) => b.toString
      case Str(s) => s
      case Undefined => "undefined"
      case Addr(a) => s"@$a"
      case Function(p, _, _, _) =>
        "[Function%s]".format(p match { case None => "" case Some(s) => ": " + s })      
    }
  }
  
  def prettyVal(m: Mem, v: Expr): String = {
    (v: @unchecked) match {
      case a @ Addr(_) if m contains a => prettyVal(m, m(a))
      case _ => prettyVal(v)
    }
  }
  
  /*
   * Determine precedence level of top-level constructor in an expression
   */  
  def prec(e: Expr): Int =
    e match {
      case v: Val => 0
      case Var(_) => 0
      case Print(_) | Call(_, _) => 1
      case UnOp(_, _) => 2
      case BinOp(bop, _, _) =>
        bop match {
          case Times | Div => 3
          case Plus | Minus => 4
          case Gt | Ge | Lt | Le => 6
          case Eq | Ne => 7
          case And => 11
          case Or => 12
          case Assign => 16
          case Seq => 17
        }
      case If(_, _, _) | Decl(_, _, _, _) => 15
    }
  
  /* Associativity of binary operators */
  sealed abstract class Assoc
  case object Left extends Assoc
  case object Right extends Assoc
  
  /* 
   * Get associativity of a binary operator 
   * (all current operators are left-associative) 
   */
  def assoc(bop: Bop): Assoc = bop match {
    case _ => Left
  }
  
  def showTyp(typ: Typ): Doc = 
    typ match {
      case TBool => "bool"
      case TNumber => "number"
      case TString => "string"
      case TUndefined => "Undefined"
      case TFunction(txs, tret) =>
        parens(ssep(txs map showPTyp, comma <> space)) <+> "=>" <+> showTyp(tret)
    }
  
  def showPTyp(pt: (PMode, Typ)): Doc =
    showPMode(pt._1) <+> showTyp(pt._2)
  
  def showTIdList(txs: Params, sep: Doc = (comma <> space)): Doc = 
    ssep(txs map showTId, sep)
    
  def showPMode(pm: PMode): Doc = pm match {
    case PConst => "const"
    case PVar => "var"
    case PName => "name"
    case PRef => "ref"
  }
    
  def showTId(tid: (String, (PMode, Typ))): Doc = {
    showPMode(tid._2._1) <+> tid._1 <> colon <+> showTyp(tid._2._2)
  }

  
  /*
   * Pretty-print expressions in concrete JavaScript syntax.
   */
  def showJS(e: Expr): Doc = {
    def showDecl(m: Mut, x: String, e1: Expr): Doc = {
      val mut = m match {
        case MConst => "const"
        case MVar => "var"
      }
      mut <+> x <+> "=" <+> 
      nest(showJS(e1)) <> semi <> line 
    }
    
    e match {
      case Undefined => "undefined"
      case Num(d) => value(d)
      case Bool(b) => b.toString()
      case Str(s) => "'" <> s <> "'"
      case Addr(a) => "@a"
      case Var(x) => x
      case eu @ UnOp(uop, e) =>
        val op: Doc = uop match {
          case UMinus => "-"
          case Not => "!"
          case Deref => "*"
        }
        op <+> (if (prec(e) < prec(eu)) showJS(e) else parens(showJS(e)))
      case BinOp(bop, e1, e2) => {
        val op: Doc = bop match {
          case Plus => " + "
          case Minus => " - "
          case Times => " * "
          case Div => " / "
          case And => " && "
          case Or => " || "
          case Eq => " === "
          case Ne => " !== "
          case Lt => " < "
          case Le => " <= "
          case Gt => " > "
          case Ge => " >= "
          case Assign => " = "
          case Seq => 
            if (isStmt(e2)) ";" <> line else ", "
        }
        def eToDoc(e1: Expr, as: Assoc): Doc =
          if (prec(e1) < prec(e) || prec(e1) == prec(e) && as == assoc(bop)) 
            showJS(e1) 
          else parens(showJS(e1))
        eToDoc(e1, Left) <> op <> eToDoc(e2, Right)
      }
      case ei @ If(e1, e2, e3) => {
        def eToDoc(e: Expr): Doc = {
          if (prec(e) < prec(ei)) showJS(e)
          else parens(showJS(e))
        }
        eToDoc(e1) <+> "?" <+> eToDoc(e2) <+> ":" <+> eToDoc(e3)
      }
      case Print(e) =>
        "console.log" <> parens(showJS(e))
      case Decl(mut, x, e1, e2) =>
        showDecl(mut, x, e1) <> line <> showJS(e2)
      case Call(e1, List(e @ BinOp(Seq, _, _))) if isStmt(e) =>
        showJS(e1) <> parens(braces(line <> indent(showJS(e)) <> line))
      case Call(e1, es) =>
        showJS(e1) <> parens(hsep(es map showJS, comma))
      case Function(p, xs, tann, e) =>
        def showReturn(e: Expr): Doc = e match {
          case BinOp(Seq, e1, e2) =>
            line <> showJS(e1) <> semi <> showReturn(e2)
          case Decl(mut, x, e1, e2) =>
            line <> showDecl(mut, x, e1) <> showReturn(e2)
          case Undefined => empty
          case e => line <> "return" <+> showJS(e)
        }
        val name = p getOrElse ""
        val params = showTIdList(xs)
        val rtyp = tann map (":" <+> showTyp(_)) getOrElse empty
        "function" <+> name <> 
          params <> rtyp <+> braces(nest(showReturn(e)) <> line)
    }  
  }
  
  def prettyAST(x: Any): String = pretty(any(x))
  def prettyJS(e: Expr): String = pretty(showJS(e))
  def prettyTyp(typ: Typ): String = pretty(showTyp(typ))
}
  
  