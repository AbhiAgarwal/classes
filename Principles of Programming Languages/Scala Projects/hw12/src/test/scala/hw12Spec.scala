import hw12._
import org.scalatest._
import js.hw12.ast._


class hw12Spec extends FlatSpec {
  // subtype
  
  "subtype" should "handle subtype relationships between basic types" in {
    assert(subtype(TNumber, TAny))
    assert(subtype(TNumber, TNumber))
    assert(!subtype(TNumber, TBool))
    assert(!subtype(TAny, TBool))
  }
  
  it should "handle subtype relationships between object types" in {
    val f1 = "f" -> (MVar, TNumber)
    val f2 = "f" -> (MConst, TNumber)
    val f3 = "f" -> (MConst, TAny)
    val f4 = "f" -> (MVar, TAny)
    val g1 = "g" -> (MVar, TObj(Map(f1)))
    val g2 = "g" -> (MConst, TObj(Map()))
    val g3 = "g" -> (MVar, TObj(Map(f2)))
    assert(subtype(TObj(Map(f1)), TObj(Map())))
    assert(subtype(TObj(Map(f1)), TObj(Map(f2))))
    assert(subtype(TObj(Map(f1)), TObj(Map(f3))))
    assert(!subtype(TObj(Map(f1)), TObj(Map(f4))))
    assert(!subtype(TObj(Map()), TObj(Map(f1))))
    assert(!subtype(TObj(Map(f2)), TObj(Map(f1))))
    assert(!subtype(TObj(Map(f3)), TObj(Map(f1))))
    assert(subtype(TObj(Map(f4)), TObj(Map(f3))))
    assert(subtype(TObj(Map(f1,g1)), TObj(Map(g2, f3))))
    assert(subtype(TObj(Map(f1,g1)), TObj(Map(f3))))
    assert(!subtype(TObj(Map(f1,g1)), TObj(Map(g3, f3))))    
  }
  
  it should "handle subtype relationships between function types" in {
    val t1 = TFunction(List(TNumber), TNumber)
    val t2 = TFunction(List(TNumber), TAny)
    val t3 = TFunction(List(TAny), TNumber)
    val t4 = TFunction(List(TAny), TAny)
    val t5 = TFunction(List(TNumber, TBool), TNumber)
    val t6 = TFunction(List(TAny, TBool), TNumber)
    assert(subtype(t1, t2))
    assert(!subtype(t1, t3))
    assert(subtype(t3, t1))
    assert(subtype(t3, t2))
    assert(!subtype(t1, t4))
    assert(!subtype(t2, t4))
    assert(subtype(t3, t4))
    assert(!subtype(t1, t5))
    assert(subtype(t5, t5))
    assert(subtype(t6, t5))
    assert(!subtype(t5, t6))
  }

  // join and meet
  
  "join" should "compute join of basic types" in {
    assert(join(TNumber, TNumber) === TNumber)
    assert(join(TNumber, TAny) === TAny)
    assert(join(TAny, TString) === TAny)
    assert(join(TFunction(List(), TNumber), TString) === TAny)
    assert(join(TObj(Map()), TString) === TAny)
    assert(join(TObj(Map()), TFunction(List(), TNumber)) === TAny)
  }
  
  it should "compute join of object types" in {
    val f1 = "f" -> (MVar, TNumber)
    val f2 = "f" -> (MConst, TNumber)
    val f3 = "f" -> (MConst, TAny)
    val f4 = "f" -> (MVar, TAny)
    val g1 = "g" -> (MVar, TObj(Map(f1)))
    val g2 = "g" -> (MConst, TObj(Map()))
    val g3 = "g" -> (MVar, TObj(Map(f2)))
    val g4 = "g" -> (MConst, TObj(Map(f2)))
    assert(join(TObj(Map(f1)), TObj(Map())) === TObj(Map()))
    assert(join(TObj(Map()), TObj(Map(f1))) === TObj(Map()))
    assert(join(TObj(Map(f1)), TObj(Map(g1))) === TObj(Map()))
    assert(join(TObj(Map(f1)), TObj(Map(f2))) === TObj(Map(f2)))
    assert(join(TObj(Map(f1)), TObj(Map(g1, f2))) === TObj(Map(f2)))
    assert(join(TObj(Map(f1, g1)), TObj(Map(f2))) === TObj(Map(f2)))
    assert(join(TObj(Map(f1, g2)), TObj(Map(g1, f2))) === TObj(Map(f2, g2)))
    assert(join(TObj(Map(f1)), TObj(Map(f4))) === TObj(Map(f3)))
    assert(join(TObj(Map(f4)), TObj(Map(f1))) === TObj(Map(f3)))
    assert(join(TObj(Map(f2, g1)), TObj(Map(g3, f2))) === TObj(Map(f2, g4)))
  }

