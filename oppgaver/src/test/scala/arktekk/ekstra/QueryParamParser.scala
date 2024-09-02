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

  def queryParams1: Parser[QueryParams] =
    for {
      _      <- char('?')
      params <- implement_me
    } yield extractQueryParams(params)

  def queryParams: Parser0[QueryParams] =
    queryParams1 | Parser.pure(QueryParams.empty)

}
