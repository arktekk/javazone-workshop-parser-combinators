package arktekk.oppgave3

import io.circe.parser.parse as parseJson
import io.circe.syntax.EncoderOps
import org.scalatest.funsuite.AnyFunSuite

class BoolValid extends AnyFunSuite {
  test("bool") {
    val Right(result) = arktekk.oppgave3.toml.parseAll("""t = true
      |f = false
      |""".stripMargin): @unchecked
    val Right(expectedJson) = parseJson("""{
      |    "f": {"type": "bool", "value": "false"},
      |    "t": {"type": "bool", "value": "true"}
      |}
      |""".stripMargin): @unchecked

    assert(result.asJson === expectedJson)
  }

}
