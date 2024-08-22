package arktekk.oppgave1

import cats.parse.{Numbers, Parser, Rfc5234}
import arktekk.ParserSuite

class A_SimpleParsers extends ParserSuite {

  test("parse \"a\" vha char") {
    val input = "a"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"ab\" vha char og ~") {
    val input = "ab"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"ab\" vha string") {
    val input = "ab"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"aba\" vha char og ~") {
    val input = "aba"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"aba\" vha char og surroundedBy") {
    val input = "aba"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"aba\" vha char og between") {
    val input = "aba"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"aa\" og \"aaa\", men ikke \"a\" eller \"aaaa\" vha rep") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("a", "aaaa")
    val p             = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("parse \"(a, a)\" (minst to a'er) vha repSep og between") {
    val validInputs   = List("(a, a, a)", "(a, a)", "(a, a, a, a)")
    val invalidInputs = List("()", "(a)", "(a, aa)", "(a,a)")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter vha surroundedBy") {
    val validInputs = List("a", " a", "  a", " a  ")

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  test("Batman bruk string, rep og ~") {
    val validInput    = "nananananananananana Batman"
    val invalidInputs = List("nanana Batman", "nananananananananananananananananananana Batman")

    val p = implement_me

    assertParsesValid(p, validInput)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall vha charsWhile") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall vha charIn") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall vha Numbers.digit") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("naturlige tall vha Numbers.digits") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }

  test("DNA vha charIn") {
    val validInputs = List("ACGT", "AAA", "TT", "ATGG", "CCAATG")

    val p = implement_me

    assertParsesValid(p, validInputs)
  }

  // ABNF
  test("parse '1*a b 1*a' vha char, ~ og rep") {
    val validInputs   = List("aaba", "aba", "aaaabaaa")
    val invalidInputs = List("baaa", "aabbaa")

    val p = implement_me

    assertParsesValid(p, validInputs)
    assertParsesInvalid(p, invalidInputs)
  }
}
