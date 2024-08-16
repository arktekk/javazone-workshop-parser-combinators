package arktekk.oppgave1

import arktekk.ParserSuite
import cats.parse.Parser

class ParsersWithCapture extends ParserSuite {

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter") {
    val validInputs = List(
      "a"    -> "a",
      " a"   -> "a",
      "  a"  -> "a",
      " a  " -> "a"
    )

    val p: Parser[String] = implement_me

    assertParses(p, validInputs*)
  }

  test("character range") {
    val validInputs = List(
      "abcde"  -> "abcde",
      "ABCDE"  -> "ABCDE",
      "æøåÆØÅ" -> "æøåÆØÅ"
    )

    val p: Parser[String] = implement_me

    assertParses(p, validInputs*)
  }

  test("boolean") {
    val validInputs = List(
      "true"  -> true,
      "false" -> false
    )

    val p: Parser[Boolean] = implement_me

    assertParses(p, validInputs*)
  }
}
