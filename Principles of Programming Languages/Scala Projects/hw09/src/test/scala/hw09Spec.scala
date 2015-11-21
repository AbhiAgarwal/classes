import hw09._
import org.scalatest._
import js.hw09.ast._


class Hw09Spec extends FlatSpec {
  // typeInfer
  
  "typeInfer" should "infer types of arithmetic expressions" in {
    val e1 = BinOp(Plus, Num(3), Num(4))
    assert(typeInfer(Map.empty, e1) === TNumber)
    val e1bad = BinOp(Plus, Num(3), Str("4"))
    intercept[StaticTypeError]{
      typeInfer(Map.empty, e1bad)
    }
    val e2 = BinOp(Plus, Str("3"), Str("4"))
    assert(typeInfer(Map.empty, e2) === TString)
    val e3 = BinOp(Times, Num(3), Num(5))
    assert(typeInfer(Map.empty, e3) === TNumber)
    val e3bad = BinOp(Times, Num(3), Undefined)
    intercept[StaticTypeError]{
      typeInfer(Map.empty, e3bad)
    }
  }
  
  "typeInfer" should "infer types of inequality expressions" in {
    val e1 = BinOp(Lt, Num(3), Num(4))
    assert(typeInfer(Map.empty, e1) === TBool)
    val e1bad = BinOp(Lt, Num(3), Str("4"))
    intercept[StaticTypeError]{
      typeInfer(Map.empty, e1bad)
    }
    val e2 = BinOp(Lt, Str("3"), Str("4"))
    assert(typeInfer(Map.empty, e2) === TBool)
  }
  
  "typeInfer" should "infer types of equality expressions" in {
    val e1 = BinOp(Eq, Num(3), Num(4))
    assert(typeInfer(Map.empty, e1) === TBool)
    val e1bad = BinOp(Eq, Num(3), Str("4"))
    intercept[StaticTypeError]{
      typeInfer(Map.empty, e1bad)
    }
    val e2 = BinOp(Ne, Str("3"), Str("4"))
    assert(typeInfer(Map.empty, e2) === TBool)
    val e3 = BinOp(Eq, Undefined, Print(Num(3)))
    assert(typeInfer(Map.empty, e3) === TBool)
    val e3bad = BinOp(Eq, Undefined, Num(3))
    intercept[StaticTypeError]{
      typeInfer(Map.empty, e3bad)
    } 
  }
  
  "typeInfer" should "reject equalities involving functions" in {
    val x = "x"
    val e1 = Function(None, List((x, (PConst, TNumber))), None, Var(x))
    val e2 = Num(2)
    intercept[StaticTypeError]{
      typeInfer(Map.empty, BinOp(Eq, e1, e2))
    }
    intercept[StaticTypeError]{
      typeInfer(Map.empty, BinOp(Eq, e2, e1))
    }
    intercept[StaticTypeError]{
      typeInfer(Map.empty, BinOp(Eq, e1, e1))
    }
  }

  "typeInfer" should "infer types of Boolean expressions" in {
    val e1 = BinOp(And, Bool(false), Bool(true))
    assert(typeInfer(Map.empty, e1) === TBool)
    val e1bad = BinOp(And, Bool(false), Num(3))
    intercept[StaticTypeError]{
      typeInfer(Map.empty, e1bad)
    }
  }

  "typeInfer" should "infer types of Seq expressions" in {
    val e1 = BinOp(Seq, Num(3), Bool(true))
    assert(typeInfer(Map.empty, e1) === TBool)
    val e2 = BinOp(Seq, Bool(true), Num(3))
    assert(typeInfer(Map.empty, e2) === TNumber)
    val e3 = BinOp(Seq, Undefined, Undefined)
    assert(typeInfer(Map.empty, e3) === TUndefined)
  }

  "typeInfer" should "infer types of Function expressions" in {
    val x = "x"
    val y = "y"
    val f = "f"
    val ebf = BinOp(Plus, Var(x), Num(3))
    val f1 = Function(None, List((x, (PConst, TNumber))), None, ebf)
    val tf = TFunction(List((PConst, TNumber)), TNumber)
    assert(typeInfer(Map.empty, f1) === tf)
    val f2 = Function(None, List((x, (PConst, TNumber))), Some(TNumber), ebf)
    assert(typeInfer(Map.empty, f2) === tf)
    val f3 = Function(Some(f), List((x, (PConst, TNumber))), Some(TNumber), ebf)
    assert(typeInfer(Map.empty, f3) === tf)
    val fbad1 = Function(None, List((x, (PConst, TNumber))), Some(TBool), ebf)
    intercept[StaticTypeError]{
      // wrong return type annotation
      typeInfer(Map.empty, fbad1)
    }
    val fbad2 = Function(None, List((x, (PConst, TBool))), None, ebf)
    intercept[StaticTypeError]{
      // wrong parameter type annotation
      typeInfer(Map.empty, fbad2)
    }
    val fbad3 = Function(Some(f), List((x, (PConst, TNumber))), None, ebf)
    intercept[StaticTypeError]{
      // missing return type annotation for (potentially) recursive function
      typeInfer(Map.empty, fbad3)
    }
    val ebg = If(Var(y), Var(x), ebf)
    val g = Function(None, List((x, (PConst, TNumber)), (y, (PConst, TBool))), None, ebg)
    val tg = TFunction(List((PConst, TNumber), (PConst, TBool)), TNumber)
    assert(typeInfer(Map.empty, g) === tg)
  }
  