  it should "compute join of function types" in {
    val t1 = TFunction(List(), TNumber)
    val t2 = TFunction(List(), TBool)
    val t3 = TFunction(List(), TAny)
    val t4 = TFunction(List(TNumber), TNumber)
    val t5 = TFunction(List(TNumber), TBool)
    val t6 = TFunction(List(TNumber), TAny)
    val t7 = TFunction(List(TBool), TNumber)
    val t8 = TFunction(List(TAny), TNumber)
    val t9 = TFunction(List(TAny), TAny)
    assert(join(t1, t1) === t1)
    assert(join(t1, t2) === t3)
    assert(join(t1, t2) === t3)
    assert(join(t1, t4) === TAny)
    assert(join(t4, t5) === t6)
    assert(join(t4, t7) === TAny)
    assert(join(t5, t7) === TAny)
    assert(join(t4, t9) === t6)
  }

  "meet" should "compute meet of object types" in {
    val f1 = "f" -> (MVar, TNumber)
    val f2 = "f" -> (MConst, TNumber)
    val f3 = "f" -> (MConst, TAny)
    val f4 = "f" -> (MConst, TBool)
    val f5 = "f" -> (MVar, TAny)
    val g1 = "g" -> (MVar, TObj(Map(f1)))
    val g2 = "g" -> (MConst, TObj(Map()))
    val g3 = "g" -> (MVar, TObj(Map(f2)))
    val h1 = "h" -> (MVar, TBool)
    val g4 = "g" -> (MConst, TObj(Map(h1)))
    val g5 = "g" -> (MVar, TObj(Map(f2, h1)))
    
    assert(meet(TObj(Map()), TObj(Map(f1))) === Some(TObj(Map(f1))))
    assert(meet(TObj(Map(f1)), TObj(Map())) === Some(TObj(Map(f1))))
    assert(meet(TObj(Map(f1)), TObj(Map(g1))) === Some(TObj(Map(f1, g1))))
    assert(meet(TObj(Map(f1)), TObj(Map(f2))) === Some(TObj(Map(f1))))
    assert(meet(TObj(Map(f2)), TObj(Map(f3))) === Some(TObj(Map(f2))))
    assert(meet(TObj(Map(f1)), TObj(Map(f4))) === None)
    assert(meet(TObj(Map(f1)), TObj(Map(f5))) === None)
    assert(meet(TObj(Map(g1)), TObj(Map(g2))) === Some(TObj(Map(g1))))
    assert(meet(TObj(Map(g3)), TObj(Map(g4))) === Some(TObj(Map(g5))))
    assert(meet(TObj(Map(g1)), TObj(Map(g3))) === None)
    assert(meet(TObj(Map(f1, g3)), TObj(Map(g4, f2))) === Some(TObj(Map(f1, g5))))
  }

  it should "compute meet of function types" in {
    val t1 = TFunction(List(), TNumber)
    val t2 = TFunction(List(), TBool)
    val t3 = TFunction(List(), TAny)
    val t4 = TFunction(List(TNumber), TNumber)
    val t5 = TFunction(List(TNumber), TBool)
    val t6 = TFunction(List(TNumber), TAny)
    val t7 = TFunction(List(TBool), TNumber)
    val t8 = TFunction(List(TAny), TNumber)
    val t9 = TFunction(List(TAny), TAny)
    assert(meet(t1, t1) === Some(t1))
    assert(meet(t1, t2) === None)
    assert(meet(t1, t2) === None)
    assert(meet(t1, t3) === Some(t1))
    assert(meet(t3, t1) === Some(t1))
    assert(meet(t4, t5) === None)
    assert(meet(t4, t7) === Some(TFunction(List(TAny), TNumber)))
    assert(meet(t5, t6) === Some(t5))
    assert(meet(t8, t9) === Some(t8))
  }

  
  // typeInfer
  
  "typeInfer" should "handle object literals" in {
    val e1 = "{var f: 0, const g: true}"
    assert(inferType(e1) === TObj(Map("f" -> (MVar, TNumber), "g" -> (MConst, TBool))))
    val e2 = "const x = {}; x"
    assert(inferType(e2) === TObj(Map.empty))
  }
  
  it should "handle field dereference expressions" in {
    val e1 = "{f: 0}.f"
    assert(inferType(e1) === TNumber)
    val e2 = "{g: 0}.f"
    intercept[StaticTypeError]{
      inferType(e2)
    }
  }
  
  it should "handle field assignment expressions" in {
    val e1 = "{f: 0}.f = 2"
    assert(inferType(e1) === TNumber)
    val e2 = "{const f: 0}.f = 2"
    intercept[LocTypeError]{
      inferType(e2)
    }
    val e3 = "{f: 0}.f = true"
    intercept[StaticTypeError]{
      inferType(e3)
    }
    val e4 = "{f: 0}.g = true"
    intercept[StaticTypeError]{
      inferType(e4)
    }
  }
  
