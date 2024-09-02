package arktekk.oppgave1

import cats.parse.{Parser, Rfc5234}
import arktekk.ParserSuite

class C_ParsersWithCapture extends ParserSuite {
  // her kommer vi til å få bruk for .string

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter") {
    // denne kan løses med .string og .surroundedBy
    // eller med *>, <*, .with1 og .string
    val validInputs = List(
      "a"    -> "a",
      " a"   -> "a",
      "  a"  -> "a",
      " a  " -> "a"
    )

    val p: Parser[String] = Parser.char('a').string.surroundedBy(Rfc5234.wsp.rep0)

    assertParses(p, validInputs)
  }

  test("unicode characters") {
    // alt fra 0x00 til 0x10ffff
    val validInputs = List(
      "abcde"  -> "abcde",
      "ABCDE"  -> "ABCDE",
      "æøåÆØÅ" -> "æøåÆØÅ"
    )

    val p = Parser.charIn(0x00.toChar to 0x10ffff.toChar).rep.string

    assertParses(p, validInputs)
  }

  test("boolean") {
    // her trenger vi i tillegg .map eller .as
    val validInputs = List(
      "true"  -> true,
      "false" -> false
    )

    val p: Parser[Boolean] = Parser.string("true").as(true) | Parser.string("false").as(false)

    assertParses(p, validInputs)
  }
}
