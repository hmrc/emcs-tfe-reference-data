import uk.gov.hmrc.DefaultBuildSettings


lazy val ItTest = config("it") extend Test

val appName = "emcs-tfe-reference-data"

ThisBuild / scalaVersion := "2.13.16"
ThisBuild / majorVersion := 1

lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(inConfig(Test)(testSettings) *)
  .settings(
    libraryDependencies ++= AppDependencies(),
    PlayKeys.playDefaultPort := 8312,
    retrieveManaged := true,
    update / evictionWarningOptions  :=
      EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    resolvers ++= Seq(Resolver.jcenterRepo),
    // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
    // suppress warnings in generated routes files
    scalacOptions += "-Wconf:src=routes/.*:s",
    Runtime / unmanagedClasspath += baseDirectory.value / "resources"
  )
  .settings(CodeCoverageSettings.settings *)

lazy val testSettings: Seq[Def.Setting[?]] = Seq(
  fork := true,
  unmanagedSourceDirectories += baseDirectory.value / "test-utils",
  Test / javaOptions += "-Dlogger.resource=logback-test.xml",
)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test") // the "test->test" allows reusing test code and test dependencies
  .settings(DefaultBuildSettings.itSettings())
  .settings(libraryDependencies ++= AppDependencies.it)

