package arktekk.jz2024.generated.toml.invalid

import io.circe.syntax.EncoderOps
import io.circe.parser.parse as parseJson
import org.scalatest.funsuite.AnyFunSuite

class Bool extends AnyFunSuite {
  test("mixed-case-true") {
    val result = arktekk.jz2024.toml.toml.parseAll("""mixed-case-true         = trUe
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("wrong-case-true") {
    val result = arktekk.jz2024.toml.toml.parseAll("""wrong-case-true         = TRUE
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("starting-same-false") {
    val result = arktekk.jz2024.toml.toml.parseAll("""starting-same-false     = falsey
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("almost-true") {
    val result = arktekk.jz2024.toml.toml.parseAll("""almost-true             = tru
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("mixed-case-false") {
    val result = arktekk.jz2024.toml.toml.parseAll("""mixed-case-false        = falsE
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("almost-false") {
    val result = arktekk.jz2024.toml.toml.parseAll("""almost-false            = fals
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("starting-same-true") {
    val result = arktekk.jz2024.toml.toml.parseAll("""starting-same-true      = truer
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("almost-false-with-extra") {
    val result = arktekk.jz2024.toml.toml.parseAll("""almost-false-with-extra = falsify
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("capitalized-true") {
    val result = arktekk.jz2024.toml.toml.parseAll("""capitalized-true         = True
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("capitalized-false") {
    val result = arktekk.jz2024.toml.toml.parseAll("""capitalized-false        = False
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("wrong-case-false") {
    val result = arktekk.jz2024.toml.toml.parseAll("""wrong-case-false        = FALSE
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("just-t") {
    val result = arktekk.jz2024.toml.toml.parseAll("""just-t                  = t
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("just-f") {
    val result = arktekk.jz2024.toml.toml.parseAll("""just-f                  = f
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("mixed-case") {
    val result = arktekk.jz2024.toml.toml.parseAll("""mixed-case              = valid   = False
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("almost-true-with-extra") {
    val result = arktekk.jz2024.toml.toml.parseAll("""almost-true-with-extra  = truthy
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

}
