package arktekk.oppgave1

import arktekk.ParserSuite
import cats.parse.{Parser, Rfc5234}

class D_Backtracking extends ParserSuite {
  // .withContext kan være fint når man debugger

  test("heltall eller flyttall") {
    enum Tall {
      case Flyttall(value: Double)
      case Heltall(value: Int)
    }

    val validInputs = List(
      "12"   -> Tall.Heltall(12),
      "14.9" -> Tall.Flyttall(14.9),
      ".55"  -> Tall.Flyttall(0.55)
    )

    val heltall = Rfc5234.digit.rep.string.map(s => Tall.Heltall(s.toInt)).withContext("heltall")
    val flyttall = (Rfc5234.digit.rep0.with1 ~ Parser.char('.') ~ Rfc5234.digit.rep).string
      .map(s => Tall.Flyttall(s.toDouble))
      .withContext("flyttall")

    val p = flyttall.backtrack | heltall

    assertParses(p, validInputs)
  }

  test("parse mm, cm eller m (f.eks. \"10cm\")") {
    enum Lengde {
      case Millimeter(value: Int)
      case Centimeter(value: Int)
      case Meter(value: Int)
    }

    val validInputs = List(
      "10cm" -> Lengde.Centimeter(10),
      "100m" -> Lengde.Meter(100),
      "45cm" -> Lengde.Centimeter(45),
      "37mm" -> Lengde.Millimeter(37),
      "19m"  -> Lengde.Meter(19)
    )

    val heltall: Parser[Int] = Rfc5234.digit.rep.string.mapFilter(_.toIntOption)

    val millimeter: Parser[Lengde] =
      (heltall <* Parser.string("mm")).map(Lengde.Millimeter.apply).withContext("millimeter")
    val centimeter: Parser[Lengde] =
      (heltall <* Parser.string("cm")).map(Lengde.Centimeter.apply).withContext("centimeter")
    val meter: Parser[Lengde] = (heltall <* Parser.string("m")).map(Lengde.Meter.apply).withContext("meter")

    val p = millimeter.backtrack | centimeter.backtrack | meter

    assertParses(p, validInputs)
  }

}
