package arktekk.oppgave2

import cats.parse.*

object PointerParser {
  //       json-pointer    = *( "/" reference-token )
  //      reference-token = *( unescaped / escaped )
  //      unescaped       = %x00-2E / %x30-7D / %x7F-10FFFF ; %x2F ('/') and %x7E ('~') are excluded from 'unescaped'
  //      escaped         = "~" ( "0" / "1" ) ; representing '~' and '/', respectively

  // skal være val
  def parser: Parser0[Path] = implement_me

  def parse(input: String): Either[Parser.Error, Path] = parser.parseAll(input)

}
