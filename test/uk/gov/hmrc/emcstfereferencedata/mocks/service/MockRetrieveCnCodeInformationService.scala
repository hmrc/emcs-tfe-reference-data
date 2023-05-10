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

package uk.gov.hmrc.emcstfereferencedata.mocks.service

import org.scalamock.handlers.CallHandler4
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.emcstfereferencedata.services.RetrieveCnCodeInformationService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockRetrieveCnCodeInformationService extends MockFactory {
  lazy val mockService: RetrieveCnCodeInformationService = mock[RetrieveCnCodeInformationService]

  object MockService {
    def retrieveCnCodeInformation(productCodeList: Seq[String], cnCodeList: Seq[String])(response: Future[Either[ErrorResponse, Map[String, CnCodeInformation]]]): CallHandler4[Seq[String], Seq[String], HeaderCarrier, ExecutionContext, Future[Either[ErrorResponse, collection.Map[String, CnCodeInformation]]]] =
      (mockService.retrieveCnCodeInformation(_: Seq[String], _: Seq[String])(_: HeaderCarrier, _: ExecutionContext)).expects(productCodeList, cnCodeList, *, *).returns(response)
  }

}
