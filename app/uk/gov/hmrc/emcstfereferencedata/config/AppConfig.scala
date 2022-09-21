/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig

@Singleton
class AppConfig @Inject()(servicesConfig :ServicesConfig, config: Configuration) {

  def stubUrl(): String = s"${servicesConfig.baseUrl("emcs-tfe-reference-data-stub")}/emcs-tfe-reference-data-stub"
}
