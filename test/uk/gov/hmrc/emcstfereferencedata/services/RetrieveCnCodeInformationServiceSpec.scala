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
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockRetrieveCnCodeInformationConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class RetrieveCnCodeInformationServiceSpec extends UnitSpec with MockRetrieveCnCodeInformationConnector with BaseFixtures {

  object TestService extends RetrieveCnCodeInformationService(mockConnector)

  "The RetrieveCnCodeInformationService" should {
    "return a successful response containing the CnCodeInformation" when {
      "retrieveCnCodeInformation method is called" in {
        val testResponse = Right(Map(testCnCode -> testCnCodeInformation))
        MockConnector.retrieveCnCodeInformation(testProductCode)(testResponse)

        TestService.retrieveCnCodeInformation(testProductCodeList, testCnCodeList) shouldBe testResponse
      }
    }

    "return an Error Response" when {
      "there is no data available" in {
        MockConnector.retrieveCnCodeInformation(testProductCode)(Left(NoDataReturnedFromDatabaseError))

        TestService.retrieveCnCodeInformation(testProductCodeList, testCnCodeList) shouldBe Left(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
