package arktekk.oppgave2

import cats.data.NonEmptyList

sealed trait ArrayRef

enum Ref {
  case Property(name: String)
  case Index(idx: Int) extends Ref with ArrayRef
  case EndOfList       extends Ref with ArrayRef
}

enum Path {
  case Root
  case Refs(parts: NonEmptyList[Ref])
}
