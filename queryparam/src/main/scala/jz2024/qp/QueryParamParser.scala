package jz2024.qp

import cats.parse.Parser._
import cats.parse.{Parser, Parser0}
import scala.collection.immutable.ListMap
import collection.immutable.Seq

object QueryParamParser {

  private def rep0sep0[A](data: Parser0[A], separator: Parser[Any]): Parser0[List[A]] =
    (data.? ~ (separator *> data).rep0).map { case (a, as) => a ++: as }

  def extractQueryParams(tuples: Seq[(String, Option[String])]) =
    QueryParams(
      ListMap.from(tuples.map(PercentDecoder.decodeTuple).groupMap(_._1)(_._2).view.mapValues(_.flatten.toList))
    )

  val query_param: Parser[(String, Some[String])] =
    for {
      key   <- until(charIn("=&#")).string
      _     <- char('=')
      value <- until0(charIn("&#"))
    } yield key -> Some(value)

  val query_token: Parser[(String, None.type)] =
    for {
      key <- until(charIn("=&#")).string
    } yield key -> None

  val nonQueryTokens = char('&').peek | char('#').peek | Parser.end

  val paramOrToken: Parser0[(String, Option[String])] =
    query_param.backtrack | query_token | nonQueryTokens.as(("", None))

  val queryParams1: Parser[QueryParams] =
    for {
      _      <- char('?')
      params <- rep0sep0(paramOrToken, char('&'))
    } yield extractQueryParams(params)

  val queryParams: Parser0[QueryParams] =
    queryParams1 | Parser.pure(QueryParams.empty)

}
