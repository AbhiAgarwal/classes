object hw10 extends js.util.JsApp {
  import js.hw10.ast._
  import js.hw10._
  import js.util.State
  import scala.util.parsing.input.NoPosition
  /*
   * CSCI-UA.0480-003: Homework 10
   * <Your Name>
   * 
   * Partner: <Your Partner's Name>
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

  /* Type Inference */
  
  // A helper function to check whether a JS type has a function type in it.
  // While this is completely given, this function is worth studying to see
  // how library functions are used.
  def hasFunctionTyp(t: Typ): Boolean = t match {
    case TFunction(_, _) => true
    case _ => false
  }
    
  def typeInfer(env: Map[String, (Mut, Typ)], e: Expr): Typ = {
    // Some shortcuts for convenience
    def typ(e1: Expr) = typeInfer(env, e1)
    def err[T](tgot: Typ, e1: Expr): T = throw StaticTypeError(tgot, e1)
    def locerr[T](e1: Expr): T = throw LocTypeError(e1)
    def checkTyp(texp: Typ, e1: Expr): Typ = {
      val tgot = typ(e1)
      if (texp == tgot) texp else err(tgot, e1)
    }
    
    e match {
      case Print(e1) => typ(e1); TUndefined
      case Num(_) => TNumber
      case Bool(_) => TBool
      case Undefined => TUndefined
      case Str(_) => TString
      case Var(x) => env(x)._2
      case Decl(mut, x, e1, e2) => 
        typeInfer(env + (x -> (mut, typ(e1))), e2)
      case UnOp(UMinus, e1) => typ(e1) match {
        case TNumber => TNumber
        case tgot => err(tgot, e1)
      }
      case UnOp(Not, e1) =>
        checkTyp(TBool, e1)
      case BinOp(bop, e1, e2) =>
        bop match {
          case Plus =>
            typ(e1) match {
              case TNumber => checkTyp(TNumber, e2)
              case TString => checkTyp(TString, e2)
              case tgot => err(tgot, e1)
            }
          case Minus | Times | Div => 
            checkTyp(TNumber, e1)
            checkTyp(TNumber, e2)
          case Eq | Ne => typ(e1) match {
            case t1 if !hasFunctionTyp(t1) => 
              checkTyp(t1, e2); TBool
            case tgot => err(tgot, e1)
          }
          case Lt | Le | Gt | Ge =>
            typ(e1) match {
              case TNumber => checkTyp(TNumber, e2)
              case TString => checkTyp(TString, e2)
              case tgot => err(tgot, e1)
            }
            TBool
          case And | Or =>
            checkTyp(TBool, e1)
            checkTyp(TBool, e2)
          case Seq =>
            typ(e1); typ(e2)
          case Assign =>
            e1 match {
              case Var(x) =>
                env(x) match {
                  case (MVar, t) => checkTyp(t, e2)
                  case _ => locerr(e1)
                }                  
              case _ => locerr(e1)
            }
        }
      case If(e1, e2, e3) =>
        checkTyp(TBool, e1)
        val t2 = typ(e2)
        checkTyp(t2, e3)
      case Function(p, xs, tann, e1) => {
        // Bind to env1 an environment that extends env with an appropriate binding if
        // the function is potentially recursive.
        val env1 = (p, tann) match {
          case (Some(f), Some(tret)) =>
            val tprime = TFunction(xs map (_._2), tret)
            env + (f -> (MConst, tprime))
          case (None, _) => env
          case _ => err(TUndefined, e1)
        }
        // Bind to env2 an environment that extends env1 with bindings for xs.
        val env2 = xs.foldLeft(env1){ 
          case (env2, (x, t)) => env2 + (x -> (MConst, t))
        }
        // Match on whether the return type is specified.
        tann match {
          case None => TFunction(xs map (_._2), typeInfer(env2, e1))
          case Some(tret) => 
            typeInfer(env2, e1) match {
              case tbody if tbody == tret => 
                TFunction(xs map (_._2), tret)
              case tbody => err(tbody, e1)
            }
        }
      }
      case Call(e1, es) => typ(e1) match {
        case TFunction(txs, tret) if (txs.length == es.length) => {
          (txs, es).zipped.foreach(checkTyp(_, _))
          tret
        }
        case tgot => err(tgot, e1)
      }
      case Addr(_) | UnOp(Deref, _) =>
        throw new IllegalArgumentException("Gremlins: Encountered unexpected expression %s.".format(e))
    }
  }
  
  /* JakartaScript Interpreter */
  
  def toNum(v: Val): Double = v match {
    case Num(n) => n
    case _ => throw StuckError(v)
  }
  
  def toBool(v: Val): Boolean = v match {
    case Bool(b) => b
    case _ => throw StuckError(v)
  }
  
  def toStr(v: Val): String = v match {
    case Str(s) => s
    case _ => throw StuckError(v)
  }
  
  /*
   * Helper function that implements the semantics of inequality
   * operators Lt, Le, Gt, and Ge on values.
   */
  def inequalityVal(bop: Bop, v1: Val, v2: Val): Boolean = {
  require(bop == Lt || bop == Le || bop == Gt || bop == Ge)
    (v1, v2) match {
      case (Str(s1), Str(s2)) =>
        (bop: @unchecked) match {
          case Lt => s1 < s2
          case Le => s1 <= s2
          case Gt => s1 > s2
          case Ge => s1 >= s2
        }
      case _ =>
        val (n1, n2) = (toNum(v1), toNum(v2))
        (bop: @unchecked) match {
          case Lt => n1 < n2
          case Le => n1 <= n2
          case Gt => n1 > n2
          case Ge => n1 >= n2
        }
    }
  }
    
  /* 
   * Substitutions e[er/x]
   */
  def subst(e: Expr, x: String, er: Expr): Expr = {
    require(closed(er))
    /* Simple helper that calls substitute on an expression
     * with the input value v and variable name x. */
    def substX(e: Expr): Expr = subst(e, x, er)
    /* Body */
    e match {
      case Num(_) | Bool(_) | Undefined | Str(_) | Addr(_) => e
      case Var(y) => if(x == y) er else e
      case Print(e1) => Print(substX(e1))
      case UnOp(uop, e1) => UnOp(uop, substX(e1))
      case BinOp(bop, e1, e2) => BinOp(bop, substX(e1), substX(e2))
      case If(b, e1, e2) => If(substX(b), substX(e1), substX(e2))
      case Call(e0, es) =>
        Call(substX(e0), es map substX)
      case Decl(mut, y, ed, eb) => 
        Decl(mut, y, substX(ed), if (x == y) eb else substX(eb))
      case Function(p, ys, tann, eb) => 
        if (p == Some(x) || (ys exists (_._1 == x))) e 
        else Function(p, ys, tann, substX(eb))
    }
  }

  
  /*
   * Big-step interpreter.
   */
  def eval(e: Expr): State[Mem, Val] = {
    require(closed(e), "eval called on non-closed expression:\n" + e.prettyJS)
    /* Some helper functions for convenience. */
    def eToNum(e: Expr): State[Mem, Double] =
      for ( v <- eval(e) ) yield toNum(v)
    def eToBool(e: Expr): State[Mem, Boolean] = 
       for ( v <- eval(e) ) yield toBool(v)
    
    e match {
      case v: Val => State insert v
      
      case Print(e) => 
        for {
          v <- eval(e)
        } yield {
          println(v.prettyVal); 
          Undefined
        }
        
      case UnOp(UMinus, e1) =>
        for {
          n1 <- eToNum(e1)
        } yield Num(- n1)
        
      case UnOp(Not, e1) =>
        ???
        
      case UnOp(Deref, a: Addr) =>
        ???
        
      case BinOp(Plus, e1, e2) =>
        ???
      
      case BinOp(bop@(Minus|Times|Div), e1, e2) =>
        ???
        
      case BinOp(And, e1, e2) => 
        for {
          b <- eToBool(e1)
          v <- if (b) eval(e2) else State.insert[Mem,Val](Bool(b))
        } yield v
      
      case BinOp(Or, e1, e2) =>
        ???
      
      case BinOp(Seq, e1, e2) => 
        ???
      
      case BinOp(Assign, UnOp(Deref, a: Addr), e2) =>
        ???
        
      case BinOp(bop@(Eq|Ne|Lt|Gt|Le|Ge), e1, e2) =>
        ???
              
      case If(e1, e2, e3) => 
        ???       
      
      case Decl(MConst, x, ed, eb) =>
        for {
          vd <- eval(ed)
          v <- eval(subst(eb, x, vd))
        } yield v
      
      case Decl(MVar, x, ed, eb) =>
        ???
        
      /** EvalCall */
      case Call(Function(None, Nil, _, eb), Nil) =>
        eval(eb)
        
      /** EvalCallConst */
      case Call(v0@Function(None, (x1, _) :: xs, _, eb), e1 :: es) =>
        ???
      
      /** EvalCallRec */
      case Call(e0, es) =>
        for {
          v0 <- eval(e0)
          v <- v0 match {
            case v0@Function(Some(x0), _, _, eb) =>
              val v0p = v0.copy(p=None, e=subst(eb, x0, v0))
              eval(Call(v0p, es))
            case _ => 
              eval(Call(v0, es))
          }
        } yield v
        
      case Var(_) | UnOp(Deref, _) | BinOp(_, _, _) => 
        throw StuckError(e) // this should never happen
    }
  }
   
  // Interface to run your interpreter from a string.  This is convenient
  // for unit testing.
  def evaluate(e: Expr): Val = eval(e)(Mem.empty)._2
  
  def evaluate(s: String): Val = eval(parse.fromString(s))(Mem.empty)._2
    
  def inferType(s: String): Typ = typeInfer(Map.empty, parse.fromString(s))
     
  
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
      val (_, v) = eval(expr)(Mem.empty)
      println(v.prettyVal)
    }
  }
}
