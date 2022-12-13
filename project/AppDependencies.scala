import play.sbt.PlayImport._
import sbt._

object AppDependencies {

  val compile = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-28"  % "7.12.0",
    jdbc,
    "com.oracle.jdbc"         %  "ojdbc8"                     % "19.3.0.0",
    "com.oracle.jdbc"         %  "orai18n"                    % "19.3.0.0"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"     % "7.3.0"             % "test, it",
    "org.scalamock"           %% "scalamock" % "5.2.0"        % "test, it",
    "org.jsoup"               % "jsoup" % "1.15.3"            % Test,
    "com.vladsch.flexmark"    % "flexmark-all"                % "0.36.8"            % "test, it",
    "com.github.tomakehurst"  % "wiremock-jre8"               % "2.33.2"            % "it"
    
  )
}
