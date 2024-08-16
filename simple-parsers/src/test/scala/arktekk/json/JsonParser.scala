package arktekk.json

import cats.data.NonEmptyList
import cats.parse.{Parser, Rfc5234}
import arktekk.ParserSuite

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

  val ows          = (Rfc5234.wsp | Rfc5234.crlf).rep0.void
  val comma        = Parser.char(',').surroundedBy(ows).withContext("comma")
  val colon        = Parser.char(':').surroundedBy(ows)
  val openBracket  = Parser.char('[').surroundedBy(ows)
  val closeBracket = Parser.char(']').surroundedBy(ows).withContext("closeBracket")
  val openBrace    = Parser.char('{').surroundedBy(ows)
  val closeBrace   = Parser.char('}').surroundedBy(ows).withContext("closeBrace")

  val jsonNull: Parser[JsonValue] = Parser.string("null").as(JsonNull).withContext("null")
  val jsonBoolean: Parser[JsonValue] =
    (Parser
      .string("false")
      .as(JsonBoolean(false)) | Parser.string("true").as(JsonBoolean(true))).withContext("boolean")
  val jsonNumber: Parser[JsonValue] =
    cats.parse.Numbers.jsonNumber.mapFilter { s =>
      Try {
        println(JsonNumber(BigDecimal(s)))
        JsonNumber(BigDecimal(s))
      }.toOption
    }

  val jsonStringParser: Parser[String] = cats.parse.strings.Json.delimited.parser

  val jsonString: Parser[JsonValue] = jsonStringParser.map(JsonString.apply)

  val jsonScalar: Parser[JsonValue] = jsonNull | jsonBoolean | jsonNumber | jsonString

  val jsonRecur: Parser[JsonValue] = Parser.defer {
    val jsonValue = (jsonRecur | jsonScalar).surroundedBy(ows)
    val jsonArray: Parser[JsonValue] = {
      (openBracket ~ closeBracket).as(JsonArray(List.empty)).backtrack |
        jsonValue
          .repSep(comma)
          .between(openBracket, closeBracket)
          .map(nel => JsonArray(nel.toList))
    }

    val jsonObject: Parser[JsonValue] = {
      (openBrace ~ closeBrace).as(JsonObject(List.empty)).backtrack |
        ((jsonStringParser <* colon) ~ jsonValue)
          .repSep(comma)
          .between(openBrace, closeBrace)
          .map((pairs: NonEmptyList[(String, JsonValue)]) => JsonObject(pairs.toList))
    }

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
      "123.123" -> JsonNumber(123.123)
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
      " [  ] "    -> JsonArray(List.empty),
      "[ 1, 20 ]" -> JsonArray(List(JsonNumber(1), JsonNumber(20)))
    )

    assertParses(jsonValue, validInputs*)
  }

  test("jsonObject") {
    val validInputs = List(
      "{ }"             -> JsonObject(List.empty),
      """{"test": 1}""" -> JsonObject(List("test" -> JsonNumber(BigDecimal(1)))),
      """{"foo": "bar","fizz":[1,2,3,"fizz"]}""" -> JsonObject(
        List(
          ("foo", JsonString("bar")),
          ("fizz", JsonArray(List(JsonNumber(1), JsonNumber(2), JsonNumber(3), JsonString("fizz"))))
        )
      )
    )

    assertParses(jsonValue, validInputs*)
  }
}
