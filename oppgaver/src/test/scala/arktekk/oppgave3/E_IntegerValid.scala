package arktekk.oppgave3

import arktekk.ParserSuite
import io.circe.parser.parse as parseJson
import io.circe.syntax.EncoderOps
import org.scalatest.funsuite.AnyFunSuite

class E_IntegerValid extends ParserSuite {
  test("float64-max") {
    // Maximum and minimum safe float64 natural numbers. Mainly here for -int-as-float.
    val inputs = List(
      "9_007_199_254_740_991"  -> TomlInteger("9007199254740991"),
      "-9_007_199_254_740_991" -> TomlInteger("-9007199254740991")
    )

    assertParses(integer, inputs)
  }

  test("zero") {
    val inputs = List(
      "0"       -> TomlInteger("0"),
      "+0"      -> TomlInteger("0"),
      "-0"      -> TomlInteger("0"),
      "0x0"     -> TomlInteger("0"),
      "0x00"    -> TomlInteger("0"),
      "0x00000" -> TomlInteger("0"),
      "0o0"     -> TomlInteger("0"),
      "0o00"    -> TomlInteger("0"),
      "0o00000" -> TomlInteger("0"),
      "0b0"     -> TomlInteger("0"),
      "0b00"    -> TomlInteger("0"),
      "0b00000" -> TomlInteger("0")
    )

    assertParses(integer, inputs)
  }

  test("long") {
    // int64 "should" be supported, but is not mandatory. It's fine to skip this test.
    val inputs = List(
      "9223372036854775807"  -> TomlInteger("9223372036854775807"),
      "-9223372036854775808" -> TomlInteger("-9223372036854775808")
    )

    assertParses(integer, inputs)
  }

  test("literals") {
    val inputs = List(
      "0b11010110"  -> TomlInteger("214"),
      "0b1_0_1"     -> TomlInteger("5"),
      "0o01234567"  -> TomlInteger("342391"),
      "0o755"       -> TomlInteger("493"),
      "0o7_6_5"     -> TomlInteger("501"),
      "0xDEADBEEF"  -> TomlInteger("3735928559"),
      "0xdeadbeef"  -> TomlInteger("3735928559"),
      "0xdead_beef" -> TomlInteger("3735928559"),
      "0x00987"     -> TomlInteger("2439")
    )

    assertParses(integer, inputs)
  }

  test("integer") {
    val inputs = List(
      "42"  -> TomlInteger("42"),
      "+42" -> TomlInteger("42"),
      "-42" -> TomlInteger("-42"),
      "0"   -> TomlInteger("0")
    )

    assertParses(integer, inputs)
  }

  test("underscore") {
    val inputs = List(
      "1_000"   -> TomlInteger("1000"),
      "1_1_1_1" -> TomlInteger("1111")
    )
    assertParses(integer, inputs)
  }
}
