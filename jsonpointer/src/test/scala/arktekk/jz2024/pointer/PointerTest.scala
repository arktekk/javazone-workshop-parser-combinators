package arktekk.jz2024.pointer

import cats.data.NonEmptyList
import org.scalatest.funsuite.AnyFunSuite

class PointerTest extends AnyFunSuite {
  test("basic - root") {
    val root   = ""
    val path   = Path.Root
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - empty string") {
    val root   = "/"
    val path   = Path.Refs(NonEmptyList.one(Ref.Property("")))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - property") {
    val root   = "/somepath"
    val path   = Path.Refs(NonEmptyList.one(Ref.Property("somepath")))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - property with escaped ~") {
    val root   = "/somepath~0ishere"
    val path   = Path.Refs(NonEmptyList.of(Ref.Property("somepath~0ishere")))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - property with escaped /") {
    val root   = "/somepath~1ishere"
    val path   = Path.Refs(NonEmptyList.of(Ref.Property("somepath~1ishere")))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - array ref") {
    val root   = "/0"
    val path   = Path.Refs(NonEmptyList.of((Ref.Index(0))))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - array ref 23") {
    val root   = "/23"
    val path   = Path.Refs(NonEmptyList.of(Ref.Index(23)))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - end of list") {
    val root   = "/-"
    val path   = Path.Refs(NonEmptyList.of(Ref.EndOfList))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - property then empty string") {
    val root   = "/foo/"
    val path   = Path.Refs(NonEmptyList.of(Ref.Property("foo"), Ref.Property("")))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }

  test("basic - property then empty string then property") {
    val root   = "/foo//bar"
    val path   = Path.Refs(NonEmptyList.of(Ref.Property("foo"), Ref.Property(""), Ref.Property("bar")))
    val parsed = PointerParser.parse(root)
    assert(parsed === Right(path))
  }
}
