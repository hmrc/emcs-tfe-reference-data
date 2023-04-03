import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val playSuffix = s"-play-28"

  val boostrapVersion = "7.12.0"
  val scalamockVersion = "5.2.0"
  val catsCoreVersion = "2.3.1"

  val compile = Seq(
    "uk.gov.hmrc" %% s"bootstrap-backend$playSuffix" % boostrapVersion,
    jdbc,
    "com.oracle.jdbc" % "ojdbc8" % "19.3.0.0",
    "com.oracle.jdbc" % "orai18n" % "19.3.0.0",
    "org.typelevel" %% "cats-core" % catsCoreVersion

  )

  val test = Seq(
    "uk.gov.hmrc" %% s"bootstrap-test$playSuffix" % boostrapVersion % "test, it",
    "org.scalamock" %% "scalamock" % "5.2.0" % "test, it",
    "org.jsoup" % "jsoup" % "1.15.3" % Test,
    "com.vladsch.flexmark" % "flexmark-all" % "0.36.8" % "test, it",
    "com.github.tomakehurst" % "wiremock-jre8" % "2.33.2" % "it"

  )
}
