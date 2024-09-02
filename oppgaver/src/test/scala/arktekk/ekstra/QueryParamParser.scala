package arktekk.ekstra

import cats.parse.Parser.*
import cats.parse.{Parser, Parser0}

import scala.collection.immutable.{ListMap, Seq}

// Implementer den mest vanlige tolkningen av HTTP query params
// https: //url.spec.whatwg.org/#urlencoded-parsing
object QueryParamParser {

  private def rep0sep0[A](data: Parser0[A], separator: Parser[Any]): Parser0[List[A]] =
    (data.? ~ (separator *> data).rep0).map { case (a, as) => a ++: as }

  def extractQueryParams(tuples: Seq[(QueryParams.Encoded, Option[QueryParams.Encoded])]) =
    QueryParams(
      ListMap.from(tuples.groupMap(_._1)(_._2.toList).view.mapValues(_.flatten.toList))
    )

  val encodedParser: Parser[QueryParams.Encoded] =
    until(charIn("=&#")).filter(!_.contains(" ")).withContext("encoded").map(QueryParams.Encoded.apply)

  val query_param: Parser[(QueryParams.Encoded, Some[QueryParams.Encoded])] =
    for {
      key   <- encodedParser
      _     <- char('=')
      value <- encodedParser | Parser.end.as(QueryParams.Encoded(""))
    } yield key -> Some(value)

  val query_token: Parser[(QueryParams.Encoded, None.type)] =
    encodedParser.map(_ -> None)

  val paramOrToken: Parser0[(QueryParams.Encoded, Option[QueryParams.Encoded])] =
    query_param.backtrack | query_token

  // ? *(key=value)
  val queryParams1: Parser[QueryParams] =
    for {
      _      <- char('?')
      params <- rep0sep0(paramOrToken, char('&'))
    } yield extractQueryParams(params)

  val queryParams: Parser0[QueryParams] =
    queryParams1 | Parser.pure(QueryParams.empty)

}
