package arktekk.jz2024.toml

import io.circe.syntax.EncoderOps
import io.circe.{Encoder, Json}

sealed trait TomlVal

object TomlVal {
  given Encoder[TomlVal] = {
    case TomlBoolean(value) =>
      Json.obj(
        ("type", Json.fromString("bool")),
        ("value", Json.fromString(value))
      )
    case TomlDateLocal(value) =>
      Json.obj(
        ("type", Json.fromString("date-local")),
        ("value", Json.fromString(value))
      )
    case TomlDateTime(value) =>
      Json.obj(
        ("type", Json.fromString("datetime")),
        ("value", Json.fromString(value))
      )
    case TomlDateTimeLocal(value) =>
      Json.obj(
        ("type", Json.fromString("datetime-local")),
        ("value", Json.fromString(value))
      )
    case TomlInteger(value) =>
      Json.obj(
        ("type", Json.fromString("integer")),
        ("value", Json.fromString(value))
      )
    case TomlTimeLocal(value) =>
      Json.obj(
        ("type", Json.fromString("time-local")),
        ("value", Json.fromString(value))
      )
  }
}

case class TomlInteger(value: String)       extends TomlVal
case class TomlBoolean(value: String)       extends TomlVal
case class TomlDateLocal(value: String)     extends TomlVal
case class TomlDateTime(value: String)      extends TomlVal
case class TomlDateTimeLocal(value: String) extends TomlVal
case class TomlTimeLocal(value: String)     extends TomlVal

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
