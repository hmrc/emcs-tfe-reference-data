/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.connectors

import org.scalamock.handlers.CallHandler0
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.connector.OracleDBConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.OtherDataReferenceListResponseModel
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockOracleDBConnector extends MockFactory {
  lazy val mockConnector: OracleDBConnector = mock[OracleDBConnector]

  object MockConnector {
    def executeTransportModeOptionList(response: OtherDataReferenceListResponseModel): CallHandler0[Future[OtherDataReferenceListResponseModel]] = {
      (() => mockConnector.executeTransportModeOptionList()).expects().returns(Future.successful(response))
    }
    }
}
