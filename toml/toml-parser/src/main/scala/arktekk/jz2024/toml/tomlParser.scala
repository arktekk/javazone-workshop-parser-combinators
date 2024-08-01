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
val unsigned_dec_int: P[String] = ((digit1_9 ~ (DIGIT | (underscore ~ DIGIT)).rep) | DIGIT).string

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
  .map(s => s.replaceAll("_", "").toLong.toString)

// integer = dec-int / hex-int / oct-int / bin-int
val integer: P[TomlInteger] = (hex_int | oct_int | bin_int | dec_int)
  .map(TomlInteger.apply)

// ;; Float
//
// nan = %x6e.61.6e  ; nan
// inf = %x69.6e.66  ; inf
// special-float = [ minus / plus ] ( inf / nan )
//
// float-exp-part = [ minus / plus ] zero-prefixable-int
// exp = "e" float-exp-part
//
// zero-prefixable-int = DIGIT *( DIGIT / underscore DIGIT )
// decimal-point = %x2E               ; .
// frac = decimal-point zero-prefixable-int
// float-int-part = dec-int
val float_int_part: P[TomlInteger] = P.defer(???)
//
// float =/ special-float
// float = float-int-part ( exp / frac [ exp ] )
val float = float_int_part

// ;; Boolean
// false   = %x66.61.6C.73.65  ; false
// true    = %x74.72.75.65     ; true
// boolean = true / false
val boolean: P[TomlBoolean] = P.string("true").as(TomlBoolean("true")) | P.string("false").as(TomlBoolean("false"))

// ;; Key-Value pairs
// val = string / boolean / array / inline-table / date-time / float / integer
val value: P[TomlVal] = boolean | integer

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
