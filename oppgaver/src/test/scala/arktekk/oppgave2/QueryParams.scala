package arktekk.oppgave2

import scala.collection.immutable.ListMap

final case class QueryParams(values: ListMap[QueryParams.Encoded, List[QueryParams.Encoded]])

object QueryParams {
  opaque type Encoded = String
  object Encoded {
    def apply(s: String): Encoded = s
    extension (e: Encoded) {
      def value: String               = e
      def decoded(d: Decoder): String = d.decode(e)
    }
  }

  val empty = QueryParams(ListMap.empty)
  def of(kv: (String, List[String]), kvs: (String, List[String])*): QueryParams = {
    QueryParams(ListMap.from((kv :: kvs.toList).map((k, list) => Encoded(k) -> list.map(Encoded.apply))))
  }
}
