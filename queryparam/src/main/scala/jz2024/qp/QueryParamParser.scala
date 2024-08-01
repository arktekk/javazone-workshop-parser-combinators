package jz2024.qp

import cats.parse.Parser._
import cats.parse.Rfc5234.alpha
import cats.parse.{Parser, Parser0}
import io.lemonlabs.uri.decoder.PercentDecoder
import scala.collection.immutable.ListMap

object QueryParamParser {
  def _query_param: Parser[(String, Some[String])] =
    for {
      key <- until(charIn("=&#")).string
      _ <- char('=')
      value <- until0(charIn("&#"))
    } yield extractTuple(key, value)

  def _query_tok: Parser[(String, None.type)] =
    for {
      key <- until(charIn("=&#")).string
    } yield extractTok(key)

  def nonQueryTokens = char('&').peek | char('#').peek | Parser.end

  def _query_param_or_tok: Parser0[(String, Option[String])] =
    _query_param.backtrack | _query_tok | nonQueryTokens.as(("", None))

  def _query_string: Parser[QueryParams] =
    for {
      _ <- char('?')
      params <- rep0sep0(_query_param_or_tok, char('&'))
    } yield extractQueryParams(params)

  def _maybe_query_string: Parser0[QueryString] =
    _query_string | Parser.pure(QueryParams.empty)

  def extractQueryParams(tuples: immutable.Seq[(String, Option[String])]) =>
    QueryParams(ListMap.from(tuples.map(PercentDecoder.decodeTuple)))
}
