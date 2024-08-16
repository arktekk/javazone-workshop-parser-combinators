package arktekk.oppgave1

import arktekk.ParserSuite
import cats.parse.Parser

class SimpleParsers extends ParserSuite {

  test("parse \"a\"") {
    val input = "a"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"ab\"") {
    val input = "ab"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse \"aba\"") {
    val input = "aba"
    val p     = implement_me

    assertParsesValid(p, input)
  }

  test("parse '1*a b 1*a'") {
    val validInputs   = List("aaba", "aba", "aaaabaaa")
    val invalidInputs = List("baaa", "aabbaa")

    val p = implement_me

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"aa\" og \"aaa\", men ikke \"a\" eller \"aaaa\"") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("a", "aaaa")

    val p = implement_me

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"(a, a)\" (minst to a'er)") {
    val validInputs   = List("(a, a, a)", "(a, a)", "(a, a)")
    val invalidInputs = List("()", "(a)", "(a, aa)", "(a,a)")

    val p = implement_me

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter") {
    val validInputs = List("a", " a", "  a", " a  ")

    val p = implement_me

    assertParsesValid(p, validInputs*)
  }

  test("Batman") {
    val validInput    = "nananananananananana Batman"
    val invalidInputs = List("nanana Batman", "nananananananananananananananananananana Batman")

    val p = implement_me

    assertParsesValid(p, validInput)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("naturlige tall") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = implement_me

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("DNA") {
    val validInputs = List("ACGT", "AAA", "TT", "ATGG", "CCAATG")

    val p = implement_me

    assertParsesValid(p, validInputs*)
  }
}
