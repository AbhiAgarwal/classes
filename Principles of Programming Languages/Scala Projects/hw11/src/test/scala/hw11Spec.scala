import hw11._
import org.scalatest._
import js.hw11.ast._


class Hw11Spec extends FlatSpec {
  // typeInfer
  
  "typeInfer" should "handle object literals" in {
    val e1 = "{var f: 0, const g: true}"
    assert(inferType(e1) === TObj(Map("f" -> (MVar, TNumber), "g" -> (MConst, TBool))))
    val e2 = "const x = {}; x"
    assert(inferType(e2) === TObj(Map.empty))
  }
  
  "typeInfer" should "handle field dereference expressions" in {
    val e1 = "{f: 0}.f"
    assert(inferType(e1) === TNumber)
    val e2 = "{g: 0}.f"
    intercept[StaticTypeError]{
      inferType(e2)
    }
  }
  
  "typeInfer" should "handle field assignment expressions" in {
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
  
  // eval
  
  "eval" should "handle field dereference expressions" in {
    val e1 = "{const f: 0}.f"
    assert(evaluate(e1) === Num(0))
  }
  
  "eval" should "handle field assignment expressions" in {
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
  
  "eval" should "handle aliasing" in {
    val e1 = """
      const x = {f: 0};
      const y = x;
      y.f = 1;
      x.f
      """
    assert(evaluate(e1) === Num(1))
  }
  
}

