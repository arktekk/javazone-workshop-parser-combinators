import cats.data.NonEmptyList
import cats.parse.Parser
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

  test("parse \"aa\" og \"aaa\", men ikke \"a\" eller \"aaaa\"") {
    val validInputs   = List("aa", "aaa")
    val invalidInputs = List("a", "aaaa")
    val p             = Parser.char('a').rep(2, 3)

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse \"ab\"") {
    val input = "ab"
    val p     = Parser.char('a') ~ Parser.char('b')

    assertParsesValid(p, input)
  }

  test("parse abc i stigende rekkefølge") {
    val validInputs   = List("abc", "bc", "c")
    val invalidInputs = List("aba", "ba", "cb")

    val p = (Parser.char('a').? ~ Parser.char('b').?).with1 ~ Parser.char('c')

    assertParsesValid(p, validInputs*)
    assertParsesInvalid(p, invalidInputs*)
  }

  test("parse abc i stigende eller lik rekkefølge") {
    val validInputs                         = List("aaabc", "abc", "bccc", "abbbc", "bbbcc", "c")
    val aParser: Parser[NonEmptyList[Unit]] = Parser.char('a').rep
    val bParser                             = Parser.char('b')

    val p = (Parser.char('a').rep0 ~ Parser.char('b').rep0).with1 ~ Parser.char('c').rep

    assertParsesValid(p, validInputs*)
  }
}
