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
import uk.gov.hmrc.emcstfereferencedata.fixtures.PackagingTypeFixtures
import uk.gov.hmrc.emcstfereferencedata.mocks.services.MockRetrievePackagingTypesService
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

import scala.concurrent.Future

class RetrievePackagingTypesControllerSpec extends UnitSpec with MockRetrievePackagingTypesService with PackagingTypeFixtures with FakeAuthAction {

  private val fakePostRequest = FakeRequest(POST, "/oracle/packaging-types").withJsonBody(Json.toJson(testPackagingTypes))
  private val fakeGetRequest = FakeRequest(GET, "/oracle/packaging-types")

  object TestController extends RetrievePackagingTypesController(stubControllerComponents(), mockService, FakeSuccessAuthAction)

  "show" should {
    s"return ${Status.OK} with the retrieved packaging types" when {
      "the services returns the other reference data" in {
        MockService.retrievePackagingTypes(testPackagingTypes)(Future.successful(Right(testPackagingTypesServiceResult)))

        val result = call(TestController.show, fakePostRequest)

        status(result) shouldBe OK
        contentAsJson(result) shouldBe Json.toJson(testPackagingTypesServiceResult)
      }
    }

    s"return a server error response" when {
      "the services returns a server error" in {
        MockService.retrievePackagingTypes(testPackagingTypes)(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        val result = call(TestController.show, fakePostRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(NoDataReturnedFromDatabaseError)
      }
    }
  }

  "showAllPackagingTypes" should {
    s"return ${Status.OK} with all the packaging types" when {
      "the services returns the other reference data" in {
        MockService.retrievePackagingTypes(None)(Future.successful(Right(testPackagingTypesServiceResult)))

        val result = call(TestController.showAllPackagingTypes(None), fakeGetRequest)

        status(result) shouldBe OK
        contentAsJson(result) shouldBe Json.toJson(testPackagingTypesServiceResultOrdered)
      }
    }

    s"return a server error response" when {
      "the services returns a server error" in {
        MockService.retrievePackagingTypes(None)(Future.successful(Left(NoDataReturnedFromDatabaseError)))

        val result = call(TestController.showAllPackagingTypes(None), fakeGetRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        contentAsJson(result) shouldBe Json.toJson(NoDataReturnedFromDatabaseError)
      }
    }
  }

}
