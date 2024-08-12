import cats.parse.Parser
import org.scalatest.funsuite.AnyFunSuite

class Backtracking extends ParserSuite {

  enum Lengde {
    case Millimeter(value: Int)
    case Centimeter(value: Int)
    case Meter(value: Int)
  }

  test("parse mm, cm eller m (f.eks. \"10cm\")") {
    val validInputs = List(
      "10cm" -> Lengde.Centimeter(10),
      "100m" -> Lengde.Meter(100),
      "45cm" -> Lengde.Centimeter(45),
      "37mm" -> Lengde.Millimeter(37),
      "19m"  -> Lengde.Meter(19)
    )

    val heltall: Parser[Int] = Parser.charsWhile(_.isDigit).map(_.toInt)

    val millimeter: Parser[Lengde] = (heltall <* Parser.string("mm")).map(Lengde.Millimeter.apply)
    val centimeter: Parser[Lengde] = (heltall <* Parser.string("cm")).map(Lengde.Centimeter.apply)
    val meter: Parser[Lengde]      = (heltall <* Parser.string("m")).map(Lengde.Meter.apply)

    val p = millimeter.backtrack | centimeter.backtrack | meter

    assertParses(p, validInputs*)
  }
}
