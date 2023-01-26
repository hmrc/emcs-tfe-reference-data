/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.connectors


import org.scalamock.handlers.CallHandler2
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.connector.EMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{HelloWorldResponse, OtherDataReferenceListResponseModel}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockEMCSStubConnector extends MockFactory  {
  lazy val mockConnector: EMCSStubConnector = mock[EMCSStubConnector]

  object MockConnector {
    def getMessage(): CallHandler2[HeaderCarrier, ExecutionContext, Future[Either[String, HelloWorldResponse]]] = {
      (mockConnector.getMessage()(_: HeaderCarrier, _: ExecutionContext))
        .expects(*, *)
    }

    def getOtherDataReferenceList(response: OtherDataReferenceListResponseModel): CallHandler2[HeaderCarrier, ExecutionContext, Future[OtherDataReferenceListResponseModel]] = {
      (mockConnector.getOtherDataReferenceList()(_: HeaderCarrier, _: ExecutionContext))
        .expects(*, *).returns(Future.successful(response))
    }
}
}
