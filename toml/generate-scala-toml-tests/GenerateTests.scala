//> using scala "2.12"
//> using dep "org.scalameta::scalafmt-core:3.8.2"

import org.scalafmt.Scalafmt
import org.scalafmt.config.ScalafmtConfig

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path, Paths, StandardOpenOption}
import scala.io.{BufferedSource, Source}

object GenerateTests extends App {

  private def listFiles(directory: String): List[File] = {
    val dir: File = new File(directory)
    if (dir.exists && dir.isDirectory) dir.listFiles.filter(_.isFile).toList
    else List.empty[File]
  }

  private def listDirectories(directory: String): List[File] = {
    val dir: File = new File(directory)
    if (dir.exists && dir.isDirectory) dir.listFiles.filter(_.isDirectory).toList
    else List.empty[File]
  }

  private def slurp(filePath: File): String = {
    val source: BufferedSource = Source.fromFile(filePath)
    try {
      source.mkString
    } finally {
      source.close()
    }
  }

  val implemented = List("integer", "bool")

  val cwd: Path       = Paths.get("").toAbsolutePath
  val projectDir      = cwd.getParent.getParent.toString
  val tomlBaseDir     = s"$projectDir/toml/"
  val tomlTestBaseDir = s"$tomlBaseDir/toml-tests"
  val scalaTestDir    = s"$tomlBaseDir/toml-parser/src/test/scala"

  // list all dirs in valid
  val validDirs            = listDirectories(s"$tomlTestBaseDir/valid/")
  val implementedValidDirs = validDirs.filter(d => implemented.contains(d.getName))
  println(s"implementedValidDirs = ${implementedValidDirs}")

  implementedValidDirs.foreach { dir =>
    val files: List[File] = listFiles(dir.toString)

    // grupper json og toml-filer
    val groupedFiles: Map[String, List[File]] = files.groupBy(file => file.getName.split("\\.")(0))

    val tests: List[ValidTomlTest] = groupedFiles.map { case (name, files) =>
      val json: String = files.find(f => f.getName.endsWith(".json")).map(slurp).get.replaceAll("\n", "\n      |")
      val toml: String = files.find(f => f.getName.endsWith(".toml")).map(slurp).get.replaceAll("\n", "\n      |")
      ValidTomlTest(name = name, parserName = "arktekk.jz2024.toml.toml", tomlContent = toml, jsonContent = json)
    }.toList
    val testSuite = TomlTestSuite(dir.getName, "arktekk.jz2024.generated.toml.valid", tests)

    testSuite.writeFile(scalaTestDir)
  }

  // list all dirs in invalid
  val invalidDirs            = listDirectories(s"$tomlTestBaseDir/invalid/")
  val implementedInvalidDirs = invalidDirs.filter(d => implemented.contains(d.getName))

  implementedInvalidDirs.foreach { dir =>
    val files: List[File] = listFiles(dir.toString)

    val tests: List[InvalidTomlTest] = files.map { file =>
      InvalidTomlTest(
        name = file.getName.split("\\.")(0),
        parserName = "arktekk.jz2024.toml.toml",
        tomlContent = slurp(file).replaceAll("\n", "\n      |")
      )
    }
    val testSuite = TomlTestSuite(dir.getName, "arktekk.jz2024.generated.toml.invalid", tests)

    testSuite.writeFile(scalaTestDir)
  }

}

case class TomlTestSuite(name: String, pack: String, tests: List[TomlTest]) {
  private def formatScalaCode(code: String): String = {
    val scalaConfig: ScalafmtConfig =
      org.scalafmt.config.ScalafmtConfig
        .fromHoconString("""version = "3.7.17"
                           |runner.dialect = scala3
                           |
                           |align.preset = more
                           |maxColumn = 120
                           |""".stripMargin)
        .get

    Scalafmt.format(code, scalaConfig).get
  }

  def toFormattedClass = {
    formatScalaCode(
      s"""package $pack
       !
       !import io.circe.syntax.EncoderOps
       !import io.circe.parser.parse as parseJson
       !import org.scalatest.funsuite.AnyFunSuite
       !
       !class ${name.capitalize} extends AnyFunSuite {
       !${tests.map(_.toMunitTest).mkString("\n")}
       !}
       !""".stripMargin('!')
    )
  }

  def writeFile(srcDir: String) = {
    val packageDirs = pack.replace('.', '/')
    val filename    = s"$srcDir/$packageDirs/${name.capitalize}.scala"

    val path = Paths.get(filename)
    path.getParent.toFile.mkdirs()

    val file = path.toFile
    println(s"file.exists() = ${file.exists()}")

    Files.write(
      Paths.get(filename),
      this.toFormattedClass.getBytes(StandardCharsets.UTF_8),
      StandardOpenOption.CREATE,
      StandardOpenOption.TRUNCATE_EXISTING
    )
  }
}

trait TomlTest {
  def toMunitTest: String
}

case class ValidTomlTest(
    name: String,
    parserName: String,
    tomlContent: String,
    jsonContent: String
) extends TomlTest {
  def toMunitTest = {
    s"""test("$name") {
       !  val Right(result) = $parserName.parseAll(\"\"\"$tomlContent\"\"\".stripMargin): @unchecked
       !  val Right(expectedJson) = parseJson(\"\"\"$jsonContent\"\"\".stripMargin): @unchecked
       !
       !  assert(result.asJson === expectedJson)
       !}
       !""".stripMargin('!')
  }
}

case class InvalidTomlTest(
    name: String,
    parserName: String,
    tomlContent: String
) extends TomlTest {
  def toMunitTest = {
    s"""test("$name") {
       !  val result = $parserName.parseAll(\"\"\"$tomlContent\"\"\".stripMargin): @unchecked
       !
       !  assert(result.isLeft)
       !}
       !""".stripMargin('!')
  }
}
