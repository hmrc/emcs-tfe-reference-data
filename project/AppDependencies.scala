import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val playSuffix        = s"-play-30"

  val boostrapVersion   = "8.4.0"
  val scalamockVersion  = "5.2.0"
  val catsCoreVersion   = "2.12.0"
  val oraVersion        = "19.3.0.0"
  val jsoupVersion      = "1.18.1"

  val compile = Seq(
    "uk.gov.hmrc"               %% s"bootstrap-backend$playSuffix"    % boostrapVersion,
    "com.oracle.jdbc"           %   "ojdbc8"                          % oraVersion,
    "com.oracle.jdbc"           %   "orai18n"                         % oraVersion,
    "org.typelevel"             %%  "cats-core"                       % catsCoreVersion,
    "org.jsoup"                 %   "jsoup"                           % jsoupVersion,
    jdbc
  )

  val test = Seq(
    "uk.gov.hmrc"               %% s"bootstrap-test$playSuffix"       % boostrapVersion     % "test, it",
    "org.scalamock"             %% "scalamock"                        % "5.2.0"             % "test, it",
    "org.wiremock"              %  "wiremock"                         % "3.3.1"             % "it",
    "org.jsoup"                 % "jsoup"                             % jsoupVersion        % Test
  )
}