  "typeInfer" should "infer types of Call expressions" in {
    val x = "x"
    val y = "y"
    val f = "f"
    val ebf = BinOp(Plus, Var(x), Num(3))
    val f1 = Function(None, List((x, (PConst, TNumber))), None, ebf)
    val e1 = Call(f1, List(Num(3)))
    assert(typeInfer(Map.empty, e1) === TNumber)
    val ebg = If(Var(y), Var(x), ebf)
    val g = Function(None, List((x, (PConst, TNumber)), (y, (PConst, TBool))), None, ebg)
    val e2 = Call(g, List(Num(3), Bool(true)))
    assert(typeInfer(Map.empty, e1) === TNumber)
    val ebf2 = Call(Var(f), List(Var(x)))
    val f2 = Function(Some(f), List((x, (PConst, TNumber))), Some(TNumber), ebf2)
    val e3 = Call(f2, List(Num(3)))
    assert(typeInfer(Map.empty, e3) === TNumber)
    val fbad1 = Call(f1, List(Num(3), Bool(false)))
    intercept[StaticTypeError]{
      // too many arguments in function call
      typeInfer(Map.empty, fbad1)
    }
    val fbad2 = Call(g, List(Num(3)))
    intercept[StaticTypeError]{
      // too few arguments in function call
      typeInfer(Map.empty, fbad2)
    }
    val fbad3 = Call(f1, List(Bool(true)))
    intercept[StaticTypeError]{
      // argument type mismatch
      typeInfer(Map.empty, fbad3)
    }
  }  
  
  "typInfer" should "reject programs that try to assign non-assignable expressions" in {
    intercept[LocTypeError] {
      inferType("const x = 3; x = x + 1")
    }
    intercept[LocTypeError] {
      inferType("""
        var x = 3; 
        const f = function (ref x: number) { return x; };
        f(x + 1)
        """)
    }
    intercept[LocTypeError] {
      inferType("""
        const x = 3; 
        const f = function (name y: number) {
            return (function (ref z: number) { return z; })(y);
          };
        f(3)
        """)
    }
  }
  
  "typeInfer" should "infer types of assignment expressions" in {
    val e1 = "var x = 3; x = x + 1"
    assert(inferType(e1) === TNumber)
    
    val e2 = "var x = 3; var y = 5; x = y = x"
    assert(inferType(e2) === TNumber)
    
    intercept[StaticTypeError] {
      inferType("var x = true; x = 3")
    }
  }
  
  "typInfer" should "infer types for different parameter passing modes" in {
    val t1 = TFunction(List((PVar, TNumber)), TNumber)
    val e1 = "function(var x: number) { x = x + 1; return x; }"
    assert(inferType(e1) === t1)
    
    val t2 = TFunction(List((PConst, TNumber), (PVar, TString)), TString)
    val e2 = "function(const x: number, var y: string) { return x === 3 ? y : y + '3'; }"
    assert(inferType(e2) === t2)
    
    val t3 = TFunction(List((PRef, TNumber), (PConst, TString)), t2)
    val e3 = "function(ref x: number, y: string) { return " + e2 + " }"
    assert(inferType(e3) === t3)
  }
  
  
  // eval
  
  "And" should "return true only if both expressions are true" in {
    val t = Bool(true)
    val f = Bool(false)
    assert(evaluate(BinOp(And,t,t)) === t)
    assert(evaluate(BinOp(And,t,f)) === f)
    assert(evaluate(BinOp(And,f,t)) === f)
    assert(evaluate(BinOp(And,f,f)) === f)
  } 
 
  "Or" should "return true if either or both expressions are true" in {
    val t = Bool(true)
    val f = Bool(false)
    assert(evaluate(BinOp(Or,t,t)) === t)
    assert(evaluate(BinOp(Or,f,t)) === t)
    assert(evaluate(BinOp(Or,t,f)) === t)
    assert(evaluate(BinOp(Or,f,f)) === f)
  }
  
  "Plus" should "add two number values and return a number" in {
    val e1 = Num(1)
    val e2 = Num(2)
    val e3 = evaluate(BinOp(Plus, e1, e2))
    assert(e3 === Num(3))
  }

  "Minus" should "subtract two number values and return a number" in {
    val e1 = Num(3)
    val e2 = Num(1)
    val e3 = evaluate(BinOp(Minus, e1, e2))
    assert(e3 === Num(2))
  }

  "Times" should "multiply two number values and return a number" in {
    val e1 = Num(3)
    val e2 = Num(2)
    val e3 = evaluate(BinOp(Times, e1, e2))
    assert(e3 === Num(6))
  }

