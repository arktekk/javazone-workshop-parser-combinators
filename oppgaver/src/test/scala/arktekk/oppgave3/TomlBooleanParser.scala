package arktekk.oppgave3

import cats.parse.Parser

// https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf

object TomlBooleanParser {
  case class TomlBoolean(value: String)

  // ;; Boolean
  // false   = %x66.61.6C.73.65  ; false
  // true    = %x74.72.75.65     ; true
  // boolean = true / false
  def tomlBoolean: Parser[TomlBoolean] =
    implement_me
}
