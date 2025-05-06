import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val playSuffix        = "-play-30"

  val hmrcBootstrapVersion   = "9.11.0"
  val scalamockVersion  = "5.2.0"
  val catsCoreVersion   = "2.13.0"
  val oraVersion        = "19.3.0.0"
  val jsoupVersion      = "1.18.1"

  private val compile = Seq(
    "uk.gov.hmrc"               %% s"bootstrap-backend$playSuffix"    % hmrcBootstrapVersion,
    "com.oracle.jdbc"           %   "ojdbc8"                          % oraVersion,
    "com.oracle.jdbc"           %   "orai18n"                         % oraVersion,
    "org.typelevel"             %%  "cats-core"                       % catsCoreVersion,
    "org.jsoup"                 %   "jsoup"                           % jsoupVersion,
    jdbc
  )

  private val test = Seq(
    "uk.gov.hmrc"               %% s"bootstrap-test$playSuffix"       % hmrcBootstrapVersion,
    "org.scalamock"             %% "scalamock"                        % "5.2.0",
    "org.jsoup"                 % "jsoup"                             % jsoupVersion,
  ).map(_ % Test)

  val it: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% s"bootstrap-test$playSuffix" % hmrcBootstrapVersion % Test
  )

  def apply(): Seq[ModuleID] = compile ++ test

}
