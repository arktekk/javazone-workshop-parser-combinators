package arktekk.jz2024.toml

import cats.data.NonEmptyList
import cats.parse.{Parser0, Rfc5234, Parser as P}

// https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf

val DIGIT: P[Char]  = Rfc5234.digit
val HEXDIG: P[Char] = Rfc5234.hexdig
val LF              = Rfc5234.lf
val CRLF            = Rfc5234.crlf
val ALPHA           = Rfc5234.alpha

// ;; Whitespace
//
// wschar =  %x20  ; Space
// wschar =/ %x09  ; Horizontal tab
val wschar: P[Char] = P.charIn(' ', '\t')

// ws = *wschar
val ws: Parser0[List[Char]] = wschar.rep0

// ;; Newline
//
// newline =  %x0A     ; LF
// newline =/ %x0D.0A  ; CRLF
val newline: P[Unit] = LF | CRLF

// ;; Comment
//
// comment-start-symbol = %x23 ; #
val comment_start_symbol: P[Unit] = P.char('#')

// non-ascii = %x80-D7FF / %xE000-10FFFF
// non-eol = %x09 / %x20-7F / non-ascii
//
// comment = comment-start-symbol *non-eol
val comment: P[Unit] = (comment_start_symbol ~ P.charsWhile0(_ != '\n')).void

// ;; Integer
// minus = %x2D                       ; -
val minus = P.char('-')

// plus = %x2B                        ; +
val plus = P.char('+')

// underscore = %x5F                  ; _
val underscore = P.char('_')

// digit1-9 = %x31-39                 ; 1-9
val digit1_9 = P.charIn('1' to '9')

// digit0-7 = %x30-37                 ; 0-7
val digit0_7 = P.charIn('0' to '7')

// digit0-1 = %x30-31                 ; 0-1
val digit0_1 = P.charIn('0', '1')

// hex-prefix = %x30.78               ; 0x
val hex_prefix: P[Unit] = P.string("0x")

// oct-prefix = %x30.6F               ; 0o
val oct_prefix: P[Unit] = P.string("0o")

// bin-prefix = %x30.62               ; 0b
val bin_prefix: P[Unit] = P.string("0b")

// unsigned-dec-int = DIGIT / digit1-9 1*( DIGIT / underscore DIGIT )
val unsigned_dec_int: P[String] = ((digit1_9 ~ (DIGIT | (underscore ~ DIGIT)).rep0) | DIGIT).string

// hex-int = hex-prefix HEXDIG *( HEXDIG / underscore HEXDIG )
val hex_int: P[String] = hex_prefix *> (HEXDIG ~ (HEXDIG | (underscore ~ HEXDIG)).rep0).string
  .map(s => java.lang.Long.parseLong(s.replaceAll("_", ""), 16).toString)

// oct-int = oct-prefix digit0-7 *( digit0-7 / underscore digit0-7 )
val oct_int: P[String] = oct_prefix *> (digit0_7 ~ (digit0_7 | (underscore ~ digit0_7)).rep0).string
  .map(s => java.lang.Long.parseLong(s.replaceAll("_", ""), 8).toString)

// bin-int = bin-prefix digit0-1 *( digit0-1 / underscore digit0-1 )
val bin_int: P[String] = bin_prefix *> (digit0_1 ~ (digit0_1 | (underscore ~ digit0_1)).rep0).string
  .map(s => java.lang.Long.parseLong(s.replaceAll("_", ""), 2).toString)

// dec-int = [ minus / plus ] unsigned-dec-int
val dec_int: P[String] = ((minus | plus).?.with1 ~ unsigned_dec_int).string
  .map(s => s.replaceAll("_", "").replaceAll("\\+", ""))

// integer = dec-int / hex-int / oct-int / bin-int
val integer: P[TomlInteger] = (hex_int | oct_int | bin_int | dec_int)
  .map { s =>
    val i = s.toLong
    if i == 0L then TomlInteger("0")
    else TomlInteger(s)
  }

// zero-prefixable-int = DIGIT *( DIGIT / underscore DIGIT )
val zero_prefixable_int: P[(Char, Any)] = DIGIT ~ (DIGIT | (underscore *> DIGIT)).rep0

//
// decimal-point = %x2E               ; .
val decimal_point: P[Unit] = P.char('.')

// ;; Boolean
// false   = %x66.61.6C.73.65  ; false
// true    = %x74.72.75.65     ; true
// boolean = true / false
val boolean: P[TomlBoolean] = P.string("true").as(TomlBoolean("true")) | P.string("false").as(TomlBoolean("false"))

// ;; Date and Time (as defined in RFC 3339)
//
//date-fullyear  = 4DIGIT
val date_fullyear = DIGIT.rep(4, 4)

//date-month     = 2DIGIT  ; 01-12
val date_month = DIGIT.rep(2, 2).string.flatMap { s =>
  val i = s.toInt
  if i >= 1 && i <= 12 then P.pure(s)
  else P.Fail
}

