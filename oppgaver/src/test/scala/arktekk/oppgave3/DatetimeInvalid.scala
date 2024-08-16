package arktekk.oppgave3

import io.circe.parser.parse as parseJson
import io.circe.syntax.EncoderOps
import org.scalatest.funsuite.AnyFunSuite

class DatetimeInvalid extends AnyFunSuite {
  test("offset-overflow-minute") {
    val result = arktekk.oppgave3.toml
      .parseAll("""# Minute must be 00-59; we allow 60 too because some people do write offsets of
      |# 60 minutes
      |d = 1985-06-18 17:04:07+12:61
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("month-under") {
    val result = arktekk.oppgave3.toml.parseAll("""# date-month      = 2DIGIT  ; 01-12
      |d = 2007-00-01T00:00:00-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("feb-30") {
    val result = arktekk.oppgave3.toml.parseAll(""""only 28 or 29 days in february" = 1988-02-30T15:15:15Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("no-leads-with-milli") {
    val result = arktekk.oppgave3.toml.parseAll("""# Day "5" instead of "05"; the leading zero is required.
      |with-milli = 1987-07-5T17:45:00.12Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("y10k") {
    val result = arktekk.oppgave3.toml.parseAll("""# Maximum RFC3399 year is 9999.
      |d = 10000-01-01 00:00:00z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("no-t") {
    val result = arktekk.oppgave3.toml.parseAll("""# No "t" or "T" between the date and time.
      |no-t = 1987-07-0517:45:00Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("minute-over") {
    val result = arktekk.oppgave3.toml.parseAll("""# time-minute     = 2DIGIT  ; 00-59
      |d = 2006-01-01T00:60:00-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("time-no-leads") {
    val result = arktekk.oppgave3.toml.parseAll("""# Leading 0 is always required.
      |d = 2023-10-01T1:32:00Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("no-leads-month") {
    val result = arktekk.oppgave3.toml.parseAll("""# Month "7" instead of "07"; the leading zero is required.
      |no-leads = 1987-7-05T17:45:00Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("mday-under") {
    val result = arktekk.oppgave3.toml.parseAll("""# date-mday       = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on
      |#                           ; month/year
      |d = 2006-01-00T00:00:00-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("hour-over") {
    val result = arktekk.oppgave3.toml.parseAll("""# time-hour       = 2DIGIT  ; 00-23
      |d = 2006-01-01T24:00:00-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("no-leads") {
    val result = arktekk.oppgave3.toml.parseAll("""# Month "7" instead of "07"; the leading zero is required.
      |no-leads = 1987-7-05T17:45:00Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("feb-29") {
    val result = arktekk.oppgave3.toml.parseAll(""""not a leap year" = 2100-02-29T15:15:15Z
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("second-over") {
    val result =
      arktekk.oppgave3.toml.parseAll("""# time-second     = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second
      |#                           ; rules
      |d = 2006-01-01T00:00:61-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("mday-over") {
    val result = arktekk.oppgave3.toml.parseAll("""# date-mday       = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on
      |#                           ; month/year
      |d = 2006-01-32T00:00:00-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("offset-overflow-hour") {
    val result = arktekk.oppgave3.toml.parseAll("""# Hour must be 00-24
      |d = 1985-06-18 17:04:07+25:00
      |""".stripMargin)

    assert(result.isLeft)
  }

  test("month-over") {
    val result = arktekk.oppgave3.toml.parseAll("""# date-month      = 2DIGIT  ; 01-12
      |d = 2006-13-01T00:00:00-00:00
      |""".stripMargin)

    assert(result.isLeft)
  }

}
