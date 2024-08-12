import cats.data.NonEmptyList
import cats.parse.{Parser, Rfc5234, StringCodec}
import org.scalatest.funsuite.AnyFunSuite

import scala.util.Try

class JsonParser extends ParserSuite {

  enum JsonValue {
    case JsonNull
    case JsonBoolean(value: Boolean)
    case JsonNumber(value: BigDecimal)
    case JsonString(value: String)
    case JsonArray(values: List[JsonValue])
    case JsonObject(pairs: List[(String, JsonValue)])
  }
  import JsonValue.*

  val comma = Parser.char(',').surroundedBy(Rfc5234.wsp.rep0)
  val colon = Parser.char(':').surroundedBy(Rfc5234.wsp.rep0)

  val jsonNull: Parser[JsonValue] = Parser.string("null").as(JsonNull).withContext("null")
  val jsonBoolean: Parser[JsonValue] =
    (Parser
      .string("false")
      .as(JsonBoolean(false)) | Parser.string("true").as(JsonBoolean(true))).withContext("boolean")
  val jsonNumber: Parser[JsonValue] =
    cats.parse.Numbers.jsonNumber.mapFilter(s => Try(JsonNumber(BigDecimal(s))).toOption)

  val jsonStringParser: Parser[String] = cats.parse.strings.Json.delimited.parser

  val jsonString: Parser[JsonValue] = jsonStringParser.map(JsonString.apply)

  val jsonScalar: Parser[JsonValue] = jsonNull | jsonBoolean | jsonNumber | jsonString

  val jsonRecur: Parser[JsonValue] = Parser.defer {
    val jsonValue = jsonRecur | jsonScalar
    val jsonArray: Parser[JsonValue] = {
      Parser.string("[]").as(JsonArray(List.empty)).backtrack |
        jsonValue
          .repSep(comma)
          .between(Parser.char('['), Parser.char(']'))
          .map(nel => JsonArray(nel.toList))
    }

    val jsonObject: Parser[JsonValue] =
      Parser.string("{}").as(JsonObject(List.empty)).backtrack |
        ((jsonStringParser <* colon) ~ jsonValue)
          .repSep(comma)
          .between(
            Parser.char('{'),
            Parser.char('}')
          )
          .map((pairs: NonEmptyList[(String, JsonValue)]) => JsonObject(pairs.toList))

    jsonArray | jsonObject
  }

  val jsonValue: Parser[JsonValue] = jsonScalar | jsonRecur

  test("null") {
    assertParses(jsonValue, "null" -> JsonNull)
  }

  test("true/false") {
    val validInputs = List(
      "false" -> JsonBoolean(false),
      "true"  -> JsonBoolean(true)
    )

    assertParses(jsonValue, validInputs*)
  }

  test("jsonNumber") {
    val validInputs = List(
      "123"     -> JsonNumber(BigDecimal("123")),
      "123.123" -> JsonNumber(BigDecimal("123.123"))
    )

    assertParses(jsonValue, validInputs*)
  }

  test("jsonString") {
    val validInputs = List(
      "\"foo\"" -> JsonString("foo"),
      "\"æøå\"" -> JsonString("æøå")
    )
    assertParses(jsonValue, validInputs*)
  }

  test("jsonArray") {
    val validInputs = List(
      "[]"    -> JsonArray(List.empty),
      "[1,2]" -> JsonArray(List(JsonNumber(BigDecimal(1)), JsonNumber(BigDecimal(2))))
    )

    assertParses(jsonValue, validInputs*)
  }

  test("jsonObject") {
    val validInputs = List(
      "{}"             -> JsonObject(List.empty),
      """{"test":1}""" -> JsonObject(List("test" -> JsonNumber(BigDecimal(1))))
    )

    assertParses(jsonValue, validInputs*)
  }
}
