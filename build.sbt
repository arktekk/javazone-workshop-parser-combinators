ThisBuild / version      := "1.0"
ThisBuild / scalaVersion := "3.4.2"

lazy val oppgaver = (project in file("oppgaver")).settings(
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-parse" % "1.0.0",
    "org.scalatest" %% "scalatest"  % "3.2.18" % Test
  )
)

lazy val queryparam = (project in file("queryparam")).configure(config)
.settings(
  libraryDependencies ++= {
    val binVersion = scalaBinaryVersion.value
    Seq(
      "io.lemonlabs" %% "scala-uri" % "4.0.3" exclude("org.typelevel", s"cats-parse_${binVersion}")
    )
  },
)

lazy val root = (project in file("."))
  .aggregate(oppgaver, queryparam)
