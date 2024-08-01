package jz2024.qp

import scala.collection.immutable.ListMap

final case class QueryParams(values: ListMap[String, List[String]])

object QueryParams {
  val empty = QueryParams(ListMap.empty)
}