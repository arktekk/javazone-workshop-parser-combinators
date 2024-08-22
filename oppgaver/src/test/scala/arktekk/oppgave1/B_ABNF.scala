package arktekk.oppgave1

import arktekk.ParserSuite
import cats.data.NonEmptyList
import cats.parse.{Numbers, Parser, Parser0, Rfc5234}

class B_ABNF extends ParserSuite {

  val a = Parser.char('a')
  val b = Parser.char('b')
  val c = Parser.char('c')
  val d = Parser.char('d')

  test("a b a") {
    val validInputs   = List("aba")
    val invalidInputs = List("baaa", "aabbaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("*a b a") {
    val validInputs   = List("aba", "aaaba", "ba")
    val invalidInputs = List("baaa", "aabbaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("a (b / c) d") {
    val validInputs = List("abd", "acd")

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  test("(a b) / (c d)") {
    val validInputs = List("ac", "ad", "bc", "bd")

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  // %x30-37
  test("%x30-37") {
    val validInputs = (0 to 7).toList.map(_.toString)

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  test("*a") {
    val validInputs = List("", "a", "aa", "aaa", "aaaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  test("*1a") {
    val validInputs   = List("", "a")
    val invalidInputs = List("aa", "aaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("1*a") {
    val validInputs   = List("a", "aa", "aaa")
    val invalidInputs = List("")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("2*3a") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("", "a", "aaaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("[a] 2*b") {
    val validInputs   = List("bb", "abb", "abbb", "bbb")
    val invalidInputs = List("aab", "aabb", "aba")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("2DIGIT") {
    val validInputs   = (10 to 99).map(_.toString).toList
    val invalidInputs = (0 to 9).map(_.toString).toList

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("%x41–5A / %x61–7A") {
    val validInputs   = List(('a' to 'z').toList, ('A' to 'Z').toList).flatten.map(_.toString)
    val invalidInputs = List("0", "1", "%")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("DIGIT / 'A' / 'B' / 'C' / 'D' / 'E' / 'F'") {
    val validInputs = List("0", "1", "6", "D", "E")

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  test("parse '1*a b 1*a'") {
    val validInputs   = List("aaba", "aba", "aaaabaaa")
    val invalidInputs = List("baaa", "aabbaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  // zip-part         = town-name "," SP state 1*2SP zip-code
  // town-name        = 1*(ALPHA / SP)
  // state            = 2ALPHA
  // zip-code         = 5DIGIT ["-" 4DIGIT]
  test("zip") {
    val zip_code  = implement_me
    val state     = implement_me
    val town_name = implement_me
    val zip_part  = implement_me

    val validInputs = List(
      "town, ST 12345",
      "city, AB  12345-4000"
    )

    assertParsesValid(zip_part, validInputs)
  }
}
