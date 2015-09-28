import org.scalatest._
import hw03._


class hw03Spec extends FlatSpec {

  // Check that filter removes all values smaller than n from the list l

  "filter" should "remove all elements from the list l that are smaller than n" in {
     val l = Cons(3, Cons(4, Cons(-2, Cons(0, Nil))))
    
     assert(filter(0, Nil) === Nil)
     assert(filter(-3, l) === l)
     assert(filter(0, l) === Cons(3, Cons(4, Cons(0, Nil))))
     assert(filter(2, l) === Cons(3, Cons(4, Nil)))
     assert(filter(4, l) === Cons(4, Nil))
     assert(filter(5, l) === Nil)
  }

}
