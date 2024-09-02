package arktekk.oppgave1

import arktekk.ParserSuite
import cats.data.NonEmptyList
import cats.parse.{Numbers, Parser, Parser0, Rfc5234}

/** Se foredrag/Augmented_Backus_Naur_form.pdf for hint til hva de forskjellige type parsere her betyr
  */
class B_ABNF extends ParserSuite {

  val a = Parser.char('a')
  val b = Parser.char('b')
  val c = Parser.char('c')
  val d = Parser.char('d')

  test("a b a") {
    val validInputs   = List("aba")
    val invalidInputs = List("baaa", "aabbaa")

    val p = a ~ b ~ a

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("*a b a") {
    val validInputs   = List("aba", "aaaba", "ba")
    val invalidInputs = List("baaa", "aabbaa")

    val p = a.rep0 ~ b ~ a

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("a (b / c) d") {
    val validInputs = List("abd", "acd")

    val p = a ~ (b | c) ~ d

    assertParsesValid(p, validInputs)
  }

  test("(a / b) ~ (c / d)") {
    val validInputs = List("ac", "ad", "bc", "bd")

    val p = (a | b) ~ (c | d)

    assertParsesValid(p, validInputs)
  }

  // hint: x30 kan skrives som 0x30 i Scala
  // og gjøres om til en char vha toChar
  test("%x30-37") {
    val validInputs = (0 to 7).toList.map(_.toString)

    val p = Parser.charIn(0x30.toChar to 0x37.toChar)

    assertParsesValid(p, validInputs)
  }

  test("*a") {
    val validInputs = List("", "a", "aa", "aaa", "aaaa")

    val p = a.rep0

    assertParsesValid(p, validInputs)
  }

  test("*1a") {
    val validInputs   = List("", "a")
    val invalidInputs = List("aa", "aaa")

    val p = a.?

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("1*a") {
    val validInputs   = List("a", "aa", "aaa")
    val invalidInputs = List("")

    val p = a.rep

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("2*3a") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("", "a", "aaaa")

    val p = a.rep(2, 3)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("[a] 2*b") {
    val validInputs   = List("bb", "abb", "abbb", "bbb")
    val invalidInputs = List("aab", "aabb", "aba")

    val p = a.? ~ b.rep(2)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("2DIGIT") {
    val validInputs   = (10 to 99).map(_.toString).toList
    val invalidInputs = (0 to 9).map(_.toString).toList

    val p = Numbers.digit.rep(2, 2)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("%x41–5A / %x61–7A") {
    val validInputs   = List(('a' to 'z').toList, ('A' to 'Z').toList).flatten.map(_.toString)
    val invalidInputs = List("0", "1", "%")

    val p = Parser.charIn(0x41.toChar to 0x5a.toChar) | Parser.charIn(0x61.toChar to 0x7a.toChar)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("DIGIT / 'A' / 'B' / 'C' / 'D' / 'E' / 'F'") {
    val validInputs = List("0", "1", "6", "D", "E")

    val p = Numbers.digit | Parser.charIn('A' to 'F')

    assertParsesValid(p, validInputs)
  }

  test("parse '1*a b 1*a'") {
    val validInputs   = List("aaba", "aba", "aaaabaaa")
    val invalidInputs = List("baaa", "aabbaa")

    val p = Parser.char('a').rep ~ Parser.char('b') ~ Parser.char('a').rep

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  // zip-part         = town-name "," SP state 1*2SP zip-code
  // town-name        = 1*(ALPHA / SP)
  // state            = 2ALPHA
  // zip-code         = 5DIGIT ["-" 4DIGIT]
  test("zip") {
    val zip_code: Parser[(NonEmptyList[Char], Option[(Unit, NonEmptyList[Char])])] =
      (Numbers.digit.rep(5, 5) ~ (Parser.char('-') ~ Numbers.digit.rep(4, 4)).?).withContext("zip-code")
    val state: Parser[NonEmptyList[Char]]       = Rfc5234.alpha.rep(2, 2).withContext("state")
    val town_name: Parser[NonEmptyList[AnyVal]] = (Rfc5234.alpha | Rfc5234.sp).rep.withContext("town-name")
    val zip_part: Parser[
      (
          ((((NonEmptyList[AnyVal], Unit), Unit), NonEmptyList[Char]), NonEmptyList[Unit]),
          (NonEmptyList[Char], Option[(Unit, NonEmptyList[Char])])
      )
    ] = (town_name ~ Parser.char(',') ~ Rfc5234.sp ~ state ~ Rfc5234.sp.rep(1, 2) ~ zip_code)
      .withContext("zip-part")

    val validInputs = List(
      "town, ST 12345",
      "city, AB  12345-4000"
    )

    assertParsesValid(zip_part, validInputs)
  }
}
