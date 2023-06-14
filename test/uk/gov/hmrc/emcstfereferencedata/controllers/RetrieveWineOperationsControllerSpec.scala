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

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.Play.materializer
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.emcstfereferencedata.fixtures.BaseFixtures
import uk.gov.hmrc.emcstfereferencedata.mocks.services.MockRetrieveWineOperationsService
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrieveWineOperationsControllerSpec extends UnitSpec with MockRetrieveWineOperationsService with BaseFixtures {

  private val fakeRequest = FakeRequest(POST, "/oracle/packaging-types").withJsonBody(Json.toJson(testWineOperations))

  object TestController extends RetrieveWineOperationsController(stubControllerComponents(), mockService)

  "getOtherDataReferenceList" should {
    s"return ${Status.OK} with the retrieved payment details from the charge details" when {
      "the services returns the other reference data" in {
        MockService.retrieveWineOperations(testWineOperations)(Future.successful(Right(testWineOperationsResult)))

        val result = call(TestController.show, fakeRequest)

        status(result) shouldBe OK
        contentAsJson(result) shouldBe Json.toJson(testWineOperationsResult)
      }
    }

    s"return a server error response" when {
      "the services returns a server error" in {
        MockService.retrieveWineOperations(testWineOperations)(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        val result = call(TestController.show, fakeRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
