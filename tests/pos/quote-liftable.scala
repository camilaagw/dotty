import scala.quoted._

object Test {

  implicit def IntIsLiftable: Liftable[Int] = new {
    def toExpr(n: Int): Expr[Int] = n match {
      case Int.MinValue    => '(Int.MinValue)
      case _ if n < 0      => '(-(~toExpr(n)))
      case 0               => '(0)
      case _ if n % 2 == 0 => '( ~toExpr(n / 2) * 2)
      case _               => '( ~toExpr(n / 2) * 2 + 1)
    }
  }

  implicit def BooleanIsLiftable: Liftable[Boolean] = new {
    implicit def toExpr(b: Boolean) =
      if (b) '(true) else '(false)
  }

  implicit def ListIsLiftable[T: Liftable: Type]: Liftable[List[T]] = new {
    def toExpr(xs: List[T]): Expr[List[T]] = xs match {
      case x :: xs1 => '{ ~implicitly[Liftable[T]].toExpr(x) :: ~toExpr(xs1) }
      case Nil => '(Nil: List[T])
    }
  }

  (true: Expr[Boolean])
  (1: Expr[Byte])
  ('a': Expr[Char])
  (1: Expr[Short])
  (1: Expr[Int])
  (1L: Expr[Long])
  (1.0f: Expr[Float])
  (1.0: Expr[Double])
  ("abc": Expr[String])

  val xs: Expr[List[Int]] = 1 :: 2 :: 3 :: Nil
}
