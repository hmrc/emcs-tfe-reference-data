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

package uk.gov.hmrc.emcstfereferencedata.services

import uk.gov.hmrc.emcstfereferencedata.fixtures.BaseFixtures
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.{MockRetrieveCnCodeInformationConnector, MockRetrieveProductCodesConnector}
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrieveCnCodeInformationServiceSpec
  extends UnitSpec
    with MockRetrieveCnCodeInformationConnector
    with MockRetrieveProductCodesConnector
    with BaseFixtures {

  object TestService extends RetrieveCnCodeInformationService(mockCnCodeInformationConnector, mockProductCodesConnector)

  "The RetrieveCnCodeInformationService" should {
    "return a successful response containing the CnCodeInformation" when {
      "retrieveCnCodeInformation method is called" in {
        val testResponse1 = Right(Map(testCnCode1 -> testCnCodeInformation1))
        val testResponse2 = Right(Map(testCnCode2 -> testCnCodeInformation2))
        MockCnCodeInformationConnector.retrieveCnCodeInformation(testCnCodeInformationRequest.copy(items = Seq(testCnCodeInformationItem1)))(Future.successful(testResponse1))
        MockProductCodesConnector.retrieveProductCodes(testCnCodeInformationRequest.copy(items = Seq(testCnCodeInformationItem2)))(Future.successful(testResponse2))

        await(TestService.retrieveCnCodeInformation(testCnCodeInformationRequest)) shouldBe Right(Map(testCnCode1 -> testCnCodeInformation1, testCnCode2 -> testCnCodeInformation2))
      }
    }

    "return an Error Response" when {
      "there is no data available" in {
        MockCnCodeInformationConnector.retrieveCnCodeInformation(testCnCodeInformationRequest.copy(items = Seq(testCnCodeInformationItem1)))(Future.successful(Left(NoDataReturnedFromDatabaseError)))
        MockProductCodesConnector.retrieveProductCodes(testCnCodeInformationRequest.copy(items = Seq(testCnCodeInformationItem2)))(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        await(TestService.retrieveCnCodeInformation(testCnCodeInformationRequest)) shouldBe Left(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
