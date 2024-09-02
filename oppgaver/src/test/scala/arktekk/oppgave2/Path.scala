package arktekk.oppgave2

import cats.data.NonEmptyList

sealed trait ArrayRef

enum Ref {
  case Property(name: String)
  case Index(idx: Int) extends Ref with ArrayRef
  case EndOfList       extends Ref with ArrayRef

  def unescape: Ref = this match
    case Property(name) => Property(name.replaceAll("~1", "/").replaceAll("~0", "~"))
    case i: Index       => i
    case EndOfList      => EndOfList

  def escape: Ref = this match
    case Property(name) => Property(name.replaceAll("~", "~0").replaceAll("/", "~1"))
    case i: Index       => i
    case EndOfList      => EndOfList
}

enum Path {
  case Root
  case Refs(parts: NonEmptyList[Ref])

  def unescape: Path = this match
    case Path.Root        => Path.Root
    case Path.Refs(parts) => Path.Refs(parts.map(_.unescape))

  def escape: Path = this match
    case Path.Root        => Path.Root
    case Path.Refs(parts) => Path.Refs(parts.map(_.escape))
}
