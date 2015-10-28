import hw06._
import org.scalatest._
import js.hw06.ast._

class SmallAndOrSpec extends FlatSpec {

  "And" should "return true only if both expressions are true" in {
    val t = Bool(true)
    val f = Bool(false)
    assert(iterateStep(BinOp(And,t,t)) === t)
    assert(iterateStep(BinOp(And,t,f)) === f)
    assert(iterateStep(BinOp(And,f,t)) === f)
    assert(iterateStep(BinOp(And,f,f)) === f)
  } 
 
  "And" should "return non-intuitive results from differing types" in {
    val e1 = Num(0)
    val e2 = Bool(true)
    val e3 = iterateStep(BinOp(And, e1, e2))
    assert(e3 === Num(0))
  }
 
  "Or" should "return true if either or both expressions are true" in {
    val t = Bool(true)
    val f = Bool(false)
    assert(iterateStep(BinOp(Or,t,t)) === t)
    assert(iterateStep(BinOp(Or,f,t)) === t)
    assert(iterateStep(BinOp(Or,t,f)) === t)
    assert(iterateStep(BinOp(Or,f,f)) === f)
  }

  "Or" should "return non-intuitive results from differing types" in {
    val e1 = Num(5)
    val e2 = Bool(false)
    val e3 = iterateStep(BinOp(Or, e1, e2))
    assert(e3 === Num(5))
  }
}

class SmallArithmeticSpec extends FlatSpec {

  "Plus" should "add two number values and return a number" in {
    val e1 = Num(1)
    val e2 = Num(2)
    val e3 = iterateStep(BinOp(Plus, e1, e2))
    assert(e3 === Num(3))
  }

  "Minus" should "subtract two number values and return a number" in {
    val e1 = Num(3)
    val e2 = Num(1)
    val e3 = iterateStep(BinOp(Minus, e1, e2))
    assert(e3 === Num(2))
  }

  "Times" should "multiply two number values and return a number" in {
    val e1 = Num(3)
    val e2 = Num(2)
    val e3 = iterateStep(BinOp(Times, e1, e2))
    assert(e3 === Num(6))
  }

  "Div" should "divide two number values and return a number" in {
    val e1 = Num(8)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Div, e1, e2))
    assert(e3 === Num(1.6))
  }

  "Arithmetic Operators" should "produce non-intuitive solutions given differing expression types" in {
    val e1 = Bool(true)
    val e2 = Num(7)
    assert(iterateStep(BinOp(Plus,e1,e2)) == Num(8))
  }

}

class SmallComparisonSpec extends FlatSpec {

  "Eq" should "return true if two numerical values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Eq, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Eq" should "return false if two numerical values are not the same" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = iterateStep(BinOp(Eq, e1, e2))
    assert(e3 === Bool(false))
  }
  
  "Ne" should "return true if two numerical values are different" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = iterateStep(BinOp(Ne, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Ne" should "return false if two numerical values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Ne, e1, e2))
    assert(e3 === Bool(false))
  }
  
  "Lt" should "return true if the first expression is less than the second" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = iterateStep(BinOp(Lt, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Lt" should "return false if the first expression is not strictly less than the second" in {
    val e1 = Num(7)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Lt, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Lt" should "return false if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Lt, e1, e2))
    assert(e3 === Bool(false))
  } 

  "Le" should "return true if the first expression is less than the second" in {
    val e1 = Num(5)
    val e2 = Num(7)
    val e3 = iterateStep(BinOp(Le, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Le" should "return false if the first expression is greater than the second" in {
    val e1 = Num(7)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Le, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Le" should "return true if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Le, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Gt" should "return true if the first expression is greater than the second" in {
    val e1 = Num(8)
    val e2 = Num(7)
    val e3 = iterateStep(BinOp(Gt, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Gt" should "return false if the first expression is not strictly greater than the second" in {
    val e1 = Num(4)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Gt, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Gt" should "return false if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Gt, e1, e2))
    assert(e3 === Bool(false))
  } 
  
  "Ge" should "return true if the first expression is greater than the second" in {
    val e1 = Num(8)
    val e2 = Num(7)
    val e3 = iterateStep(BinOp(Ge, e1, e2))
    assert(e3 === Bool(true))
  } 
  
  "Ge" should "return false if the first expression is less than the second" in {
    val e1 = Num(4)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Ge, e1, e2))
    assert(e3 === Bool(false))
  } 
     
  "Ge" should "return true if two number values are the same" in {
    val e1 = Num(5)
    val e2 = Num(5)
    val e3 = iterateStep(BinOp(Ge, e1, e2))
    assert(e3 === Bool(true))
  }
  
  "Comparisons" should "produce non-intuitive results given the expressions given" in {
    val e1 = Num(5)
    val e2 = Undefined
    assert(iterateStep(BinOp(Eq,e1,e2)) === Bool(false))
  } 
  
}

