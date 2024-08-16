package arktekk.oppgave1

import arktekk.ParserSuite
import cats.parse.Parser

class Backtracking extends ParserSuite {

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

    val p: Parser[Tall] = implement_me

    assertParses(p, validInputs*)
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

    val p: Parser[Lengde] = implement_me

    assertParses(p, validInputs*)
  }

}
