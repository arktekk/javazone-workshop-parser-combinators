package arktekk.jz2024.generated.toml

import io.circe.syntax.EncoderOps
import io.circe.parser.parse as parseJson
import org.scalatest.funsuite.AnyFunSuite

class DatetimeValid extends AnyFunSuite {
  test("local-date") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""bestdayever = 1987-07-05
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "bestdayever": {"type": "date-local", "value": "1987-07-05"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("timezone") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""utc  = 1987-07-05T17:45:56Z
      |pdt  = 1987-07-05T17:45:56-05:00
      |nzst = 1987-07-05T17:45:56+12:00
      |nzdt = 1987-07-05T17:45:56+13:00  # DST
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "nzdt": {"type": "datetime", "value": "1987-07-05T17:45:56+13:00"},
      |    "nzst": {"type": "datetime", "value": "1987-07-05T17:45:56+12:00"},
      |    "pdt":  {"type": "datetime", "value": "1987-07-05T17:45:56-05:00"},
      |    "utc":  {"type": "datetime", "value": "1987-07-05T17:45:56Z"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("datetime") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""space = 1987-07-05 17:45:00Z
      |
      |# ABNF is case-insensitive, both "Z" and "z" must be supported.
      |lower = 1987-07-05t17:45:00z
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "lower": {"type": "datetime", "value": "1987-07-05T17:45:00Z"},
      |    "space": {"type": "datetime", "value": "1987-07-05T17:45:00Z"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("local") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""local = 1987-07-05T17:45:00
      |milli = 1977-12-21T10:32:00.555
      |space = 1987-07-05 17:45:00
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "local": {"type": "datetime-local", "value": "1987-07-05T17:45:00"},
      |    "milli": {"type": "datetime-local", "value": "1977-12-21T10:32:00.555"},
      |    "space": {"type": "datetime-local", "value": "1987-07-05T17:45:00"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("no-seconds") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""# Seconds are optional in date-time and time.
      |without-seconds-1 = 13:37
      |without-seconds-2 = 1979-05-27 07:32Z
      |without-seconds-3 = 1979-05-27 07:32-07:00
      |without-seconds-4 = 1979-05-27T07:32
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "without-seconds-1": {"type": "time-local", "value": "13:37:00"},
      |    "without-seconds-2": {"type": "datetime", "value": "1979-05-27T07:32:00Z"},
      |    "without-seconds-3": {"type": "datetime", "value": "1979-05-27T07:32:00-07:00"},
      |    "without-seconds-4": {"type": "datetime-local", "value": "1979-05-27T07:32:00"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("leap-year") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""2000-datetime       = 2000-02-29 15:15:15Z
      |2000-datetime-local = 2000-02-29 15:15:15
      |2000-date           = 2000-02-29
      |
      |2024-datetime       = 2024-02-29 15:15:15Z
      |2024-datetime-local = 2024-02-29 15:15:15
      |2024-date           = 2024-02-29
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "2000-date":           {"type": "date-local", "value": "2000-02-29"},
      |    "2000-datetime":       {"type": "datetime", "value": "2000-02-29T15:15:15Z"},
      |    "2000-datetime-local": {"type": "datetime-local", "value": "2000-02-29T15:15:15"},
      |    "2024-date":           {"type": "date-local", "value": "2024-02-29"},
      |    "2024-datetime":       {"type": "datetime", "value": "2024-02-29T15:15:15Z"},
      |    "2024-datetime-local": {"type": "datetime-local", "value": "2024-02-29T15:15:15"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("edge") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""first-offset = 0001-01-01 00:00:00Z
      |first-local  = 0001-01-01 00:00:00
      |first-date   = 0001-01-01
      |
      |last-offset = 9999-12-31 23:59:59Z
      |last-local  = 9999-12-31 23:59:59
      |last-date   = 9999-12-31
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "first-date":   {"type": "date-local", "value": "0001-01-01"},
      |    "first-local":  {"type": "datetime-local", "value": "0001-01-01T00:00:00"},
      |    "first-offset": {"type": "datetime", "value": "0001-01-01T00:00:00Z"},
      |    "last-date":    {"type": "date-local", "value": "9999-12-31"},
      |    "last-local":   {"type": "datetime-local", "value": "9999-12-31T23:59:59"},
      |    "last-offset":  {"type": "datetime", "value": "9999-12-31T23:59:59Z"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("milliseconds") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""utc1  = 1987-07-05T17:45:56.123Z
      |utc2  = 1987-07-05T17:45:56.6Z
      |wita1 = 1987-07-05T17:45:56.123+08:00
      |wita2 = 1987-07-05T17:45:56.6+08:00
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "utc1":  {"type": "datetime", "value": "1987-07-05T17:45:56.123Z"},
      |    "utc2":  {"type": "datetime", "value": "1987-07-05T17:45:56.600Z"},
      |    "wita1": {"type": "datetime", "value": "1987-07-05T17:45:56.123+08:00"},
      |    "wita2": {"type": "datetime", "value": "1987-07-05T17:45:56.600+08:00"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

  test("local-time") {
    val Right(result) = arktekk.jz2024.toml.toml.parseAll("""besttimeever = 17:45:00
      |milliseconds = 10:32:00.555
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "besttimeever": {"type": "time-local", "value": "17:45:00"},
      |    "milliseconds": {"type": "time-local", "value": "10:32:00.555"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

}
