/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.config

import org.scalamock.handlers.CallHandler
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig

trait MockAppConfig extends MockFactory {
  val mockAppConfig: AppConfig = mock[AppConfig]

  object MockedAppConfig {
    // MTD ID Lookup Config
    def referenceDataBaseUrl: CallHandler[String] = (mockAppConfig.stubUrl _: () => String).expects()
  }

}