package arktekk.oppgave3

import cats.parse.Parser

// implementer en parser for TomlBoolean i henhold til ABNF under
// (hentet fra https://github.com/toml-lang/toml/blob/1.0.0/toml.abnf)
//
// ;; Boolean
//
// boolean = true / false
//
// true    = %x74.72.75.65     ; true
// false   = %x66.61.6C.73.65  ; false
//
object TomlBooleanParser {
  case class TomlBoolean(value: String)

  val tomlBoolean: Parser[TomlBoolean] =
    implement_me
}
