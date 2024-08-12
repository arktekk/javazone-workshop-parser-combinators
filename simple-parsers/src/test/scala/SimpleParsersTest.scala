import cats.data.NonEmptyList
import cats.parse.{Parser, Rfc5234}
import org.scalatest.funsuite.AnyFunSuite

class SimpleParsers extends AnyFunSuite {

  inline def assertParsesValid[A](parser: Parser[A], inputs: String*) = {
    inputs.foreach { input =>
      val result = parser.parseAll(input)
      assert(result.isRight)
    }
  }

  inline def assertParsesInvalid[A](parser: Parser[A], inputs: String*) = {
    inputs.foreach { input =>
      val result = parser.parseAll(input)
      assert(result.isLeft)
    }
  }

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
}
