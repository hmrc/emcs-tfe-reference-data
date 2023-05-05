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

import org.scalamock.handlers.CallHandler1
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.connector.RetrieveCnCodeInformationConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}

trait MockRetrieveCnCodeInformationConnector extends MockFactory {
  lazy val mockConnector: RetrieveCnCodeInformationConnector = mock[RetrieveCnCodeInformationConnector]

  object MockConnector {
    def retrieveCnCodeInformation(productCodes: Seq[String])(response: Either[ErrorResponse, Map[String, CnCodeInformation]]): CallHandler1[Seq[String], Either[ErrorResponse, Map[String, CnCodeInformation]]] =
      (mockConnector.retrieveCnCodeInformation(_: Seq[String])).expects(productCodes).returns(response)
  }

}
