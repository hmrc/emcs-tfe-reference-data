/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.connectors


import org.scalamock.handlers.CallHandler2
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.connector.EMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.HelloWorldResponse
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockEMCSStubConnector extends MockFactory  {
  lazy val mockConnector: EMCSStubConnector = mock[EMCSStubConnector]

  object MockConnector {
    def getMessage(): CallHandler2[HeaderCarrier, ExecutionContext, Future[Either[String, HelloWorldResponse]]] = {
      (mockConnector.getMessage()(_: HeaderCarrier, _: ExecutionContext))
        .expects(*, *)
    }
  }
}


