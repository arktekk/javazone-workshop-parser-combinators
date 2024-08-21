ThisBuild / version      := "1.0"
ThisBuild / scalaVersion := "3.4.2"

lazy val oppgaver = (project in file("oppgaver")).settings(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse" % "1.0.0",
    "org.scalatest" %% "scalatest"  % "3.2.18" % Test
  )
)
