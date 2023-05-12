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
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockRetrievePackagingTypesConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrievePackagingTypesServiceSpec extends UnitSpec with MockRetrievePackagingTypesConnector with BaseFixtures {

  object TestService extends RetrievePackagingTypesService(mockConnector)

  "The RetrievePackagingTypesService" should {
    "return a successful response containing the PackagingTypes" when {
      "retrievePackagingTypes method is called" in {
        val testResponse = Right(testPackagingTypesResult)
        MockConnector.retrievePackagingTypes()(Future.successful(testResponse))

        await(TestService.retrievePackagingTypes(testPackagingTypes)) shouldBe testResponse
      }
    }

    "return an Error Response" when {
      "there is no data available" in {
        MockConnector.retrievePackagingTypes()(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        await(TestService.retrievePackagingTypes(testPackagingTypes)) shouldBe Left(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
