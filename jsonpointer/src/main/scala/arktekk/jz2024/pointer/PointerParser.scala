package arktekk.jz2024.pointer

import cats.parse.*

object PointerParser {
  val unescaped = Parser.charIn(0x00.toChar to 0x2e.toChar) | Parser.charIn(0x30.toChar to 0x7d.toChar) | Parser.charIn(
    0x7f.toChar to 0x10ffff.toChar
  )
  val escaped = Parser.string("~0") | Parser.string("~1")
  val zero    = Parser.char('0').as(Ref.Index(0))

  val endOfList = Parser.char('-').as(Ref.EndOfList)

  val digitWithOutZero = Parser.charIn('1' to '9')
  val digit            = Parser.charIn('0' to '9')
  val numericIndex     = (digitWithOutZero ~ digit.rep0).string.map(x => Ref.Index(x.toInt))

  val sep = Parser.char('/')

  val index: Parser[Ref & ArrayRef] = zero | endOfList | numericIndex
  val property: Parser[Ref]         = (unescaped | escaped).rep.string.map(Ref.Property(_))

  val root = Parser.end.as(Path.Root)

  val parser: Parser0[Path] =
    root | (sep ~ (index | property).repSep(sep)).map((_, list) => Path.Refs(list))

  def parse(input: String): Either[Parser.Error, Path] = parser.parseAll(input)

}
