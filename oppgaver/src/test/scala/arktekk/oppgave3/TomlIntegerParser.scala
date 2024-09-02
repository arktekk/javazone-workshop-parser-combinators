package arktekk.oppgave3

import cats.parse.{Parser, Rfc5234}

// implementer er parser for TomlInteger i henhold til ABNF under
// (hentet fra https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf)
//
// ;; Integer
//
// integer = dec-int / hex-int / oct-int / bin-int
//
// minus = %x2D                       ; -
// plus = %x2B                        ; +
// underscore = %x5F                  ; _
// digit1-9 = %x31-39                 ; 1-9
// digit0-7 = %x30-37                 ; 0-7
// digit0-1 = %x30-31                 ; 0-1
//
// hex-prefix = %x30.78               ; 0x
// oct-prefix = %x30.6F               ; 0o
// bin-prefix = %x30.62               ; 0b
//
// dec-int = [ minus / plus ] unsigned-dec-int
// unsigned-dec-int = DIGIT / digit1-9 1*( DIGIT / underscore DIGIT )
//
// hex-int = hex-prefix HEXDIG *( HEXDIG / underscore HEXDIG )
// oct-int = oct-prefix digit0-7 *( digit0-7 / underscore digit0-7 )
// bin-int = bin-prefix digit0-1 *( digit0-1 / underscore digit0-1 )
//
object TomlIntegerParser {

  case class TomlInteger(value: String)

  def DIGIT: Parser[Char]  = implement_me
  def HEXDIG: Parser[Char] = implement_me

// ;; Integer
// minus = %x2D                       ; -
  def minus = implement_me

// plus = %x2B                        ; +
  def plus = implement_me

// underscore = %x5F                  ; _
  def underscore = implement_me

// digit1-9 = %x31-39                 ; 1-9
  def digit1_9 = implement_me

// digit0-7 = %x30-37                 ; 0-7
  def digit0_7 = implement_me

// digit0-1 = %x30-31                 ; 0-1
  def digit0_1 = implement_me

// hex-prefix = %x30.78               ; 0x
  def hex_prefix: Parser[Unit] = implement_me

// oct-prefix = %x30.6F               ; 0o
  def oct_prefix: Parser[Unit] = implement_me

// bin-prefix = %x30.62               ; 0b
  def bin_prefix: Parser[Unit] = implement_me

// unsigned-dec-int = DIGIT / digit1-9 1*( DIGIT / underscore DIGIT )
  def unsigned_dec_int: Parser[String] = implement_me

// hex-int = hex-prefix HEXDIG *( HEXDIG / underscore HEXDIG )
  def hex_int: Parser[String] = implement_me

// oct-int = oct-prefix digit0-7 *( digit0-7 / underscore digit0-7 )
  def oct_int: Parser[String] = implement_me

// bin-int = bin-prefix digit0-1 *( digit0-1 / underscore digit0-1 )
  def bin_int: Parser[String] = implement_me

// dec-int = [ minus / plus ] unsigned-dec-int
  def dec_int: Parser[String] = implement_me

// integer = dec-int / hex-int / oct-int / bin-int
  def tomlInteger: Parser[TomlInteger] = (hex_int | oct_int | bin_int | dec_int)
    .map { s =>
      def i = s.toLong
      if i == 0L then TomlInteger("0")
      else TomlInteger(s)
    }
}
