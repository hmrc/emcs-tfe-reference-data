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

import uk.gov.hmrc.emcstfereferencedata.fixtures.PackagingTypeFixtures
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockRetrievePackagingTypesConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.collection.immutable.ListMap
import scala.concurrent.Future

class RetrievePackagingTypesServiceSpec extends UnitSpec with MockRetrievePackagingTypesConnector with PackagingTypeFixtures {

  object TestService extends RetrievePackagingTypesService(mockConnector)

  "The RetrievePackagingTypesService" should {
    "return a successful response containing the PackagingTypes" when {
      "retrievePackagingTypes(Seq[String]) method is called" in {
        MockConnector.retrievePackagingTypes()(Future.successful(Right(testPackagingTypesConnectorResult)))

        await(TestService.retrievePackagingTypes(testPackagingTypes)) shouldBe Right(testPackagingTypesServiceResult)
      }

      "retrievePackagingTypes(Option[Boolean]) method is called (ordering by description) - returning only countable" in {
        val testServiceResponse: Map[String, String] = ListMap(
          "TO" -> "Tun",
          "VP" -> "Vacuum-packed"
        )
        MockConnector.retrievePackagingTypes()(Future.successful(Right(testPackagingTypesConnectorResult)))

        await(TestService.retrievePackagingTypes(Some(true))) shouldBe Right(testServiceResponse)
      }

      "retrievePackagingTypes(Option[Boolean]) method is called (ordering by description) - returning non countable" in {
        val testServiceResponse: Map[String, String] = ListMap(
          "NE" -> "Unpacked or unpackaged"
        )
        MockConnector.retrievePackagingTypes()(Future.successful(Right(testPackagingTypesConnectorResult)))

        await(TestService.retrievePackagingTypes(Some(false))) shouldBe Right(testServiceResponse)
      }

      "retrievePackagingTypes(Option[Boolean]) method is called (ordering by description) - returning all" in {
        val testServiceResponse: Map[String, String] = ListMap(
          "TO" -> "Tun",
          "NE" -> "Unpacked or unpackaged",
          "VP" -> "Vacuum-packed"
        )
        MockConnector.retrievePackagingTypes()(Future.successful(Right(testPackagingTypesConnectorResult)))

        await(TestService.retrievePackagingTypes(None)) shouldBe Right(testServiceResponse)
      }
    }

    "return an Error Response" when {
      "there is no data available for retrievePackagingTypes(Seq[String]) method call" in {
        MockConnector.retrievePackagingTypes()(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        await(TestService.retrievePackagingTypes(testPackagingTypes)) shouldBe Left(NoDataReturnedFromDatabaseError)
      }

      "there is no data available for retrievePackagingTypes(Option[Boolean]) method call" in {
        MockConnector.retrievePackagingTypes()(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        await(TestService.retrievePackagingTypes(Some(true))) shouldBe Left(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
