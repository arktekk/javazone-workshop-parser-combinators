package jz2024.qp

import java.nio.charset.Charset
import scala.annotation.tailrec
import scala.collection.mutable
import java.nio.charset.StandardCharsets.UTF_8

//Copied from https://github.com/lemonlabsuk/scala-uri/blob/master/shared/src/main/scala/io/lemonlabs/uri/decoding/PercentDecoder.scala
object PercentDecoder {
  private val errorMessage =
    "It looks like this URL isn't Percent Encoded. Ideally you should Percent Encode the relevant parts of your URL"

  private val percentByte = '%'.toByte

  private def decodeBytes(s: String, charset: Charset): Array[Byte] = {
    def toHexByte(hex: String): Option[Byte] =
      try {
        if (hex.length != 2)
          None
        else
          Some(Integer.parseInt(hex, 16).toByte)
      } catch {
        case e: NumberFormatException => None
      }

    @tailrec
    def go(remaining: List[Char], result: mutable.ArrayBuilder[Byte]): Array[Byte] =
      remaining match {
        case Nil =>
          result.result()
        case '%' :: xs =>
          val hex = xs.take(2).mkString
          toHexByte(hex) match {
            case Some(b) =>
              go(xs.drop(2), result += b)
            case _ =>
              throw new RuntimeException(s"Encountered '%' followed by a non hex number '$hex'. $errorMessage")
          }
        case ch :: xs =>
          go(xs, result ++= ch.toString.getBytes(charset))
      }

    go(s.toCharArray.toList, new mutable.ArrayBuilder.ofByte)
  }

  def decode(s: String): String =
    new String(decodeBytes(s, UTF_8), UTF_8)

  def decodeTuple(kv: (String, Option[String])): (String, Option[String]) =
    decode(kv._1) -> kv._2.map(decode)

}