  it should "handle subtyping in call expressions" in {
    val e1 = """
      const x = { f: 1, g: true };
      const fun = function(x: {const f: any}) {
          return x;
        };
      fun(x).f
      """
    assert(inferType(e1) === TAny)
    val e2 = """
      const x = { f: 1 };
      const fun = function(x: {const f: number, g: bool}) {
          return x;
        };
      fun(x).f
      """
    intercept[StaticTypeError]{
      inferType(e2)
    }
  }

  
  it should "handle subtyping in return type annotations" in {
    val e1 = """
      function(): {const f: number} {
          return {f: 1, g: true};
      }
      """
    assert(inferType(e1) === TFunction(Nil, TObj(Map("f" -> (MConst, TNumber)))))
    val e2 = """
      function fun(): {const f: number} {
          return {f: 1, g: true};
      }
      """
    assert(inferType(e2) === TFunction(Nil, TObj(Map("f" -> (MConst, TNumber)))))
    val e3 = """
      function fun(): {const f: number, var g: bool} {
          return {f: 1, const g: true};
      }
      """
    intercept[StaticTypeError](inferType(e3))
  }
  
  it should "handle subtyping in variable assignments" in {
    val e1 = """
      var x = {const f: {} };
      x = {const f: { h: 1 } };
      """
    assert(inferType(e1) === TObj(Map("f" -> (MConst, TObj(Map("h" -> (MVar, TNumber)))))))
    val e2 = """
      var x = {const f: { h: 1 } };
      x = {const f: {} };
      """
    intercept[StaticTypeError]{
      inferType(e2)
    }
  }

  it should "handle subtyping in field assignments" in {
    val e1 = """
      const x = {f: {const g: 1}};
      x.f = {g: 2, const h: true};
      x.f.g
      """
    assert(inferType(e1) === TNumber)
    val e2 = """
      const x = {f: {const g: 1}};
      x.f = {g: 2, const h: true};
      x.f.h
      """
    intercept[StaticTypeError]{
      inferType(e2)
    }
    val e3 = """
      const x = {f: {const g: 1}};
      x.f = {};
      """
    intercept[StaticTypeError]{
      inferType(e3)
    }
  }
  
  it should "handle subtyping in equalities" in {
    assert(inferType("{f: 1} === {g: true}") === TBool)
    
    intercept[StaticTypeError]{
      inferType("3 === true")
    }
    
    intercept[StaticTypeError]{ 
      val e = """
        const f = function(x: number) { return x; };
        f === f;
        """
      inferType(e)
    }
  }
  
  it should "handle subtyping in conditionals" in {
    val e1 = """
      true ? { f: 1 } : { f: true }
      """
    assert(inferType(e1) === TObj(Map("f" -> (MConst, TAny))))
    val e2 = """
      (true ? { f: 1 } : { f: 1, g: undefined }).f
      """
    assert(inferType(e2) === TNumber)
    val e3 = """
      function(b: bool) {
        const fun1 = function(x: {f: number}) { return x.f; };
        const fun2 = function(x: {g: bool}) { return x.g; };
        return (b ? fun1 : fun2)({f: 1, g: true, h: {}})
      }
      """
    assert(inferType(e3) === TFunction(List(TBool), TAny))
    val e4 = """
      function(b: bool) {
        const fun1 = function(x: {f: number}) { return x.f; };
        const fun2 = function(x: {g: bool}) { return x.g; };
        return (b ? fun1 : fun2)({f: 1})
      }
      """
    intercept[StaticTypeError]{
      inferType(e4)
    }
  }
  
  // eval
  
  "eval" should "handle field dereference expressions" in {
    val e1 = "{const f: 0}.f"
    assert(evaluate(e1) === Num(0))
  }
  
  it should "handle field assignment expressions" in {
    val e1 = """
      const x = {f: 0, g: true};
      x.f = 2;
      x.f + 1
      """
    assert(evaluate(e1) === Num(3))
    val e2 = """
      const x = {f: 1 + 1, g: true};
      x.g = false;
      x.g ? x.f + 1 : x.f - 1
      """
    assert(evaluate(e2) === Num(1))
    val e3 = """
      const x = {f: {g: true}};
      x.f.g = false;
      x.f.g ? 1 : 2
      """
    assert(evaluate(e3) === Num(2))
  }
  
  it should "handle aliasing" in {
    val e1 = """
      const x = {f: 0};
      const y = x;
      y.f = 1;
      x.f
      """
    assert(evaluate(e1) === Num(1))
  }
  
}

