package arktekk

import cats.parse.{Parser, Parser0, Rfc5234}
import org.scalatest.funsuite.AnyFunSuite

trait ParserSuite extends AnyFunSuite {

  inline def assertParses[A](parser: Parser[A], inputs: List[(String, A)]): Unit = {
    inputs.foreach { (input, expectedResult) =>
      val result = parser.parseAll(input)
      assert(result === Right(expectedResult))
    }
  }

  inline def assertParses[A](parser: Parser[A], input: (String, A)): Unit =
    assertParses(parser, List(input))

  inline def assertParsesValid[A](parser: Parser0[A], inputs: List[String]): Unit = {
    inputs.foreach { (input: String) =>
      val result = parser.parseAll(input)
      if result.isLeft then {

        val ll = LazyList.from(0)

        val idxs: String = ll.take(input.length).map(i => (i % 10).toString).mkString

        println("Feil under parsing av:")
        println(idxs)
        println(input)
        println(s"resultat:\n$result")
      }
      assert(result.isRight)
    }
  }

  inline def assertParsesValid[A](parser: Parser0[A], input: String): Unit = assertParsesValid(parser, List(input))

  inline def assertParsesInvalid[A](parser: Parser0[A], inputs: List[String]) = {
    inputs.foreach { input =>
      val result = parser.parseAll(input)
      assert(result.isLeft)
    }
  }
}