  "Div" should "divide two number values and return a number" in {
    val e1 = Num(8)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Div, e1, e2))
    assert(e3 === Num(1.6))
  }

  "Eq" should "return true if two numerical values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Eq, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Eq" should "return false if two numerical values are not the same" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = evaluate(BinOp(Eq, e1, e2))
    assert(e3 === Bool(false))
  }
  
  "Ne" should "return true if two numerical values are different" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = evaluate(BinOp(Ne, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Ne" should "return false if two numerical values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Ne, e1, e2))
    assert(e3 === Bool(false))
  }
  
  "Lt" should "return true if the first expression is less than the second" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = evaluate(BinOp(Lt, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Lt" should "return false if the first expression is not strictly less than the second" in {
    val e1 = Num(7)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Lt, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Lt" should "return false if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Lt, e1, e2))
    assert(e3 === Bool(false))
  } 

  "Le" should "return true if the first expression is less than the second" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = evaluate(BinOp(Le, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Le" should "return false if the first expression is greater than the second" in {
    val e1 = Num(7)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Le, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Le" should "return true if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Le, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Gt" should "return true if the first expression is greater than the second" in {
    val e1 = Num(8)
    val e2 = Num(7)
    val e3 = evaluate(BinOp(Gt, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Gt" should "return false if the first expression is not strictly greater than the second" in {
    val e1 = Num(4)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Gt, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Gt" should "return false if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Gt, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Ge" should "return true if the first expression is greater than the second" in {
    val e1 = Num(8)
    val e2 = Num(7)
    val e3 = evaluate(BinOp(Ge, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Ge" should "return false if the first expression is less than the second" in {
    val e1 = Num(4)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Ge, e1, e2))
    assert(e3 === Bool(false))
  } 
     
  "Ge" should "return true if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = evaluate(BinOp(Ge, e1, e2))
    assert(e3 === Bool(true))
  }
  
  "ConstDecl" should "extend the environment with the first expression results bound to the identifier, and then eval the second expression" in {
    val e1 = Num(3)
    val e2 = BinOp(Plus, Var("x"), Num(1))
    val e3 = evaluate(Decl(MConst, "x", e1, e2)) 
    assert(e3 === Num(4))
  }   

  "If" should "eval the first expression if the conditional is true" in {
    val e1 = BinOp(Plus, Num(3), Num(2))
    val e2 = BinOp(Plus, Num(1), Num(1))
    val e3 = evaluate(If(Bool(true), e1, e2)) 
    assert(e3 === Num(5))
  } 
  
  "If" should "eval the second expression if the conditional is false" in {
    val e1 = BinOp(Plus, Num(3), Num(2))
    val e2 = BinOp(Plus, Num(1), Num(1))
    val e3 = evaluate(If(Bool(false), e1, e2)) 
    assert(e3 === Num(2))
  } 
  
  "Seq" should "execute the first expression, followed by the second, and should eval to the second expression" in {
    val e1 = BinOp(Plus, Num(3), Num(2))
    val e2 = BinOp(Plus, Num(1), Num(1))
    val e3 = evaluate(BinOp(Seq, e1, e2)) 
    assert(e3 === Num(2))
  } 
  
  "Neg" should "return the negation of a number value" in {
    val e1 = Num(5)
    val e2 = evaluate(UnOp(UMinus, e1))
    assert(e2 === Num(-5))
  } 
  
  "Not" should "return the compliment of a boolean value" in {
    val e1 = Bool(true)
    val e2 = Bool(false)
    val e3 = evaluate(UnOp(Not, e1))
    val e4 = evaluate(UnOp(Not, e2))
    assert(e3 === Bool(false))
    assert(e4 === Bool(true))
  }
  
  "Call" should "evaluate a function using big-step semantics" in {
    val f = "f"
    val x = "x"
    val e1 = Function(None, List((x, (PConst, TNumber))), None, BinOp(Plus, Var(x), Num(1)))
    val e2 = Num(2)
    val e3 = evaluate(Call(e1, List(e2)))
    assert(e3 === Num(3))
  }

  "Call" should "handle recursive functions using big-step semantics" in {
    val f = "f"
    val x = "x"
    val fbody = If(BinOp(Eq, Var(x), Num(0)), Var(x), BinOp(Plus, Var(x), Call(Var(f), List(BinOp(Minus, Var(x), Num(1))))))
    val e1 = Function(Some(f), List((x, (PConst, TNumber))), Some(TNumber), fbody)
    val e2 = Num(3)
    val e3 = evaluate(Call(e1, List(e2)))
    assert(e3 === Num(6))
  } 
  
  "Call" should "handle parameter passing modes" in {
    val e1 = """
      var x = 1;
      const f = function(name x: number, y: number) { return x + x + y; };
      f(x = x + 1, 1)
    """
    assert(evaluate(e1) === Num(6))
    
    val e2 = """
      var x = 1;
      const f = function(var x: number) { x = x + 1; return x; };
      f(x) + x
    """
    assert(evaluate(e2) === Num(3))
    
    val e3 = """
      var x = 1;
      const f = function(ref x: number) { x = x + 1; return x; };
      f(x) + x
    """
    assert(evaluate(e3) === Num(4))
  }
  
}

