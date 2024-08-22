package arktekk.oppgave1

import cats.parse.{Numbers, Parser, Rfc5234}
import arktekk.ParserSuite

class A_SimpleParsers extends ParserSuite {

  test("parse \"a\" med char") {
    val input = "a"
    val p     = Parser.char('a')

    assertParsesValid(p, input)
  }

  test("parse \"ab\" med char og ~") {
    val input = "ab"
    val p     = Parser.char('a') ~ Parser.char('b')

    assertParsesValid(p, input)
  }

  test("parse \"ab\" med string") {
    val input = "ab"
    val p     = Parser.char('a') ~ Parser.char('b')

    assertParsesValid(p, input)
  }

  test("parse \"aba\" med char og ~") {
    val input = "aba"
    val p     = Parser.char('a') ~ Parser.char('b') ~ Parser.char('a')

    assertParsesValid(p, input)
  }

  test("parse \"aba\" med char og surroundedBy") {
    val input = "aba"
    val p     = Parser.char('a') ~ Parser.char('b') ~ Parser.char('a')

    assertParsesValid(p, input)
  }

  test("parse \"aba\" med char og between") {
    val input = "aba"
    val p     = Parser.char('a') ~ Parser.char('b') ~ Parser.char('a')

    assertParsesValid(p, input)
  }

  test("parse \"aa\" og \"aaa\", men ikke \"a\" eller \"aaaa\" med rep") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("a", "aaaa")
    val p             = Parser.char('a').rep(2, 3)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("parse \"(a, a)\" (minst to a'er) med repSep og between") {
    val validInputs   = List("(a, a, a)", "(a, a)", "(a, a, a, a)")
    val invalidInputs = List("()", "(a)", "(a, aa)", "(a,a)")

    val p =
      Parser.char('(') ~
        Parser.char('a').repSep(2, Parser.string(", ")) ~
        Parser.char(')')

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter med surroundedBy") {
    val validInputs = List("a", " a", "  a", " a  ")

    val p = Parser.char('a').surroundedBy(Rfc5234.wsp.rep0)

    assertParsesValid(p, validInputs)
  }

  test("Batman bruk string, rep og ~") {
    val validInput    = "nananananananananana Batman"
    val invalidInputs = List("nanana Batman", "nananananananananananananananananananana Batman")

    val p = Parser.string("na").rep(10, 10) ~ Rfc5234.wsp ~ Parser.string("Batman")

    assertParsesValid(p, validInput)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall med charsWhile") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = Parser.charsWhile(_.isDigit)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall med charIn") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = Parser.charsWhile(_.isDigit)

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall med Numbers.digit") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = Numbers.digits

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall med Numbers.digits") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = Numbers.digits

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("DNA med charIn") {
    val validInputs = List("ACGT", "AAA", "TT", "ATGG", "CCAATG")

    val p = Parser.charIn("ACGT").rep

    assertParsesValid(p, validInputs)
  }

  // ABNF
  test("parse '1*a b 1*a' med char, ~ og rep") {
    val validInputs   = List("aaba", "aba", "aaaabaaa")
    val invalidInputs = List("baaa", "aabbaa")

    val p = Parser.char('a').rep ~ Parser.char('b') ~ Parser.char('a').rep

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }
}
