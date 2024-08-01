package arktekk.jz2024.generated.toml.invalid

import io.circe.syntax.EncoderOps
import io.circe.parser.parse as parseJson
import org.scalatest.funsuite.AnyFunSuite

class Integer extends AnyFunSuite {
  test("us-after-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""us-after-oct = 0o_1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("invalid-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""invalid-bin = 0b0012
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("trailing-us") {
    val result = arktekk.jz2024.toml.toml.parseAll("""trailing-us = 123_
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("incomplete-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""incomplete-oct = 0o
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("negative-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""negative-oct = -0o755
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("double-sign-plus") {
    val result = arktekk.jz2024.toml.toml.parseAll("""double-sign-plus = ++99
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("double-us") {
    val result = arktekk.jz2024.toml.toml.parseAll("""double-us = 1__23
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-us-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-us-bin = _0b1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("trailing-us-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""trailing-us-oct = 0o1_
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("invalid-hex-1") {
    val result = arktekk.jz2024.toml.toml.parseAll("""invalid-hex-1 = 0xaafz
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("positive-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""positive-bin = +0b11010110
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("capital-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""capital-oct = 0O0
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-zero-3") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-zero-3 = 0_0
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("invalid-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""invalid-hex = 0xaafz
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-us") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-us = _123
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("positive-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""positive-hex = +0xff
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-us-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-us-hex = _0x1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-zero-2") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-zero-2 = 00
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-zero-sign-1") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-zero-sign-1 = -01
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-zero-1") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-zero-1 = 01
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("text-after-integer") {
    val result = arktekk.jz2024.toml.toml.parseAll("""answer = 42 the ultimate answer?
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-zero-sign-2") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-zero-sign-2 = +01
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("trailing-us-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""trailing-us-bin = 0b1_
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("positive-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""positive-oct = +0o755
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-us-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-us-oct = _0o1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("invalid-oct") {
    val result = arktekk.jz2024.toml.toml.parseAll("""invalid-oct = 0o778
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("incomplete-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""incomplete-bin = 0b
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("us-after-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""us-after-bin = 0b_1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("negative-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""negative-bin = -0b11010110
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("leading-zero-sign-3") {
    val result = arktekk.jz2024.toml.toml.parseAll("""leading-zero-sign-3 = +0_1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("capital-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""capital-hex = 0X1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("invalid-hex-2") {
    val result = arktekk.jz2024.toml.toml.parseAll("""invalid-hex-2 = 0xgabba00f1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("trailing-us-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""trailing-us-hex = 0x1_
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("double-sign-nex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""double-sign-nex = --99
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("capital-bin") {
    val result = arktekk.jz2024.toml.toml.parseAll("""capital-bin = 0B0
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("negative-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""negative-hex = -0xff
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("incomplete-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""incomplete-hex = 0x
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

  test("us-after-hex") {
    val result = arktekk.jz2024.toml.toml.parseAll("""us-after-hex = 0x_1
      |""".stripMargin): @unchecked

    assert(result.isLeft)
  }

}
