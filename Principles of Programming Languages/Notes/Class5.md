## Scala

- Sealed classes

```scala
object expressions {
    sealed abstract class Expr
    case class Num(num: Int) extends Expr
    case class BinOp(op: Op, left: Expr, right: Expr) extends Expr

    sealed abstract class Op
    case object Plus extends Op
    case object Minus extends Op
    case object Mult extends Op
    case object Div extends Op

    def simplifyTop(e: Expr): Expr = e match {
        case BinOp(Mult, _, e2 @ Num(0)) => e2
        case BinOp(Mult, e1, Num(1)) => e1
        case BinOp(Plus, e1, Num(0)) => e1
        case BinOp(Plus, e1, e2) if e1 == e2 => BinOp(Mult, Num(2), 2)
    }
}
```