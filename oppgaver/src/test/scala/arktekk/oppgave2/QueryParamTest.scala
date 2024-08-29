package arktekk.oppgave2

import arktekk.ParserSuite
import cats.parse.Parser0

class QueryParamTest extends ParserSuite {
  test("minimal") {
    assertParses(
      QueryParamParser.queryParams,
      List(
        "?"        -> QueryParams.empty,
        ""         -> QueryParams.empty,
        "?foo=bar" -> QueryParams.of("foo" -> List("bar")),
        "?foo"     -> QueryParams.of("foo" -> Nil),
        "?foo="    -> QueryParams.of("foo" -> List("")),
        "?query=select+1+from-table&foo=bar" -> QueryParams.of(
          "query" -> List("select+1+from-table"),
          "foo"   -> List("bar")
        )
      )
    )
  }

  test("% encoded") {
    val result = QueryParamParser.queryParams.parseAll("?yes%20is%20true=meh")
    assert(result.isRight)
    result.fold(
      err => fail(err.toString()),
      qp => assert(qp === QueryParams.of("yes%20is%20true" -> List("meh")))
    )
  }

  test("spaces are not allowed") {
    assertParsesInvalid(
      QueryParamParser.queryParams,
      List(
        "?query=select 1 from table&foo=bar"
      )
    )
  }
}
