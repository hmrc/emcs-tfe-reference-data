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
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockRetrieveOtherReferenceDataConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrieveWineOperationsServiceSpec extends UnitSpec with MockRetrieveOtherReferenceDataConnector with BaseFixtures {

  object TestService extends RetrieveWineOperationsService(mockConnector)

  "The RetrieveWineOperationsService" should {
    "return a successful response containing the WineOperations" when {
      "retrieveWineOperations(Seq[String]) method is called" in {
        val testResponse = Right(testWineOperationsResult)
        MockConnector.retrieveWineOperations()(Future.successful(testResponse))

        await(TestService.retrieveWineOperations(testWineOperations)) shouldBe testResponse
      }
      "retrieveWineOperations() method is called" in {
        val testResponse = Right(testWineOperationsResult)
        MockConnector.retrieveWineOperations()(Future.successful(testResponse))

        await(TestService.retrieveWineOperations()) shouldBe testResponse
      }
    }

    "return an Error Response" when {
      "there is no data available for retrieveWineOperations(Seq[String]) method call" in {
        MockConnector.retrieveWineOperations()(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        await(TestService.retrieveWineOperations(testWineOperations)) shouldBe Left(NoDataReturnedFromDatabaseError)
      }
      "there is no data available for retrieveWineOperations() method call" in {
        MockConnector.retrieveWineOperations()(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        await(TestService.retrieveWineOperations()) shouldBe Left(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
