package arktekk.oppgave3

import cats.parse.{Parser, Rfc5234}

// https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf

object TomlDateTimeParser {
  sealed trait TomlDateTimeBase
  case class TomlDateLocal(value: String)     extends TomlDateTimeBase
  case class TomlDateTime(value: String)      extends TomlDateTimeBase
  case class TomlDateTimeLocal(value: String) extends TomlDateTimeBase
  case class TomlTimeLocal(value: String)     extends TomlDateTimeBase

  val DIGIT: Parser[Char] = Rfc5234.digit

// ;; Date and Time (as defined in RFC 3339)
//
//date-fullyear  = 4DIGIT
  val date_fullyear = DIGIT.rep(4, 4)

//date-month     = 2DIGIT  ; 01-12
  val date_month = DIGIT.rep(2, 2).string.flatMap { s =>
    val i = s.toInt
    if i >= 1 && i <= 12 then Parser.pure(s)
    else Parser.Fail
  }

//date-mday      = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
  val date_mday = DIGIT.rep(2, 2).string.flatMap { s =>
    val i = s.toInt
    if i >= 1 && i <= 31 then Parser.pure(s)
    else Parser.Fail
  }

//time-delim     = "T" / %x20 ; T, t, or space
  val time_delim = Parser.ignoreCaseCharIn("T ")

//time-hour      = 2DIGIT  ; 00-23
  val time_hour: Parser[String] = DIGIT.rep(2, 2).string.flatMap { s =>
    val i = s.toInt

    if i >= 0 && i <= 23 then Parser.pure(s)
    else Parser.Fail
  }

//time-minute    = 2DIGIT  ; 00-59
  val time_minute: Parser[String] = (Parser.charIn('0' to '5') ~ DIGIT).string.flatMap { s =>
    val i = s.toInt

    if (i >= 0 && i < 60) Parser.pure(s)
    else Parser.fail
  }

//time-second    = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second rules
  val time_second: Parser[String] = DIGIT.rep(2, 2).string.flatMap { s =>
    val i = s.toInt
    if i >= 0 && i <= 59 then Parser.pure(s)
    else Parser.Fail
  }

//time-secfrac   = "." 1*DIGIT
  val time_secfrac: Parser[String] = Parser.char('.') *> DIGIT.rep.string.map(_.padTo(3, '0'))

//time-numoffset = ( "+" / "-" ) time-hour ":" time-minute
  val time_numoffset: Parser[String] = (Parser.charIn("+-") ~ time_hour ~ Parser.char(':') ~ time_minute).string

//time-offset    = "Z" / time-numoffset
  val time_offset: Parser[String] = Parser.ignoreCaseCharIn('Z').as("Z") | time_numoffset

//
//partial-time   = time-hour ":" time-minute ":" time-second [ time-secfrac ]
  val partial_time: Parser[String] =
    ((time_hour <* Parser.char(':'))
      ~ time_minute
      ~ (Parser.char(':') *> (time_second ~ time_secfrac.?)).?)
      .map { case ((hour, minute), maybeSec) =>
        val seconds: String =
          maybeSec match {
            case Some((sec, Some(secFrac))) => sec + "." + secFrac
            case Some((sec, None))          => sec
            case None                       => "00"
          }

        hour + ":" + minute + ":" + seconds
      }

//full-date      = date-fullyear "-" date-month "-" date-mday
  val full_date = date_fullyear ~ Parser.char('-') ~ date_month ~ Parser.char('-') ~ date_mday

//full-time      = partial-time time-offset
  val full_time: Parser[String] = (partial_time ~ time_offset).map { case (partial, offset) => partial + offset }

//;; Offset Date-Time
//
//offset-date-time = full-date time-delim full-time
  val offset_date_time: Parser[TomlDateTime] = ((full_date.string <* time_delim) ~ full_time).map { case (date, time) =>
    TomlDateTime(s"${date}T${time}")
  }
//
//;; Local Date-Time
//
//local-date-time = full-date time-delim partial-time
  val local_date_time: Parser[TomlDateTimeLocal] = ((full_date.string <* time_delim) ~ partial_time).map {
    case (date, time) =>
      TomlDateTimeLocal(s"${date}T$time")
  }

//;; Local Date
//
//local-date = full-date
  val local_date: Parser[TomlDateLocal] = full_date.string.map(TomlDateLocal.apply)
//
//;; Local Time
//
//local-time = partial-time
  val local_time: Parser[TomlTimeLocal] = partial_time.map(TomlTimeLocal.apply)

//date-time      = offset-date-time / local-date-time / local-date / local-time
  val tomlDateTime: Parser[TomlDateTimeBase] =
    offset_date_time.backtrack | local_date_time.backtrack | local_date.backtrack | local_time

}
