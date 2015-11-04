import hw07._
import org.scalatest._
import js.hw07.ast._


class Hw07Spec extends FlatSpec {
  "compressRec/compressFold" should "compress List(1, 2, 2, 3, 3, 3)" in {
    val l1 = List(1, 2, 2, 3, 3, 3)
    val gold1 = List(1, 2, 3)
    assertResult(gold1) { compressRec(l1) }
    assertResult(gold1) { compressFold(l1) }
  } 
  
  "mapFirst" should "map the first element where f returns Some" in {
     val l1 = List(1, 2, -3, 4, -5)
     val gold1 = List(1, 2, 3, 4, -5)
     assertResult(gold1) {
       mapFirst { (i: Int) => if (i < 0) Some(-i) else None } (l1)
     }
  }
  
  "foldLeft" should "enable implementing treeFromList and sum" in {
    assertResult(6){
      sum(treeFromList(List(1, 2, 3)))
    }
  }

  "strictlyOrdered" should "check strict ordering of a binary search tree" in {
    assert(!strictlyOrdered(treeFromList(List(1,1,2))))
    assert(strictlyOrdered(treeFromList(List(1,2))))
  } 
  
  // subst
  
  "subst" should "handle call expressions correctly" in {
    val x = "x"
    val y = "y"
    val z = "z"
    val f = "f"
    val e1 = BinOp(Plus, Var(x), Var(y))
    val e2 = BinOp(Plus, Num(3), Var(y))
    val e3 = BinOp(Plus, Var(x), Var(x))
    val e4 = BinOp(Plus, Num(3), Num(3))
    val e5 = Function(None, List(x), e3)
    assert(subst(Call(Var(f), Nil), x, Num(3)) === Call(Var(f), Nil))
    assert(subst(Call(Var(f), List(e1)), x, Num(3)) === Call(Var(f), List(e2)))
    assert(subst(Call(Var(f), List(e1, e3)), x, Num(3)) === Call(Var(f), List(e2, e4)))
    assert(subst(Call(Var(f), Nil), f, e5) === Call(e5, Nil))
  }
  
  "subst" should "handle function expressions correctly" in {
    val x = "x"
    val y = "y"
    val z = "z"
    val e1 = BinOp(Plus, Var(x), Var(y))
    val e2 = BinOp(Plus, Num(3), Var(y))
    assert(subst(Function(None, Nil, e1), x, Num(3)) === Function(None, Nil, e2))
    assert(subst(Function(None, List(y, z), e1), x, Num(3)) === Function(None, List(y, z), e2))
    assert(subst(Function(None, List(y, x), e1), x, Num(3)) === Function(None, List(y, x), e1))
    assert(subst(Function(Some(z), List(y), e1), x, Num(3)) === Function(Some(z), List(y), e2))
    assert(subst(Function(Some(x), List(y, z), e1), x, Num(3)) === Function(Some(x), List(y, z), e1))
  }
  
  // eval
  
  "Call" should "eval a function using big-step semantics" in {
    val f = "f"
    val x = "x"
    val e1 = Function(None, List(x), BinOp(Plus, Var(x), Num(1)))
    val e2 = Num(2)
    val e3 = eval(Call(e1, List(e2)))
    assert(e3 === Num(3))
  }

  "Call" should "handle recursive functions using big-step semantics" in {
    val f = "f"
    val x = "x"
    val fbody = If(BinOp(Eq, Var(x), Num(0)), Var(x), BinOp(Plus, Var(x), Call(Var(f), List(BinOp(Minus, Var(x), Num(1))))))
    val e1 = Function(Some(f), List(x), fbody)
    val e2 = Num(3)
    val e3 = eval(Call(e1, List(e2)))
    assert(e3 === Num(6))
  } 
  
  "Call" should "handle function calls with missing arguments" in {
    val x = "x"
    val y = "y"
    val e1 = Function(None, List(x, y), Var(y))
    val e2 = eval(Call(e1, List(Num(1))))
    assert (e2 === Undefined)
  }
  
  "Call" should "handle function calls with too many arguments" in {
    val x = "x"
    val e1 = Function(None, List(x), Var(x))
    val e2 = eval(Call(e1, List(Num(1), BinOp(Plus, Num(2), Num(3)))))
    assert (e2 === Num(1))
  }
}

