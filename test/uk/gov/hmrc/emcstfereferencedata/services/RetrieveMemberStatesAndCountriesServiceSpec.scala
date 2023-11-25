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
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.{NoDataReturnedFromDatabaseError, UnexpectedDownstreamResponseError}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrieveMemberStatesAndCountriesServiceSpec extends UnitSpec with MockRetrieveOtherReferenceDataConnector with BaseFixtures {

  object TestService extends RetrieveMemberStatesAndCountriesService(mockConnector)

  "The RetrieveMemberStatesAndCountriesService" should {
    "return a successful response" when {
      "retrieveMemberStates method returns data and retrieveCountries method returns no data" in {
        MockConnector.retrieveMemberStates()(Future.successful(Right(memberStatesResult)))
        MockConnector.retrieveCountries()(Future.successful(Right(Map())))
        await(TestService.get()) shouldBe Right(memberStatesAndCountriesResultNoCountries)
      }
      "retrieveMemberStates method returns no data and retrieveCountries method returns data" in {
        MockConnector.retrieveMemberStates()(Future.successful(Right(Map())))
        MockConnector.retrieveCountries()(Future.successful(Right(countriesResult)))
        await(TestService.get()) shouldBe Right(memberStatesAndCountriesResultNoMemberStates)
      }
      "retrieveMemberStates method returns data and retrieveCountries method returns data" in {
        MockConnector.retrieveMemberStates()(Future.successful(Right(memberStatesResult)))
        MockConnector.retrieveCountries()(Future.successful(Right(countriesResult)))
        await(TestService.get()) shouldBe Right(memberStatesAndCountriesResult)
      }
    }

    "return an Error Response" when {
      "there is no data available" in {
        MockConnector.retrieveMemberStates()(Future.successful(Right(Map())))
        MockConnector.retrieveCountries()(Future.successful(Right(Map())))
        await(TestService.get()) shouldBe Left(NoDataReturnedFromDatabaseError)
      }

      "retrieveCountries returns a left" in {
        MockConnector.retrieveMemberStates()(Future.successful(Right(memberStatesResult)))
        MockConnector.retrieveCountries()(Future.successful(Left(UnexpectedDownstreamResponseError)))
        await(TestService.get()) shouldBe Left(UnexpectedDownstreamResponseError)
      }

      "retrieveMemberStates returns a left" in {
        MockConnector.retrieveMemberStates()(Future.successful(Left(UnexpectedDownstreamResponseError)))
        await(TestService.get()) shouldBe Left(UnexpectedDownstreamResponseError)
      }
    }
  }
}
