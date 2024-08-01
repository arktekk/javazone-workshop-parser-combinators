package arktekk.jz2024.toml

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}

sealed trait TomlVal

object TomlVal {
  given Encoder[TomlVal] = {
    case TomlInteger(value) =>
      Json.obj(
        ("type", Json.fromString("integer")),
        ("value", Json.fromString(value))
      )
    case TomlBoolean(value) =>
      Json.obj(
        ("type", Json.fromString("bool")),
        ("value", Json.fromString(value))
      )
  }
}

case class TomlInteger(value: String) extends TomlVal
case class TomlBoolean(value: String) extends TomlVal

sealed trait TomlExpression
case class TomlKeyVal(key: String, value: TomlVal) extends TomlExpression

case class TomlDocument(expressions: List[TomlExpression])

object TomlDocument {
  given Encoder[TomlDocument] = { doc =>
    Json.obj(
      doc.expressions.map { case TomlKeyVal(key, value: TomlVal) =>
        (key, value.asJson)
      }*
    )
  }
}
