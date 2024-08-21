package arktekk.oppgave3

import arktekk.ParserSuite
import org.scalatest.funsuite.AnyFunSuite
import TomlDateTimeParser.*

class B_DatetimeValid extends ParserSuite {

  test("local-date") {
    val input = "1987-07-05" -> TomlDateLocal("1987-07-05")

    assertParses(tomlDateTime, input)
  }

  test("timezone") {
    val validInputs = List(
      "1987-07-05T17:45:56Z"      -> TomlDateTime("1987-07-05T17:45:56Z"),
      "1987-07-05T17:45:56-05:00" -> TomlDateTime("1987-07-05T17:45:56-05:00"),
      "1987-07-05T17:45:56+12:00" -> TomlDateTime("1987-07-05T17:45:56+12:00"),
      "1987-07-05T17:45:56+13:00" -> TomlDateTime("1987-07-05T17:45:56+13:00")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("datetime") {
    // ABNF is case-insensitive, both "Z" and "z" must be supported.
    val validInputs = List(
      "1987-07-05 17:45:00Z" -> TomlDateTime("1987-07-05T17:45:00Z"),
      "1987-07-05t17:45:00z" -> TomlDateTime("1987-07-05T17:45:00Z")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("local") {
    val validInputs = List(
      "1987-07-05T17:45:00"     -> TomlDateTimeLocal("1987-07-05T17:45:00"),
      "1977-12-21T10:32:00.555" -> TomlDateTimeLocal("1977-12-21T10:32:00.555"),
      "1987-07-05 17:45:00"     -> TomlDateTimeLocal("1987-07-05T17:45:00")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("no-seconds") {
    // Seconds are optional in date-time and time.

    val validInputs = List(
      "13:37"                  -> TomlTimeLocal("13:37:00"),
      "1979-05-27 07:32Z"      -> TomlDateTime("1979-05-27T07:32:00Z"),
      "1979-05-27 07:32-07:00" -> TomlDateTime("1979-05-27T07:32:00-07:00"),
      "1979-05-27T07:32"       -> TomlDateTimeLocal("1979-05-27T07:32:00")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("leap-year") {
    val validInputs = List(
      "2000-02-29 15:15:15Z" -> TomlDateTime("2000-02-29T15:15:15Z"),
      "2000-02-29 15:15:15"  -> TomlDateTimeLocal("2000-02-29T15:15:15"),
      "2000-02-29"           -> TomlDateLocal("2000-02-29"),
      "2024-02-29 15:15:15Z" -> TomlDateTime("2024-02-29T15:15:15Z"),
      "2024-02-29 15:15:15"  -> TomlDateTimeLocal("2024-02-29T15:15:15"),
      "2024-02-29"           -> TomlDateLocal("2024-02-29")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("edge") {
    val validInputs = List(
      "0001-01-01 00:00:00Z" -> TomlDateTime("0001-01-01T00:00:00Z"),
      "0001-01-01 00:00:00"  -> TomlDateTimeLocal("0001-01-01T00:00:00"),
      "0001-01-01"           -> TomlDateLocal("0001-01-01"),
      "9999-12-31 23:59:59Z" -> TomlDateTime("9999-12-31T23:59:59Z"),
      "9999-12-31 23:59:59"  -> TomlDateTimeLocal("9999-12-31T23:59:59"),
      "9999-12-31"           -> TomlDateLocal("9999-12-31")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("milliseconds") {
    val validInputs = List(
      "1987-07-05T17:45:56.123Z"      -> TomlDateTime("1987-07-05T17:45:56.123Z"),
      "1987-07-05T17:45:56.6Z"        -> TomlDateTime("1987-07-05T17:45:56.600Z"),
      "1987-07-05T17:45:56.123+08:00" -> TomlDateTime("1987-07-05T17:45:56.123+08:00"),
      "1987-07-05T17:45:56.6+08:00"   -> TomlDateTime("1987-07-05T17:45:56.600+08:00")
    )

    assertParses(tomlDateTime, validInputs)
  }

  test("local-time") {
    val validInputs = List(
      "17:45:00"     -> TomlTimeLocal("17:45:00"),
      "10:32:00.555" -> TomlTimeLocal("10:32:00.555")
    )

    assertParses(tomlDateTime, validInputs)
  }

}
