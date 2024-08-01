ThisBuild / name         := "parse-combinator-ws"
ThisBuild / version      := "1.0"
ThisBuild / scalaVersion := "3.4.1"
ThisBuild / testFrameworks += new TestFramework("munit.Framework")

lazy val toml = (project in file("toml/toml-parser")).settings(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse"         % "1.0.0",
    "io.circe"      %% "circe-core"         % "0.14.7",
    "io.circe"      %% "circe-parser"       % "0.14.7",
    "org.scalameta" %% "munit"              % "0.7.29" % Test,
    "com.geirsson"   % "scalafmt-core_2.12" % "1.5.1"  % Test
  )
)

lazy val root = (project in file("."))
  .aggregate(toml)