class SmallConstSpec extends FlatSpec {
  "ConstDecl" should "extend the environment with the first expression results bound to the identifier, and then eval the second expression" in {
    val e1 = Num(3)
    val e2 = BinOp(Plus, Var("x"), Num(1))
    val e3 = iterateStep(ConstDecl("x", e1, e2)) 
    assert(e3 === Num(4))
  }   
}

class SmallIfSpec extends FlatSpec {

  "If" should "eval the first expression if the conditional is true" in {
    val e1 = BinOp(Plus, Num(3), Num(2))
    val e2 = BinOp(Plus, Num(1), Num(1))
    val e3 = iterateStep(If(Bool(true), e1, e2)) 
    assert(e3 === Num(5))
  } 
  
  "If" should "eval the second expression if the conditional is false" in {
    val e1 = BinOp(Plus, Num(3), Num(2))
    val e2 = BinOp(Plus, Num(1), Num(1))
    val e3 = iterateStep(If(Bool(false), e1, e2)) 
    assert(e3 === Num(2))
  } 
  
}

class SmallSeqSpec extends FlatSpec {

  "Seq" should "execute the first expression, followed by the second, and should eval to the second expression" in {
    val e1 = BinOp(Plus, Num(3), Num(2))
    val e2 = BinOp(Plus, Num(1), Num(1))
    val e3 = iterateStep(BinOp(Seq, e1, e2)) 
    assert(e3 === Num(2))
  } 
  
}

class SmallUnOpSpec extends FlatSpec {

  "Neg" should "return the negation of a number value" in {
    val e1 = Num(5)
    val e2 = iterateStep(UnOp(UMinus, e1))
    assert(e2 === Num(-5))
  } 
  
  "Not" should "return the compliment of a boolean value" in {
    val e1 = Bool(true)
    val e2 = Bool(false)
    val e3 = iterateStep(UnOp(Not, e1))
    val e4 = iterateStep(UnOp(Not, e2))
    assert(e3 === Bool(false))
    assert(e4 === Bool(true))
  }
}

class SmallFunctionCallSpec extends FlatSpec {

  "Functions" should "be considered values" in {
    val f = "f"
    val x = "x"
    val e1 = Function(None, x, Var(x))
    val e2 = Function(Some(f), x, Var(x))
    assert(iterateStep(e1) == e1)
    assert(iterateStep(e2) == e2)
  } 
  
  "Call" should "evaluate a function using big-step semantics" in {
    val f = "f"
    val x = "x"
    val e1 = Function(None, x, BinOp(Plus, Var(x), Num(1)))
    val e2 = Num(2)
    val e3 = iterateStep(Call(e1, e2))
    assert(e3 === Num(3))
  }

  "Call" should "handle recursive functions using big-step semantics" in {
    val f = "f"
    val x = "x"
    val fbody = If(BinOp(Eq, Var(x), Num(0)), Var(x), BinOp(Plus, Var(x), Call(Var(f), BinOp(Minus, Var(x), Num(1)))))
    val e1 = Function(Some(f), x, fbody)
    val e2 = Num(3)
    val e3 = iterateStep(Call(e1, e2))
    assert(e3 === Num(6))
  } 
}

