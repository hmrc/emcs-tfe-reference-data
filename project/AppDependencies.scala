import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val playSuffix = s"-play-28"

  val boostrapVersion = "7.22.0"
  val scalamockVersion = "5.2.0"
  val catsCoreVersion = "2.9.0"

  val compile = Seq(
    "uk.gov.hmrc"     %% s"bootstrap-backend$playSuffix" % boostrapVersion,
    "com.oracle.jdbc" % "ojdbc8"                         % "19.3.0.0",
    "com.oracle.jdbc" % "orai18n"                        % "19.3.0.0",
    "org.typelevel"   %% "cats-core"                     % catsCoreVersion,
    jdbc

  )

  val test = Seq(
    "uk.gov.hmrc"            %% s"bootstrap-test$playSuffix" % boostrapVersion % "test, it",
    "org.scalamock"          %% "scalamock"                  % "5.2.0"         % "test, it",
    "org.jsoup"              % "jsoup"                       % "1.15.4"        % Test,
    "com.github.tomakehurst" % "wiremock-jre8"               % "2.33.2"        % "it"
  )
}
