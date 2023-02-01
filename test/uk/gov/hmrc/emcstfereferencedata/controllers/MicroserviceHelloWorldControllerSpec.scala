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

import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockEMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.HelloWorldResponse
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class MicroserviceHelloWorldControllerSpec extends UnitSpec with MockEMCSStubConnector {

  private val fakeRequest = FakeRequest("GET", "/hello-world")
  private val controller = new MicroserviceHelloWorldController(Helpers.stubControllerComponents(), mockConnector)


  "GET /hello-world" should {
    "return 200" when {
      "service returns a Right" in {

        MockConnector.getMessage().returns(Future.successful(Right(HelloWorldResponse("Success from reference-data-stub"))))

        val result = controller.hello()(fakeRequest)

        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.obj("message" -> "Success from reference-data-stub")
      }
    }
    "return 500" when {
      "service returns a Left" in {

        MockConnector.getMessage().returns(Future.successful(Left("connection Time out")))

        val result = controller.hello()(fakeRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
