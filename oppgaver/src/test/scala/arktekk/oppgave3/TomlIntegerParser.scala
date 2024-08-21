package arktekk.oppgave3

import cats.parse.{Parser, Rfc5234}

// https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf

object TomlIntegerParser {

  case class TomlInteger(value: String)

  val DIGIT: Parser[Char]  = Rfc5234.digit
  val HEXDIG: Parser[Char] = Rfc5234.hexdig

// ;; Integer
// minus = %x2D                       ; -
  val minus = Parser.char('-')

// plus = %x2B                        ; +
  val plus = Parser.char('+')

// underscore = %x5F                  ; _
  val underscore = Parser.char('_')

// digit1-9 = %x31-39                 ; 1-9
  val digit1_9 = Parser.charIn('1' to '9')

// digit0-7 = %x30-37                 ; 0-7
  val digit0_7 = Parser.charIn('0' to '7')

// digit0-1 = %x30-31                 ; 0-1
  val digit0_1 = Parser.charIn('0', '1')

// hex-prefix = %x30.78               ; 0x
  val hex_prefix: Parser[Unit] = Parser.string("0x")

// oct-prefix = %x30.6F               ; 0o
  val oct_prefix: Parser[Unit] = Parser.string("0o")

// bin-prefix = %x30.62               ; 0b
  val bin_prefix: Parser[Unit] = Parser.string("0b")

// unsigned-dec-int = DIGIT / digit1-9 1*( DIGIT / underscore DIGIT )
  val unsigned_dec_int: Parser[String] = ((digit1_9 ~ (DIGIT | (underscore ~ DIGIT)).rep0) | DIGIT).string

// hex-int = hex-prefix HEXDIG *( HEXDIG / underscore HEXDIG )
  val hex_int: Parser[String] = hex_prefix *> (HEXDIG ~ (HEXDIG | (underscore ~ HEXDIG)).rep0).string
    .map(s => java.lang.Long.parseLong(s.replaceAll("_", ""), 16).toString)

// oct-int = oct-prefix digit0-7 *( digit0-7 / underscore digit0-7 )
  val oct_int: Parser[String] = oct_prefix *> (digit0_7 ~ (digit0_7 | (underscore ~ digit0_7)).rep0).string
    .map(s => java.lang.Long.parseLong(s.replaceAll("_", ""), 8).toString)

// bin-int = bin-prefix digit0-1 *( digit0-1 / underscore digit0-1 )
  val bin_int: Parser[String] = bin_prefix *> (digit0_1 ~ (digit0_1 | (underscore ~ digit0_1)).rep0).string
    .map(s => java.lang.Long.parseLong(s.replaceAll("_", ""), 2).toString)

// dec-int = [ minus / plus ] unsigned-dec-int
  val dec_int: Parser[String] = ((minus | plus).?.with1 ~ unsigned_dec_int).string
    .map(s => s.replaceAll("_", "").replaceAll("\\+", ""))

// integer = dec-int / hex-int / oct-int / bin-int
  val tomlInteger: Parser[TomlInteger] = (hex_int | oct_int | bin_int | dec_int)
    .map { s =>
      val i = s.toLong
      if i == 0L then TomlInteger("0")
      else TomlInteger(s)
    }
}
