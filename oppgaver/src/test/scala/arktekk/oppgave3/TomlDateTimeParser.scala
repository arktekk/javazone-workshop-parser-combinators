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

  // date-time      = offset-date-time / local-date-time / local-date / local-time
  def tomlDateTime: Parser[TomlDateTimeBase] = implement_me

}
