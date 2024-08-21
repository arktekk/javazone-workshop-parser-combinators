package arktekk.oppgave3

import arktekk.ParserSuite
import arktekk.oppgave3.TomlIntegerParser.tomlInteger
import org.scalatest.funsuite.AnyFunSuite

class E_IntegerInvalid extends ParserSuite {
  test("us-after-oct") {
    val invalidInput = "0o_1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("invalid-bin") {
    val invalidInput = "0b0012"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("trailing-us") {
    val invalidInput = "123_"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("incomplete-oct") {
    val invalidInput = "0o"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("negative-oct") {
    val invalidInput = "-0o755"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("double-sign-plus") {
    val invalidInput = "++99"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("double-us") {
    val invalidInput = "1__23"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-us-bin") {
    val invalidInput = "_0b1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("trailing-us-oct") {
    val invalidInput = "0o1_"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("invalid-hex-1") {
    val invalidInput = "0xaafz"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("positive-bin") {
    val invalidInput = "+0b11010110"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("capital-oct") {
    val invalidInput = "0O0"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-zero-3") {
    val invalidInput = "0_0"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("invalid-hex") {
    val invalidInput = "0xaafz"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-us") {
    val invalidInput = "_123"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("positive-hex") {
    val invalidInput = "+0xff"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-us-hex") {
    val invalidInput = "_0x1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-zero-2") {
    val invalidInput = "00"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-zero-sign-1") {
    val invalidInput = "-01"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-zero-1") {
    val invalidInput = "01"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("text-after-integer") {
    val invalidInput = "42 the ultimate answer?"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-zero-sign-2") {
    val invalidInput = "+01"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("trailing-us-bin") {
    val invalidInput = "0b1_"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("positive-oct") {
    val invalidInput = "+0o755"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-us-oct") {
    val invalidInput = "_0o1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("invalid-oct") {
    val invalidInput = "0o778"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("incomplete-bin") {
    val invalidInput = "0b"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("us-after-bin") {
    val invalidInput = "0b_1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("negative-bin") {
    val invalidInput = "-0b11010110"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("leading-zero-sign-3") {
    val invalidInput = "+0_1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("capital-hex") {
    val invalidInput = "0X1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("invalid-hex-2") {
    val invalidInput = "0xgabba00f1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("trailing-us-hex") {
    val invalidInput = "0x1_"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("double-sign-nex") {
    val invalidInput = "--99"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("capital-bin") {
    val invalidInput = "0B0"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("negative-hex") {
    val invalidInput = "-0xff"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("incomplete-hex") {
    val invalidInput = "0x"

    assertParsesInvalid(tomlInteger, invalidInput)
  }

  test("us-after-hex") {
    val invalidInput = "0x_1"

    assertParsesInvalid(tomlInteger, invalidInput)
  }
}
