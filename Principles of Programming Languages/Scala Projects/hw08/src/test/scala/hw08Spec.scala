import hw08._
import org.scalatest._
import js.hw08.ast._


class Hw08Spec extends FlatSpec {  
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
    val e1 = Function(None, List((x, TNumber)), None, Var(x))
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
    val f1 = Function(None, List((x, TNumber)), None, ebf)
    val tf = TFunction(List(TNumber), TNumber)
    assert(typeInfer(Map.empty, f1) === tf)
    val f2 = Function(None, List((x, TNumber)), Some(TNumber), ebf)
    assert(typeInfer(Map.empty, f2) === tf)
    val f3 = Function(Some(f), List((x, TNumber)), Some(TNumber), ebf)
    assert(typeInfer(Map.empty, f3) === tf)
    val fbad1 = Function(None, List((x, TNumber)), Some(TBool), ebf)
    intercept[StaticTypeError]{
      // wrong return type annotation
      typeInfer(Map.empty, fbad1)
    }
    val fbad2 = Function(None, List((x, TBool)), None, ebf)
    intercept[StaticTypeError]{
      // wrong parameter type annotation
      typeInfer(Map.empty, fbad2)
    }
    val fbad3 = Function(Some(f), List((x, TNumber)), None, ebf)
    intercept[StaticTypeError]{
      // missing return type annotation for (potentially) recursive function
      typeInfer(Map.empty, fbad3)
    }
    val ebg = If(Var(y), Var(x), ebf)
    val g = Function(None, List((x, TNumber), (y, TBool)), None, ebg)
    val tg = TFunction(List(TNumber, TBool), TNumber)
    assert(typeInfer(Map.empty, g) === tg)
  }
  
  "typeInfer" should "infer types of Call expressions" in {
    val x = "x"
    val y = "y"
    val f = "f"
    val ebf = BinOp(Plus, Var(x), Num(3))
    val f1 = Function(None, List((x, TNumber)), None, ebf)
    val e1 = Call(f1, List(Num(3)))
    assert(typeInfer(Map.empty, e1) === TNumber)
    val ebg = If(Var(y), Var(x), ebf)
    val g = Function(None, List((x, TNumber), (y, TBool)), None, ebg)
    val e2 = Call(g, List(Num(3), Bool(true)))
    assert(typeInfer(Map.empty, e1) === TNumber)
    val ebf2 = Call(Var(f), List(Var(x)))
    val f2 = Function(Some(f), List((x, TNumber)), Some(TNumber), ebf2)
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
}

