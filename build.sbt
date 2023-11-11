ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "zio-test",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.19",
      "dev.zio" %% "zio-streams" % "2.0.19",
      "dev.zio" %% "zio-nio" % "2.0.2",
      "dev.zio" %% "zio-config" % "3.0.7",
      "dev.zio" %% "zio-config-typesafe" % "3.0.7",

    )
  )
