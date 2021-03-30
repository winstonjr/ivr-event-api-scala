name := """ivr-event-api-scala"""
organization := "cc.atleastonce"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.3"

resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven"
libraryDependencies ++= Seq(
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test,
  "mysql" % "mysql-connector-java" % "8.0.17",
  "io.getquill" %% "quill-jdbc" % "3.7.0",
  "com.eclipsesource"  %% "play-json-schema-validator" % "0.9.5"
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "cc.atleastonce.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "cc.atleastonce.binders._"
