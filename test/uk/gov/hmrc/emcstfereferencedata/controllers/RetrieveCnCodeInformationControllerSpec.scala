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
import uk.gov.hmrc.emcstfereferencedata.controllers.predicates.FakeAuthAction
import uk.gov.hmrc.emcstfereferencedata.fixtures.BaseFixtures
import uk.gov.hmrc.emcstfereferencedata.mocks.services.MockRetrieveCnCodeInformationService
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrieveCnCodeInformationControllerSpec extends UnitSpec with MockRetrieveCnCodeInformationService with BaseFixtures with FakeAuthAction {

  private val fakeRequest = FakeRequest(POST, "/oracle/cn-code-information").withJsonBody(
    Json.obj(
      "productCodeList" -> Json.arr("T400"),
      "cnCodeList" -> Json.arr("24029000")
    ))


  object TestController extends RetrieveCnCodeInformationController(stubControllerComponents(), mockService, FakeSuccessAuthAction)

  "getOtherDataReferenceList" should {
    s"return ${Status.OK} with the retrieved payment details from the charge details" when {
      "the services returns the other reference data" in {
        MockService.retrieveCnCodeInformation(testProductCodeList, testCnCodeList)(Future.successful(Right(Map(testCnCode -> testCnCodeInformation))))

        val result = call(TestController.show, fakeRequest)

        status(result) shouldBe OK
        contentAsJson(result) shouldBe Json.obj(testCnCode -> testCnCodeInformation)
      }
    }

    s"return a server error response" when {
      "the services returns a server error" in {
        MockService.retrieveCnCodeInformation(testProductCodeList, testCnCodeList)(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        val result = call(TestController.show, fakeRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
