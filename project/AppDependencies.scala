import play.core.PlayVersion
import play.sbt.PlayImport._
import sbt.Keys.libraryDependencies
import sbt._

object AppDependencies {

  val playSuffix = s"-play-28"

  val boostrapVersion  = "7.12.0"
  val scalamockVersion = "5.2.0"

  val compile = Seq(
    "uk.gov.hmrc" %% s"bootstrap-backend$playSuffix" % boostrapVersion
  )

  val test = Seq(
    "uk.gov.hmrc"   %% s"bootstrap-test$playSuffix" % boostrapVersion  % "test, it",
    "org.scalamock" %% "scalamock"                  % scalamockVersion % "test, it"

  )
}
