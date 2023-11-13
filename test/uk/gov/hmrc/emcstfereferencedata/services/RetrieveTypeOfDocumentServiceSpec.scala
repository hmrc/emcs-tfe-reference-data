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
import uk.gov.hmrc.emcstfereferencedata.models.response.TypeOfDocument
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrieveTypeOfDocumentServiceSpec extends UnitSpec with MockRetrieveOtherReferenceDataConnector with BaseFixtures {

  object TestService extends RetrieveTypeOfDocumentService(mockConnector)

  "The RetrieveTypeOfDocumentService" should {
    "return a successful response containing the types of document" when {
      "retrieveTypeOfDocument method is called" in {
        MockConnector.retrieveTypesOfDocument()(Future.successful(Right(typesOfDocumentResult)))
        await(TestService.retrieveTypesOfDocument()) shouldBe Right(TypeOfDocument(typesOfDocumentResult))
      }
    }

    "return an Error Response" when {
      "there is no data available" in {
        MockConnector.retrieveTypesOfDocument()(Future.successful(Right(Map())))
        await(TestService.retrieveTypesOfDocument()) shouldBe Left(NoDataReturnedFromDatabaseError)
      }

      "connector returns a left" in {
        MockConnector.retrieveTypesOfDocument()(Future.successful(Left(UnexpectedDownstreamResponseError)))
        await(TestService.retrieveTypesOfDocument()) shouldBe Left(UnexpectedDownstreamResponseError)
      }
    }
  }
}
