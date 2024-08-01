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

  val implemented = List("integer")

  val cwd: Path       = Paths.get("").toAbsolutePath
  val projectDir      = cwd.getParent.getParent.toString
  val tomlTestBaseDir = s"$projectDir/toml/toml-tests"
  val scalaTestDir    = s"$projectDir/src/test/scala"

  // list all dirs in valid
  val validDirs            = listDirectories(s"$tomlTestBaseDir/valid/")
  val implementedValidDirs = validDirs.filter(d => implemented.contains(d.getName))

  // list all dirs in invalid
  val invalidDirs            = listDirectories(s"$tomlTestBaseDir/invalid/")
  val implementedInvalidDirs = invalidDirs.filter(d => implemented.contains(d.getName))

  implementedValidDirs.map { dir =>
    val files: List[File] = listFiles(dir.toString)

    // grupper json og toml-filer
    val groupedFiles: Map[String, List[File]] = files.groupBy(file => file.getName.split("\\.")(0))

    val tests: List[TomlTest] = groupedFiles.map { case (name, files) =>
      val json: String = files.find(f => f.getName.endsWith(".json")).map(slurp).get.replaceAll("\n", "\n      |")
      val toml: String = files.find(f => f.getName.endsWith(".toml")).map(slurp).get.replaceAll("\n", "\n      |")
      TomlTest(
        parserName = "arktekk.jz2024.toml.toml",
        name = name,
        jsonContent = json,
        tomlContent = toml
      )
    }.toList
    val testSuite = TomlTestSuite("integer", "arktekk.jz2024.generated.toml.valid", tests)

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
       !
       !class ${name.capitalize} extends munit.FunSuite {
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
      StandardOpenOption.CREATE_NEW
    )
  }
}

case class TomlTest(name: String, parserName: String, jsonContent: String, tomlContent: String) {
  def toMunitTest = {
    s"""test("$name") {
       !  val Right(result) = $parserName.parseAll(\"\"\"$tomlContent\"\"\".stripMargin): @unchecked
       !  val Right(expectedJson) = parseJson(\"\"\"$jsonContent\"\"\".stripMargin): @unchecked
       !
       !  assertEquals(result.asJson, expectedJson)
       !}
       !""".stripMargin('!')
  }
}
