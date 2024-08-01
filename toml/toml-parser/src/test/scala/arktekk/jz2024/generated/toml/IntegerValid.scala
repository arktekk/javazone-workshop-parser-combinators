package arktekk.jz2024.generated.toml

import io.circe.syntax.EncoderOps
import io.circe.parser.parse as parseJson
import org.scalatest.funsuite.AnyFunSuite

class IntegerValid extends AnyFunSuite {
  test("float64-max") {
    val Right(result) =
      arktekk.jz2024.toml.toml.parseAll("""# Maximum and minimum safe float64 natural numbers. Mainly here for
      |# -int-as-float.
      |max_int =  9_007_199_254_740_991
      |min_int = -9_007_199_254_740_991
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "max_int": {"type": "integer", "value": "9007199254740991"},
      |    "min_int": {"type": "integer", "value": "-9007199254740991"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("zero") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""d1 = 0
      |d2 = +0
      |d3 = -0
      |
      |h1 = 0x0
      |h2 = 0x00
      |h3 = 0x00000
      |
      |o1 = 0o0
      |a2 = 0o00
      |a3 = 0o00000
      |
      |b1 = 0b0
      |b2 = 0b00
      |b3 = 0b00000
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "a2": {"type": "integer", "value": "0"},
      |    "a3": {"type": "integer", "value": "0"},
      |    "b1": {"type": "integer", "value": "0"},
      |    "b2": {"type": "integer", "value": "0"},
      |    "b3": {"type": "integer", "value": "0"},
      |    "d1": {"type": "integer", "value": "0"},
      |    "d2": {"type": "integer", "value": "0"},
      |    "d3": {"type": "integer", "value": "0"},
      |    "h1": {"type": "integer", "value": "0"},
      |    "h2": {"type": "integer", "value": "0"},
      |    "h3": {"type": "integer", "value": "0"},
      |    "o1": {"type": "integer", "value": "0"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("long") {
    val Right(result) =
      arktekk.jz2024.toml.toml.parseAll("""# int64 "should" be supported, but is not mandatory. It's fine to skip this
      |# test.
      |int64-max     = 9223372036854775807
      |int64-max-neg = -9223372036854775808
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "int64-max":     {"type": "integer", "value": "9223372036854775807"},
      |    "int64-max-neg": {"type": "integer", "value": "-9223372036854775808"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("literals") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""bin1 = 0b11010110
      |bin2 = 0b1_0_1
      |
      |oct1 = 0o01234567
      |oct2 = 0o755
      |oct3 = 0o7_6_5
      |
      |hex1 = 0xDEADBEEF
      |hex2 = 0xdeadbeef
      |hex3 = 0xdead_beef
      |hex4 = 0x00987
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "bin1": {"type": "integer", "value": "214"},
      |    "bin2": {"type": "integer", "value": "5"},
      |    "hex1": {"type": "integer", "value": "3735928559"},
      |    "hex2": {"type": "integer", "value": "3735928559"},
      |    "hex3": {"type": "integer", "value": "3735928559"},
      |    "hex4": {"type": "integer", "value": "2439"},
      |    "oct1": {"type": "integer", "value": "342391"},
      |    "oct2": {"type": "integer", "value": "493"},
      |    "oct3": {"type": "integer", "value": "501"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("integer") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""answer = 42
      |posanswer = +42
      |neganswer = -42
      |zero = 0
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "answer":    {"type": "integer", "value": "42"},
      |    "neganswer": {"type": "integer", "value": "-42"},
      |    "posanswer": {"type": "integer", "value": "42"},
      |    "zero":      {"type": "integer", "value": "0"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("underscore") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""kilo = 1_000
      |x = 1_1_1_1
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "kilo": {"type": "integer", "value": "1000"},
      |    "x":    {"type": "integer", "value": "1111"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

}
