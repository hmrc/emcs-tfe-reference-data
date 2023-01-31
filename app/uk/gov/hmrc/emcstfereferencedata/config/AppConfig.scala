/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

import scala.concurrent.duration.FiniteDuration

@Singleton
class AppConfig @Inject()(servicesConfig :ServicesConfig, config: Configuration) {

  def stubUrl(): String = servicesConfig.baseUrl("emcs-tfe-reference-data-stub")

  def appLogging: Boolean = config.get[Boolean]("settings.appLogging")
  def transactionTimeout: FiniteDuration = config.get[FiniteDuration]("settings.transactionTimeout")
}
