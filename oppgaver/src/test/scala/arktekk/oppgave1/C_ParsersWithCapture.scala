package arktekk.oppgave1

import arktekk.ParserSuite
import cats.parse.Parser

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

    val p: Parser[String] = implement_me

    assertParses(p, validInputs)
  }

  test("unicode characters") {
    // alt fra 0x00 til 0x10ffff
    val validInputs = List(
      "abcde"  -> "abcde",
      "ABCDE"  -> "ABCDE",
      "æøåÆØÅ" -> "æøåÆØÅ"
    )

    val p: Parser[String] = implement_me

    assertParses(p, validInputs)
  }

  test("boolean") {
    // her trenger vi i tillegg .map eller .as
    val validInputs = List(
      "true"  -> true,
      "false" -> false
    )

    val p: Parser[Boolean] = implement_me

    assertParses(p, validInputs)
  }
}
