ThisBuild / version      := "1.0"
ThisBuild / scalaVersion := "3.4.2"

lazy val oppgaver = (project in file("oppgaver")).settings(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse"   % "1.0.0",
    "io.circe"      %% "circe-core"   % "0.14.9",
    "io.circe"      %% "circe-parser" % "0.14.9",
    "org.scalatest" %% "scalatest"    % "3.2.18" % Test
  )
)
