lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion = "2.6.0-M2"
val CatsVersion = "1.6.0"
val CirceVersion = "0.11.1"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "ayon",
      scalaVersion := "2.12.8"
    )),
    name := "functional-shopping-cart",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "org.typelevel" %% "cats-core" % CatsVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-literal" % CirceVersion,
      "io.circe" %% "circe-generic-extras" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "de.heikoseeberger" %% "akka-http-circe" % "1.25.2",
      "com.softwaremill.macwire" %% "macros"  % "2.3.2",
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,
      "org.scalamock" %% "scalamock" % "4.2.0" % Test
    )
  )
