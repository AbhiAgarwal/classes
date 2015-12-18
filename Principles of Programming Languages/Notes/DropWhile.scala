// that takes a predicate p and a list l and drops the longest prefix of  elements of l that satisfies p, e.g.,

object DropWhile {
    def dropWhile[A](p: A => Boolean)(l: List[A]): List[A] =
    l match {
        case Nil => l
        case hd :: tl => if (p(head) == True) dropWhile(p)(tl) else l
    }
}

object DropWhileWithAcc {
    def dropWhileWithAcc[A](p: A => Boolean)(acc: List[A], l: List[A]): List[A] =
    l match {
        case Nil => acc
        case hd :: tl => if (p(head)) dropWhileWithAcc(p)(acc.tail, tl) else acc
    }
    def dropWhile[A](p: A => Boolean)(l: List[A]): List[A] =
        dropWhileWithAcc(p)(l, l)
}

object DropWhileFold {
    dropWhile[A](p: A => Boolean)(l: List[A]): List[A] =
    // Initial value is the case where the list is empty
    l.foldLeft(l)((acc: List[A], hd: A) => 
        if (p(hd)) acc.tail
        else acc
    )
}