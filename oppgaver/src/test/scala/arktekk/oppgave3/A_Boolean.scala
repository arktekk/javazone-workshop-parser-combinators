package arktekk.oppgave3

import arktekk.ParserSuite
import org.scalatest.funsuite.AnyFunSuite

class A_Boolean extends ParserSuite {
  test("valid booleans") {
    val validInputs: List[(String, TomlBoolean)] =
      List(
        "true"  -> TomlBoolean("true"),
        "false" -> TomlBoolean("false")
      )

    assertParses(boolean, validInputs)
  }

  test("invalid booleans") {
    val invalidInputs =
      List(
        "trUe",
        "TRUE",
        "falsey",
        "tru",
        "falsE",
        "fals",
        "truer",
        "falsify",
        "True",
        "False",
        "FALSE",
        "t",
        "f",
        "False",
        "truthy"
      )

    assertParsesInvalid(boolean, invalidInputs)
  }
}
