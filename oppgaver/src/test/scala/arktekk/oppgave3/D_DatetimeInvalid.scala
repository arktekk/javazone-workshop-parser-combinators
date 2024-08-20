package arktekk.oppgave3

import arktekk.ParserSuite
import org.scalatest.funsuite.AnyFunSuite

class D_DatetimeInvalid extends ParserSuite {
  test("offset-overflow-minute") {
    // Minute must be 00-59
    val invalidInput = "1985-06-18 17:04:07+12:61"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("month-under") {
    //  date-month      = 2DIGIT  ; 01-12
    val invalidInput = "2007-00-01T00:00:00-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("feb-30") {
    // only 28 or 29 days in february
    val invalidInput = "1988-02-30T15:15:15Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("no-leads-with-milli") {
    // Day "5" instead of "05"; the leading zero is required.
    val invalidInput = "1987-07-5T17:45:00.12Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("y10k") {
    // Maximum RFC3399 year is 9999.
    val invalidInput = "10000-01-01 00:00:00z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("no-t") {
    // No "t" or "T" between the date and time.
    val invalidInput = "1987-07-0517:45:00Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("minute-over") {
    // time-minute     = 2DIGIT  ; 00-59
    val invalidInput = "2006-01-01T00:60:00-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("time-no-leads") {
    // Leading 0 is always required.
    val invalidInput = "2023-10-01T1:32:00Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("no-leads-month") {
    // Month "7" instead of "07"; the leading zero is required.
    val invalidInput = "1987-7-05T17:45:00Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("mday-under") {
    // date-mday       = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
    val invalidInput = "2006-01-00T00:00:00-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("hour-over") {
    // time-hour       = 2DIGIT  ; 00-23
    val invalidInput = "2006-01-01T24:00:00-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("no-leads") {
    // Month "7" instead of "07"; the leading zero is required.
    val invalidInput = "1987-7-05T17:45:00Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("feb-29") {
    // not a leap year
    val invalidInput = "2100-02-29T15:15:15Z"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("second-over") {
    // time-second     = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second rules
    val invalidInput = "2006-01-01T00:00:61-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("mday-over") {
    // date-mday       = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
    val invalidInput = "2006-01-32T00:00:00-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("offset-overflow-hour") {
    // Hour must be 00-24
    val invalidInput = "1985-06-18 17:04:07+25:00"

    assertParsesInvalid(date_time, invalidInput)
  }

  test("month-over") {
    // date-month      = 2DIGIT  ; 01-12
    val invalidInput = "2006-13-01T00:00:00-00:00"

    assertParsesInvalid(date_time, invalidInput)
  }
}
