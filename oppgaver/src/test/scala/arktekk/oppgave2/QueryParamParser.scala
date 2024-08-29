package arktekk.oppgave2

import cats.parse.Parser.*
import cats.parse.{Parser, Parser0}

import scala.collection.immutable.{ListMap, Seq}

object QueryParamParser {

  private def rep0sep0[A](data: Parser0[A], separator: Parser[Any]): Parser0[List[A]] =
    (data.? ~ (separator *> data).rep0).map { case (a, as) => a ++: as }

  def extractQueryParams(tuples: Seq[(String, Option[String])]) =
    QueryParams(
      ListMap.from(
        tuples
          .groupMap((k, _) => QueryParams.Encoded(k))((_, v) => v.map(QueryParams.Encoded.apply))
          .view
          .mapValues(_.flatten.toList)
      )
    )

  val keyParser: Parser[String] = until(charIn("=&#")).filter(!_.contains(" ")).withContext("key")

  val query_param: Parser[(String, Some[String])] =
    for {
      key   <- keyParser
      _     <- char('=')
      value <- until0(charIn("&#")).filter(!_.contains(" ")).withContext("value")
    } yield key -> Some(value)

  val query_token: Parser[(String, None.type)] =
    keyParser.map(_ -> None)

  val paramOrToken: Parser0[(String, Option[String])] =
    query_param.backtrack | query_token

  val queryParams1: Parser[QueryParams] =
    for {
      _      <- char('?')
      params <- rep0sep0(paramOrToken, char('&'))
    } yield extractQueryParams(params)

  val queryParams: Parser0[QueryParams] =
    queryParams1 | Parser.pure(QueryParams.empty)

}
