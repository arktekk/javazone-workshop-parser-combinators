package arktekk.oppgave3

import cats.parse.{Parser, Rfc5234}

// implementer en parser for TomlDateTimeBase i henhold til ABNF under
// (hentet fra https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf)
//
// ;; Date and Time (as defined in RFC 3339)
//
// date-time      = offset-date-time / local-date-time / local-date / local-time
//
// date-fullyear  = 4DIGIT
// date-month     = 2DIGIT  ; 01-12
// date-mday      = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
// time-delim     = "T" / %x20 ; T, t, or space
// time-hour      = 2DIGIT  ; 00-23
// time-minute    = 2DIGIT  ; 00-59
// time-second    = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second rules
// time-secfrac   = "." 1*DIGIT
// time-numoffset = ( "+" / "-" ) time-hour ":" time-minute
// time-offset    = "Z" / time-numoffset
//
// partial-time   = time-hour ":" time-minute ":" time-second [ time-secfrac ]
// full-date      = date-fullyear "-" date-month "-" date-mday
// full-time      = partial-time time-offset
//
// ;; Offset Date-Time
//
// offset-date-time = full-date time-delim full-time
//
// ;; Local Date-Time
//
// local-date-time = full-date time-delim partial-time
//
// ;; Local Date
//
// local-date = full-date
//
// ;; Local Time
//
// local-time = partial-time
//
object TomlDateTimeParser {
  sealed trait TomlDateTimeBase
  case class TomlDateLocal(value: String)     extends TomlDateTimeBase
  case class TomlDateTime(value: String)      extends TomlDateTimeBase
  case class TomlDateTimeLocal(value: String) extends TomlDateTimeBase
  case class TomlTimeLocal(value: String)     extends TomlDateTimeBase

  def DIGIT: Parser[Char] = implement_me

// ;; Date and Time (as defined in RFC 3339)
//
//date-fullyear  = 4DIGIT
  def date_fullyear = implement_me

//date-month     = 2DIGIT  ; 01-12
  def date_month = implement_me

//date-mday      = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
  def date_mday = implement_me

//time-delim     = "T" / %x20 ; T, t, or space
  def time_delim = implement_me

//time-hour      = 2DIGIT  ; 00-23
  def time_hour: Parser[String] = implement_me

//time-minute    = 2DIGIT  ; 00-59
  def time_minute: Parser[String] = implement_me

//time-second    = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second rules
  def time_second: Parser[String] = implement_me

//time-secfrac   = "." 1*DIGIT
  def time_secfrac: Parser[String] = implement_me

//time-numoffset = ( "+" / "-" ) time-hour ":" time-minute
  def time_numoffset: Parser[String] = implement_me

//time-offset    = "Z" / time-numoffset
  def time_offset: Parser[String] = implement_me

//
//partial-time   = time-hour ":" time-minute ":" time-second [ time-secfrac ]
  def partial_time: Parser[String] = implement_me

//full-date      = date-fullyear "-" date-month "-" date-mday
  def full_date = implement_me

//full-time      = partial-time time-offset
  def full_time: Parser[String] = implement_me

//;; Offset Date-Time
//
//offset-date-time = full-date time-delim full-time
  def offset_date_time: Parser[TomlDateTime] = implement_me
//
//;; Local Date-Time
//
//local-date-time = full-date time-delim partial-time
  def local_date_time: Parser[TomlDateTimeLocal] = implement_me

//;; Local Date
//
//local-date = full-date
  def local_date: Parser[TomlDateLocal] = implement_me
//
//;; Local Time
//
//local-time = partial-time
  def local_time: Parser[TomlTimeLocal] = implement_me

//date-time      = offset-date-time / local-date-time / local-date / local-time
  def tomlDateTime: Parser[TomlDateTimeBase] =
    offset_date_time.backtrack | local_date_time.backtrack | local_date.backtrack | local_time

}
