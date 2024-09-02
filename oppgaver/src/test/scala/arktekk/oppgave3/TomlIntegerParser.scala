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

  // integer = dec-int / hex-int / oct-int / bin-int
  def tomlInteger: Parser[TomlInteger] = implement_me
}
