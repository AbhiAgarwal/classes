object hw12 extends js.util.JsApp {
  import js.hw12.ast._
  import js.hw12._
  import js.util.State
  import scala.util.parsing.input.NoPosition
  /*
   * CSCI-UA.0480-003: Homework 12
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

  /* Type Inference */
    
  /*
   * The subtype relation, t1 <: t2
   */  
  def subtype(t1: Typ, t2: Typ): Boolean = (t1, t2) match {
    /** SubObj */
    case (TObj(fts1), TObj(fts2)) =>
      fts2 forall { 
        case (gj, (mutj, tj)) =>
          fts1.get(gj) match {
            case Some((_, t3)) => {
              subtype(t3, t1)
            }
            case None => false
          }
      }
      
    /** SubFun */
    case (TFunction (ts1, tret1), TFunction (ts2, tret2)) =>
      if (!subtype(tret1, tret2)) false
      else {
        (ts1, ts2).zipped forall {
          case (a, b) => subtype(a, b)
        }
      }
      
    /** SubRefl, SubAny */
    case (t1, t2) =>
      t1 == t2 || t2 == TAny
  }
  
  /*
   * The join operator
   */  
  def join(t1: Typ, t2: Typ): Typ = (t1, t2) match {
    /** JoinObj* */
    case (TObj(fts1), TObj(fts2)) =>
      // fts should be the field map of the join of t1 and t2
      val fts = 
        fts1.foldLeft((Map.empty[Fld, (Mut, Typ)])) {
          case (fts, (h, (mut_h, t_h))) =>
            fts2.get(h) match {
              /** JoinObjNO, JoinObjVar, JoinObjMut_!= */
              case None => fts
              case Some((mutp_h, tp_h)) => ??? /*{
                for (ft <- fts) yield {

                }
              }*/
            }
        }
      TObj(fts)
      
    /** JoinFunMeet, JoinFunAny */
    case (TFunction (ts1, tret1), TFunction (ts2, tret2)) if ts1.size == ts2.size =>
      // tsopt is an Option value. It should contain the list of parameter types for the join 
      // of t1 and t2 if the join is a function type (rule JoinFunMeet). 
      // If the join is 'any', then tsopt should be None (rule JoinFunAny).
      val tsopt = (ts1, ts2).zipped.foldRight(Some(Nil): Option[List[Typ]]) {
        case ((t1, t2), tsopt) =>
          ???
      }
      tsopt map(???) getOrElse TAny
    
    /** JoinBasic_=, JoinAny_1, JoinAny_2, JoinObjFun, JoinFunObj */
    case _ => if (t1 == t2) t1 else TAny
  }
  
  /*
   * The meet operator
   */  
  def meet(t1: Typ, t2: Typ): Option[Typ] = (t1, t2) match {
    case (TObj(fts1), TObj(fts2)) =>
      // fts_common_opt is an Option value. It should contain the field map with the common
      // fields of fts1 and fts2 for the resulting meet of t1 and t2 (if the meet exists).
      // If merging the common fields fails (i.e., the meet does not exist), then 
      // fts_common_opt should be None.
      val fts_common_opt = 
        fts1.foldLeft((Some(Map.empty): Option[Map[Fld, (Mut, Typ)]])) {
          case (fts_common_opt, (h, (mut_h, t_h))) =>
            fts2.get(h) match {
               /** MeetObjNO, MeetObjVar_=, MeetObjMut_!=, MeetObjConst */
              case None => ???
              case Some((mutp_h, tp_h)) => ???
            }
        }
      for {
        fts_common <- fts_common_opt
      } yield TObj(fts2 ++ fts_common) // rule MeetObjEmp is implicit here
    
    /** MeetFunJoin */
    case (TFunction (ts1, tret1), TFunction (ts2, tret2)) if ts1.size == ts2.size =>
      ???
      
    /** MeetAny_1 */
    case (t1, TAny) => Some(t1)
    
    /** MeetAny_2 */
    case (TAny, t2) => Some(t2)
    
    /** MeetBasic_= */
    case (t1, t2) => 
      if (t1 == t2) Some(t1) else None
  }
  
  // A helper function to check whether a JS type has a function type in it.
  def hasFunctionTyp(t: Typ): Boolean = t match {
    case TFunction(_, _) => true
    case _ => false
  }

  /*
   * Type inference algorithm
   */
  def typeInfer(env: Map[String, (Mut, Typ)], e: Expr): Typ = {
    // Some shortcuts for convenience
    def typ(e1: Expr) = typeInfer(env, e1)
    def err[T](tgot: Typ, e1: Expr): T = throw StaticTypeError(tgot, e1)
    def locerr[T](e1: Expr): T = throw LocTypeError(e1)
    def checkTyp(texp: Typ, e1: Expr): Typ = {
      val tgot = typ(e1)
      if (texp == tgot) texp else err(tgot, e1)
    }
    def checkSubtyp(texp: Typ, e1: Expr): Typ = {
      val tgot = typ(e1)
      if (subtype(tgot, texp)) tgot else err(tgot, e1)
    }
    
    /* The actual implementation of the type inference rules: */
    e match {
      /** TypePrint */
      case Print(e1) => typ(e1); TUndefined
      
      /** TypeNum */
      case Num(_) => TNumber
      
      /** TypeBool */
      case Bool(_) => TBool
      
      /** TypeUndefined */
      case Undefined => TUndefined
      
      /** TypeStr */
      case Str(_) => TString
      
      /** TypeVar */
      case Var(x) => env(x)._2
      
      /** TypeDecl */
      case Decl(mut, x, e1, e2) => 
        typeInfer(env + (x -> (mut, typ(e1))), e2)
      
      /** TypeUMinus */
      case UnOp(UMinus, e1) => typ(e1) match {
        case TNumber => TNumber
        case tgot => err(tgot, e1)
      }
      
      /** TypeNot */
      case UnOp(Not, e1) =>
        checkTyp(TBool, e1)
      
      /** TypeDerefFld */
      case UnOp(FldDeref(f), e) => typ(e) match {
        case TObj(tfs) if (tfs contains f) => tfs(f)._2
        case tgot => err(tgot, e)
      } 
        
      case BinOp(bop, e1, e2) =>
        bop match {
          /** TypePlusStr, TypeArith(+) */
          case Plus =>
            typ(e1) match {
              case TNumber => checkTyp(TNumber, e2)
              case TString => checkTyp(TString, e2)
              case tgot => err(tgot, e1)
            }
          /** TypeArith (-,*,/) */
          case Minus | Times | Div => 
            checkTyp(TNumber, e1)
            checkTyp(TNumber, e2)
            
          /** TypeEqual */
          case Eq | Ne => 
            ???
          
          /** TypeInequal */
          case Lt | Le | Gt | Ge =>
            typ(e1) match {
              case TNumber => checkTyp(TNumber, e2)
              case TString => checkTyp(TString, e2)
              case tgot => err(tgot, e1)
            }
            TBool
            
          /** TypeAndOr */
          case And | Or =>
            checkTyp(TBool, e1)
            checkTyp(TBool, e2)
            
          /** TypeSeq */
          case Seq =>
            typ(e1); typ(e2)
            
          case Assign =>
            e1 match {
              /** TypeAssignVar */
              case Var(x) =>
                ???
                
              /** TypeAssignFld */
              case UnOp(FldDeref(f), e11) =>
                ???
                
              case _ => locerr(e1)
            }
        }
        
      /** TypeIf */
      case If(e1, e2, e3) =>
        ???
        
      /** TypeFun, TypeFunAnn, TypeFunRec */  
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
              case tbody if ??? => 
                TFunction(xs map (_._2), tret)
              case tbody => err(tbody, e1)
            }
        }
      }
      
      /** TypeCall */
      case Call(e1, es) => typ(e1) match {
        case TFunction(txs, tret) if (txs.length == es.length) => {
          (txs, es).zipped.foreach(???)
          tret
        }
        case tgot => err(tgot, e1)
      }
      
      /** TypeObj */
      case Obj(fs) => TObj(fs mapValues { case (mut, e) => (mut, typ(e)) })
      
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

  def toAddr(v: Val): Addr = v match {
    case a: Addr => a
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
   * Substitutions e[er/x] (no changes compared to Homework 11)
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
      case Obj(fes) => 
        Obj(fes mapValues { case (m, e) => (m, substX(e)) })
    }
  }

  
  /*
   * Big-step interpreter (no changes compared to Homework 11)
   */
  def eval(e: Expr): State[Mem, Val] = {
    require(closed(e), "eval called on non-closed expression:\n" + e.prettyJS)

    /* Some helper functions for convenience: */
    def eToNum(e: Expr): State[Mem, Double] =
      for ( v <- eval(e) ) yield toNum(v)
    def eToBool(e: Expr): State[Mem, Boolean] = 
       for ( v <- eval(e) ) yield toBool(v)
    def eToAddr(e: Expr): State[Mem, Addr] = 
       for ( v <- eval(e) ) yield toAddr(v)
    def readVal(a: Addr): State[Mem, Val] = 
      State read { m: Mem =>
        m(a) match {
          case v: Val => v
          case _ => throw StuckError(e)
        }
      }
    def readObj(a: Addr): State[Mem, Map[String, Val]] = 
      State read { m: Mem =>
        m(a) match {
          case ObjVal(fs) => fs
          case _ => throw StuckError(e)
        }
      }
    
    /* The actual implementation of the evaluation rules: */
    e match {
      /** EvalVal */
      case v: Val => State insert v
      
      /** EvalPrint */
      case Print(e) => 
        for {
          v <- eval(e)
        } yield {
          println(v.prettyVal); 
          Undefined
        }
        
      /** EvalUMinus */
      case UnOp(UMinus, e1) =>
        for {
          n1 <- eToNum(e1)
        } yield Num(- n1)
        
      /** EvalNot */
      case UnOp(Not, e1) =>
        for {
          b <- eToBool(e1)
        } yield Bool(! b)
 
      /** EvalDerefVar */
      case UnOp(Deref, a: Addr) =>
        for { v <- readVal(a) } yield v
 
      /** EvalDerefFld */
      case UnOp(FldDeref(f), e) =>
        ???
      
      /** EvalPlusNum, EvalPlusStr */
      case BinOp(Plus, e1, e2) =>
        for {
          v1 <- eval(e1)
          v2 <- eval(e2)
        } yield (v1, v2) match {
          case (Str(s1), v2) => Str(s1 + toStr(v2))
          case (v1, Str(s2)) => Str(toStr(v1) + s2)
          case (v1, v2) => Num(toNum(v1) + toNum(v2))
        }
      
      /** EvalArith */
      case BinOp(bop@(Minus|Times|Div), e1, e2) =>
        for {
          n1 <- eToNum(e1) 
          n2 <- eToNum(e2)
        } yield (bop: @unchecked) match {
          case Minus => Num(n1 - n2)
          case Times => Num(n1 * n2)
          case Div => Num(n1 / n2)
        }
        
      /** EvalAndTrue, EvalAndFalse */
      case BinOp(And, e1, e2) => 
        for {
          b <- eToBool(e1)
          v <- if (b) eval(e2) else State insert[Mem,Val] (Bool(b))
        } yield v
      
      /** EvalOrFalse, EvalOrTrue */
      case BinOp(Or, e1, e2) =>
        for {
          b <- eToBool(e1)
          v <- if (b) State insert[Mem,Val] (Bool(b)) else eval(e2)
        } yield v
      
      /** EvalSeq */
      case BinOp(Seq, e1, e2) => 
        for {
          _ <- eval(e1)
          v2 <- eval(e2)
        } yield v2
      
      /** EvalAssignVar */
      case BinOp(Assign, UnOp(Deref, a: Addr), e2) =>
        for {
          v2 <- eval(e2)
          _ <- State write { m: Mem => m + (a -> v2) }
        } yield v2
      
      /** EvalAssignFld */
      case BinOp(Assign, UnOp(FldDeref(f), e1), e2) =>
        ???
      
      /** EvalInequalNum, EvalInequalStr */
      case BinOp(bop@(Eq|Ne|Lt|Gt|Le|Ge), e1, e2) =>
        for {
          v1 <- eval(e1)
          v2 <- eval(e2)
        } yield (bop: @unchecked) match {
          case Eq => Bool(v1 == v2)
          case Ne => Bool(v1 != v2)
          case Le|Ge|Lt|Gt => Bool(inequalityVal(bop, v1, v2))
        }
              
      /** EvalIfThen, EvalIfElse */
      case If(e1, e2, e3) => 
        for {
          b <- eToBool(e1)
          v <- if (b) eval(e2) else eval(e3)
        } yield v       
      
      /** EvalConstDecl */
      case Decl(MConst, x, ed, eb) =>
        for {
          vd <- eval(ed)
          v <- eval(subst(eb, x, vd))
        } yield v
      
      /** EvalVarDecl */
      case Decl(MVar, x, ed, eb) =>
        for {
          vd <- eval(ed)
          a <- Mem.alloc(vd)
          v <- eval(subst(eb, x, UnOp(Deref, a)))
        } yield v
        
      /** EvalCall */
      case Call(Function(None, Nil, _, eb), Nil) =>
        eval(eb)
        
      /** EvalCallConst */
      case Call(v0@Function(None, (x1, _) :: xs, _, eb), e1 :: es) =>
        for {
          v1 <- eval(e1)
          v <- eval(Call(v0.copy(xs=xs, e=subst(eb, x1, v1)), es))
        } yield v
      
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
        
      /** EvalObj */
      case Obj(fes) =>
        val state0 = State.insert[Mem, Map[String, Val]](Map.empty)
        fes.foldLeft(state0) {
          case (state, (fi, (_, ei))) =>
            ???
        } flatMap {
          fvs => ???
        }
        
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
    
    handle(fail()) {
      val t = typeInfer(Map.empty, expr)
    }
    
    handle() {
      val (_, v) = eval(expr)(Mem.empty)
      println(v.prettyVal)
    }
  }
}
