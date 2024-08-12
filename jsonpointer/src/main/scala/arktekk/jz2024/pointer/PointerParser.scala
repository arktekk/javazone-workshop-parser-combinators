package arktekk.jz2024.pointer

import cats.data.NonEmptyList
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

  val sep       = Parser.char('/')
  val emptyProp = sep.as(Ref.Property(""))

  val index: Parser[Ref & ArrayRef] = zero | endOfList | numericIndex
  val property: Parser[Ref] =
    (unescaped | escaped).rep.string.map(Ref.Property(_)) | emptyProp

  val root = Parser.end.as(Path.Root)

  val refs = (emptyProp ~ (index | property).repSep0(sep.?) ~ emptyProp.?).map { case ((empty, list), maybeEnd) =>
    if list.isEmpty then Path.Refs(NonEmptyList.of(empty))
    else Path.Refs(NonEmptyList.fromListUnsafe(list ++ maybeEnd.toList))
  }

  val parser: Parser0[Path] =
    root | refs

  def parse(input: String): Either[Parser.Error, Path] = parser.parseAll(input)

}
