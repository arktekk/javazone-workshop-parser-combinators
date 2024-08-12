import cats.parse.{Parser, Rfc5234}
import org.scalatest.funsuite.AnyFunSuite

trait ParserSuite extends AnyFunSuite {

  inline def assertParses[A](parser: Parser[A], inputs: (String, A)*) = {
    inputs.foreach { (input, expectedResult) =>
      val result = parser.parseAll(input)
      assert(result === Right(expectedResult))
    }
  }

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
}
