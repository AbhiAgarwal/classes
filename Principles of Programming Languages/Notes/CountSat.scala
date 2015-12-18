// that counts the number of elements in l for which p returns true

object CountSat {
    def countSat[A](p: A => Boolean)(l: List[A]): Int =
    l match {
        case Nil => 0
        case hd :: tl => if ((hd) 1 else 0) + countSat(p)(tl)
    }
}

object CountSatFoldRight {
    def countSat[A](p: A => Boolean)(l: List[A]): Int =
        l.foldRight(0)((hd: A, acc: Int) => (if(p(hd)) 1 else 0) + acc)
}

object CountSat {
    def countSatWithAcc[A](p: A => Boolean)(l: List[A], acc: Int): Int =
    l match {
        case Nil => acc
        case hd :: tl => if (p(hd)) countSat(tl, acc + 1) else countSat(tl, acc)
    }

    def countSat[A](p: A => Boolean)(l: List[A]): List[A] =
        countSatWithAcc(p)(l, 0)
}

// Create a foldLeft version using a tail-recursive function.
// When you have a tail recursive version you can make it into a tailLeft.