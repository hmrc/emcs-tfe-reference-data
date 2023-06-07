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

package uk.gov.hmrc.emcstfereferencedata.retrieveCnCodeInformation

import play.api.http.Status
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{JsNull, JsObject, JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.{JsonValidationError, UnexpectedDownstreamResponseError}
import uk.gov.hmrc.emcstfereferencedata.stubs.DownstreamStub
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RetrieveCnCodeInformationControllerWithStubIntegrationSpec extends IntegrationBaseSpec {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> false)

  private trait Test {

    private def uri: String = "/oracle/cn-code-information"

    def request(): WSRequest = {
      buildRequest(uri)
    }
  }

  "POST /oracle/cn-code-information (stub)" when {

    "application.conf points the service to the stub" should {

      s"return a success" when {
        s"the stub returns status code OK ($OK) and a body which can be mapped to JSON" in new Test {

          val testRequestJson: JsObject =
            Json.obj(
              "productCodeList" -> Json.arr("T400"),
              "cnCodeList" -> Json.arr("24029000")
            )

          val testResponseJson: JsValue =
            Json.obj(
              "24029000" -> Json.obj(
                "cnCodeDescription" -> "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
                "unitOfMeasureCode" -> 1,
                "exciseProductCodeDescription" -> "Cigars &amp; cigarillos"
              )
            )

          DownstreamStub.onSuccess(DownstreamStub.GET, "/cn-code-information", OK, testResponseJson)

          val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

          response.status shouldBe Status.OK
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe testResponseJson
        }
      }

      s"return a fail" when {
        s"the stub returns status code OK ($OK) and a body which cannot be mapped to JSON" in new Test {

          val testRequestJson: JsObject =
            Json.obj(
              "productCodeList" -> Json.arr("T400"),
              "cnCodeList" -> Json.arr("24029000")
            )

          val testResponseJson: JsValue = JsNull

          DownstreamStub.onSuccess(DownstreamStub.GET, "/cn-code-information", OK, testResponseJson)

          val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(JsonValidationError)
        }
        s"the stub returns status code other than OK ($OK)" in new Test {

          val testRequestJson: JsObject =
            Json.obj(
              "productCodeList" -> Json.arr("T400"),
              "cnCodeList" -> Json.arr("24029000")
            )

          val testResponseJson: JsValue =
            Json.obj(
              "24029000" -> Json.obj(
                "cnCodeDescription" -> "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
                "unitOfMeasureCode" -> 1,
                "exciseProductCodeDescription" -> "Cigars &amp; cigarillos"
              )
            )

          DownstreamStub.onSuccess(DownstreamStub.GET, "/cn-code-information", BAD_REQUEST, testResponseJson)

          val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(UnexpectedDownstreamResponseError)
        }
      }
    }
  }

}
