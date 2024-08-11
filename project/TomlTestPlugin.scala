import sbt._, Keys._
import java.nio.file.Paths
import java.nio.file.Files
import java.nio.charset.StandardCharsets
import java.nio.file.StandardOpenOption

object TomlTestPlugin extends AutoPlugin {
  override def trigger = NoTrigger

  override def projectSettings: Seq[Setting[_]] = Seq(
    Test / sourceGenerators += Def.task {
      val sourcedir = (Test / sourceDirectory).value / "scala"
      GenerateTests.gen(baseDirectory.value, sourcedir)
    }.taskValue
  )

  object GenerateTests {

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

    val implemented = List("integer", "bool", "datetime")

    def gen(basedir: File, sourceDir: File): Seq[File] = {
      println("basedir: " + basedir)
      val tomlBaseDir     = basedir.getParentFile()
      val tomlTestBaseDir = s"$tomlBaseDir/toml-tests"

      // list all dirs in valid
      val validDirs            = listDirectories(s"$tomlTestBaseDir/valid/")
      val implementedValidDirs = validDirs.filter(d => implemented.contains(d.getName))
      println(s"implementedValidDirs = ${implementedValidDirs}")

      val validTests = implementedValidDirs.map { dir =>
        val files: List[File] = listFiles(dir.toString)

        // grupper json og toml-filer
        val groupedFiles: Map[String, List[File]] = files.groupBy(file => file.getName.split("\\.")(0))

        val tests: List[ValidTomlTest] = groupedFiles.map { case (name, files) =>
          val json: String =
            files.find(f => f.getName.endsWith(".json")).map(IO.read(_)).get.replaceAll("\n", "\n      |")
          val toml: String =
            files.find(f => f.getName.endsWith(".toml")).map(IO.read(_)).get.replaceAll("\n", "\n      |")
          ValidTomlTest(name = name, parserName = "arktekk.jz2024.toml.toml", tomlContent = toml, jsonContent = json)
        }.toList
        val testSuite = TomlTestSuite(dir.getName + "Valid", "arktekk.jz2024.generated.toml", tests)

        testSuite.writeFile(sourceDir)
      }

      // list all dirs in invalid
      val invalidDirs            = listDirectories(s"$tomlTestBaseDir/invalid/")
      val implementedInvalidDirs = invalidDirs.filter(d => implemented.contains(d.getName))
      println(s"implementedInvalidDirs = ${implementedInvalidDirs}")

      val invalidTests = implementedInvalidDirs.map { dir =>
        val files: List[File] = listFiles(dir.toString)

        val tests: List[InvalidTomlTest] = files.map { file =>
          InvalidTomlTest(
            name = file.getName.split("\\.")(0),
            parserName = "arktekk.jz2024.toml.toml",
            tomlContent = IO.read(file).replaceAll("\n", "\n      |")
          )
        }
        val testSuite = TomlTestSuite(dir.getName + "Invalid", "arktekk.jz2024.generated.toml", tests)

        testSuite.writeFile(sourceDir)
      }
      validTests ++ invalidTests
    }

  }

  case class TomlTestSuite(name: String, pack: String, tests: List[TomlTest]) {
    private def formatScalaCode(code: String): String = {
      val scalaConfig =
        org.scalafmt.config.ScalafmtConfig
          .fromHoconString("""version = "3.7.17"
                           |runner.dialect = scala3
                           |
                           |align.preset = more
                           |maxColumn = 120
                           |""".stripMargin)
          .get

      org.scalafmt.Scalafmt.format(code, scalaConfig).get
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

    def writeFile(srcDir: File) = {
      val packageDirs = pack.replace('.', '/')
      val filename    = s"$srcDir/$packageDirs/${name.capitalize}.scala"

      val path = Paths.get(filename)
      Files.createDirectories(path.getParent)

      Files.write(
        path,
        this.toFormattedClass.getBytes(StandardCharsets.UTF_8),
        StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING
      )
      path.toFile
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
       !  val result = $parserName.parseAll(\"\"\"$tomlContent\"\"\".stripMargin)
       !
       !  assert(result.isLeft)
       !}
       !""".stripMargin('!')
    }
  }
}
