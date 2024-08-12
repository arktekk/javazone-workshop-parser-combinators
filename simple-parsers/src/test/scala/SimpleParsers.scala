import cats.parse.{Parser, Rfc5234}
import org.scalatest.funsuite.AnyFunSuite

class SimpleParsers extends ParserSuite {

  test("parse \"a\"") {
    val input = "a"
    val p     = Parser.char('a')

    assertParsesValid(p, input)
  }

  test("parse \"ab\"") {
    val input = "ab"
    val p     = Parser.char('a') ~ Parser.char('b')

    assertParsesValid(p, input)
  }

  test("parse \"aba\"") {
    val input = "aba"
    val p     = Parser.char('a') ~ Parser.char('b') ~ Parser.char('a')

    assertParsesValid(p, input)
  }

  test("parse '1*a b 1*a'") {
    val validInputs   = List("aaba", "aba", "aaaabaaa")
    val invalidInputs = List("baaa", "aabbaa")

    val p = Parser.char('a').rep ~ Parser.char('b') ~ Parser.char('a').rep

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"aa\" og \"aaa\", men ikke \"a\" eller \"aaaa\"") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("a", "aaaa")
    val p             = Parser.char('a').rep(2, 3)

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"(a, a)\" (minst to a'er)") {
    val validInputs   = List("(a, a, a)", "(a, a)", "(a, a)")
    val invalidInputs = List("()", "(a)", "(a, aa)", "(a,a)")

    val p =
      Parser.char('(') ~
        Parser.char('a').repSep(2, Parser.string(", ")) ~
        Parser.char(')')

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"a\" med vilkårlig mange spaces før og/eller etter") {
    val validInputs = List("a", " a", "  a", " a  ")

    val p = Parser.char('a').surroundedBy(Rfc5234.wsp.rep0)

    assertParsesValid(p, validInputs*)
  }

  test("Batman") {
    val validInput    = "nananananananananana Batman"
    val invalidInputs = List("nanana Batman", "nananananananananananananananananananana Batman")

    val p = Parser.string("na").rep(10, 10) ~ Rfc5234.wsp ~ Parser.string("Batman")

    assertParsesValid(p, validInput)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("naturlige tall") {
    val validInputs   = List("123", "234", "1", "0")
    val invalidInputs = List("-1", "0xff", "a", "12c")

    val p = Parser.charsWhile(_.isDigit)

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("DNA") {
    val validInputs = List("ACGT", "AAA", "TT", "ATGG", "CCAATG")

    val p = Parser.charIn("ACGT").rep

    assertParsesValid(p, validInputs*)
  }

  test("boolean") {
    val validInputs = List(
      "true"  -> true,
      "false" -> false
    )

    val p: Parser[Boolean] = Parser.string("true").as(true) | Parser.string("false").as(false)

    assertParses(p, validInputs*)
  }
}
