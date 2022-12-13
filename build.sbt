import org.flywaydb.core.Flyway
import org.flywaydb.core.internal.util.jdbc.DriverDataSource
import org.flywaydb.core.internal.util.logging.LogFactory
import org.flywaydb.core.internal.util.logging.console.{ConsoleLog, ConsoleLogCreator}
import uk.gov.hmrc.DefaultBuildSettings.{addTestReportOption, integrationTestSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings


lazy val ItTest = config("it") extend Test

lazy val flywayMigrate = taskKey[Unit]("Initialise databases using flyway")

lazy val microservice = Project("emcs-tfe-reference-data", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    majorVersion        := 0,
    scalaVersion        := "2.13.8",
    libraryDependencies ++= AppDependencies.compile ++ AppDependencies.test,
    // https://www.scala-lang.org/2021/01/12/configuring-and-suppressing-warnings.html
    // suppress warnings in generated routes files
    scalacOptions += "-Wconf:src=routes/.*:s",
  )
  .settings(publishingSettings: _*)
  .configs(ItTest)
  .settings(inConfig(ItTest)(Defaults.itSettings): _*)
  .settings(
    ItTest / fork := true,
    ItTest / unmanagedSourceDirectories := Seq((ItTest / baseDirectory).value / "it"),
    ItTest / unmanagedClasspath += baseDirectory.value / "resources",
    Runtime / unmanagedClasspath += baseDirectory.value / "resources",
    ItTest / javaOptions += "-Dlogger.resource=logback-test.xml",
    ItTest / parallelExecution := false,
    addTestReportOption(ItTest, "int-test-reports")
  )
  .settings(flywayMigrate := {
    LogFactory.setLogCreator(new ConsoleLogCreator(ConsoleLog.Level.INFO))
    val f = new Flyway()
    f.setLocations("filesystem:test/resources/database/migrations/sql")
    f.setDataSource(new DriverDataSource(
      Class.forName("oracle.jdbc.OracleDriver").getClassLoader,
      "oracle.jdbc.driver.OracleDriver",
      "jdbc:oracle:thin:@localhost:1521:XE",
      "sys as sysdba",
      "oracle"))
    f.setBaselineOnMigrate(true)
    f.migrate()
  })
  .settings(integrationTestSettings(): _*)
  .settings(resolvers ++= Seq(
    "hmrc-releases" at "https://artefacts.tax.service.gov.uk/artifactory/hmrc-releases/",
    "third-party-maven-releases" at "https://artefacts.tax.service.gov.uk/artifactory/third-party-maven-releases/"))
  .settings(CodeCoverageSettings.settings: _*)
  .settings(PlayKeys.playDefaultPort := 8312)

//flywayLocations := envConfig.value.getStringList("flywayLocations").asScala
//flywayDriver := envConfig.value.getString("jdbcDriver")Resolver.jcenterRepo
//flywayUrl := envConfig.value.getString("jdbcUrl")
//flywayUser := envConfig.value.getString("jdbcUserName")
//flywayPassword := envConfig.value.getString("jdbcPassword")