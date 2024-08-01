package arktekk.jz2024.toml

import io.circe.syntax.EncoderOps

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.StdIn.readLine
import scala.sys.exit

object TomlMain extends App {
  var line                      = readLine()
  val input: ListBuffer[String] = mutable.ListBuffer.empty[String]
  while (line != null) {
    input.append(line)
    line = readLine()
  }

  val result = toml.parseAll(input.mkString("\n"))

  result match {
    case Left(value) =>
      exit(1)
    case Right(value) =>
      println(value.asJson)
      exit(0)
  }
}
