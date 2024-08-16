package arktekk.oppgave1

import cats.parse.{Parser, Rfc5234}
import arktekk.ParserSuite

class ParsersWithCapture extends ParserSuite {

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter") {
    val validInputs = List(
      "a"    -> "a",
      " a"   -> "a",
      "  a"  -> "a",
      " a  " -> "a"
    )

    val p: Parser[String] = Parser.char('a').string.surroundedBy(Rfc5234.wsp.rep0)

    assertParses(p, validInputs*)
  }

  test("character range") {
    val validInputs = List(
      "abcde"  -> "abcde",
      "ABCDE"  -> "ABCDE",
      "æøåÆØÅ" -> "æøåÆØÅ"
    )

    val p = Parser.charIn(0x00.toChar to 0x10ffff.toChar).rep.string

    assertParses(p, validInputs*)
  }
}
