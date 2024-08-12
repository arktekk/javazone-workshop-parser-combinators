//ThisBuild / name         := "parse-combinator-ws"
ThisBuild / version      := "1.0"
ThisBuild / scalaVersion := "3.4.2"

lazy val toml = (project in file("toml/toml-parser"))
  .settings(
  name := "toml",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-parse"         % "1.0.0",
    "io.circe"      %% "circe-core"         % "0.14.9",
    "io.circe"      %% "circe-parser"       % "0.14.9",
    "org.scalatest" %% "scalatest"          % "3.2.18" % Test
    )
  )
  .enablePlugins(TomlTestPlugin)

lazy val simpleParsers = (project in file("simple-parsers")).settings(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse" % "1.0.0",
    "org.scalatest" %% "scalatest"  % "3.2.18" % Test
  )
)

lazy val jsonPointer = (project in file("jsonpointer")).settings(
  name := "json-pointer",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse"         % "1.0.0",
    "io.circe"      %% "circe-core"         % "0.14.9",
    "io.circe"      %% "circe-parser"       % "0.14.9",
    "org.scalatest" %% "scalatest"          % "3.2.18" % Test
  )
)


lazy val root = (project in file("."))
  .aggregate(simpleParsers, toml, jsonPointer)