//date-mday      = 2DIGIT  ; 01-28, 01-29, 01-30, 01-31 based on month/year
val date_mday = DIGIT.rep(2, 2).string.flatMap { s =>
  val i = s.toInt
  if i >= 1 && i <= 31 then P.pure(s)
  else P.Fail
}

//time-delim     = "T" / %x20 ; T, t, or space
val time_delim = P.ignoreCaseCharIn("T ")

//time-hour      = 2DIGIT  ; 00-23
val time_hour: P[String] = DIGIT.rep(2, 2).string.flatMap { s =>
  val i = s.toInt

  if i >= 0 && i <= 23 then P.pure(s)
  else P.Fail
}

//time-minute    = 2DIGIT  ; 00-59
val time_minute: P[String] = (P.charIn('0' to '5') ~ DIGIT).string.flatMap { s =>
  val i = s.toInt

  if (i >= 0 && i < 60) P.pure(s)
  else P.fail
}

//time-second    = 2DIGIT  ; 00-58, 00-59, 00-60 based on leap second rules
val time_second: P[String] = DIGIT.rep(2, 2).string.flatMap { s =>
  val i = s.toInt
  if i >= 0 && i <= 59 then P.pure(s)
  else P.Fail
}

//time-secfrac   = "." 1*DIGIT
val time_secfrac: P[String] = P.char('.') *> DIGIT.rep.string.map(_.padTo(3, '0'))

//time-numoffset = ( "+" / "-" ) time-hour ":" time-minute
val time_numoffset: P[String] = (P.charIn("+-") ~ time_hour ~ P.char(':') ~ time_minute).string

//time-offset    = "Z" / time-numoffset
val time_offset: P[String] = P.ignoreCaseCharIn('Z').as("Z") | time_numoffset

//
//partial-time   = time-hour ":" time-minute ":" time-second [ time-secfrac ]
val partial_time: P[String] =
  ((time_hour <* P.char(':'))
    ~ time_minute
    ~ (P.char(':') *> (time_second ~ time_secfrac.?)).?)
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
val full_date = date_fullyear ~ P.char('-') ~ date_month ~ P.char('-') ~ date_mday

//full-time      = partial-time time-offset
val full_time: P[String] = (partial_time ~ time_offset).map { case (partial, offset) => partial + offset }

//;; Offset Date-Time
//
//offset-date-time = full-date time-delim full-time
val offset_date_time: P[TomlDateTime] = ((full_date.string <* time_delim) ~ full_time).map { case (date, time) =>
  TomlDateTime(s"${date}T${time}")
}
//
//;; Local Date-Time
//
//local-date-time = full-date time-delim partial-time
val local_date_time: P[TomlDateTimeLocal] = ((full_date.string <* time_delim) ~ partial_time).map { case (date, time) =>
  TomlDateTimeLocal(s"${date}T$time")
}

//;; Local Date
//
//local-date = full-date
val local_date: P[TomlDateLocal] = full_date.string.map(TomlDateLocal.apply)
//
//;; Local Time
//
//local-time = partial-time
val local_time: P[TomlTimeLocal] = partial_time.map(TomlTimeLocal.apply)

//date-time      = offset-date-time / local-date-time / local-date / local-time
val date_time =
  offset_date_time.backtrack | local_date_time.backtrack | local_date.backtrack | local_time

// ;; Key-Value pairs
// val = string / boolean / array / inline-table / date-time / float / integer
val value: P[TomlVal] = boolean | date_time.backtrack | integer

// keyval-sep = ws %x3D ws ; =
val keyval_sep: P[Unit] = (ws.with1 ~ P.char('=') ~ ws).void

// dot-sep   = ws %x2E ws  ; . Period
//
// dotted-key = simple-key 1*( dot-sep simple-key )
// quoted-key = basic-string / literal-string
// unquoted-key = 1*( ALPHA / DIGIT / %x2D / %x5F ) ; A-Z / a-z / 0-9 / - / _
val unquoted_key: P[String] = (ALPHA | DIGIT | minus | underscore).rep.string

// simple-key = quoted-key / unquoted-key
val simple_key: P[String] = unquoted_key

// key = simple-key / dotted-key
val key: P[String] = simple_key

// keyval = key keyval-sep val
val keyval: P[TomlKeyVal] = (key ~ (keyval_sep *> value))
  .map((key, value) => TomlKeyVal(key, value))

// expression =  ws [ comment ]
// expression =/ ws keyval ws [ comment ]
// expression =/ ws table ws [ comment ]
val expression: P[TomlKeyVal] =
  ws.with1 *> keyval

// toml = expression *( newline expression )
val toml: Parser0[TomlDocument] = {
  val emptyline: P[Unit]                       = ((ws ~ comment.?).with1 ~ newline).void
  val expressions: P[NonEmptyList[TomlKeyVal]] = expression.repSep(emptyline.rep)

  (emptyline.rep0 *> expressions <* emptyline.rep0)
    .map(es => TomlDocument(es.toList))
}
