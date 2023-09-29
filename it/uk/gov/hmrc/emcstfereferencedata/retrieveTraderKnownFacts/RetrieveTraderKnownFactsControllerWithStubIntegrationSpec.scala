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

package uk.gov.hmrc.emcstfereferencedata.retrieveTraderKnownFacts

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{JsNull, JsObject, JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.fixtures.BaseFixtures
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.{JsonValidationError, UnexpectedDownstreamResponseError}
import uk.gov.hmrc.emcstfereferencedata.stubs.{AuthStub, DownstreamStub}
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.xml.Elem

class RetrieveTraderKnownFactsControllerWithStubIntegrationSpec extends IntegrationBaseSpec with BaseFixtures {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> false)

  private trait Test {
    def setupStubs(): StubMapping

    private def uri: String = "/oracle/trader-known-facts"

    def id = "GBWK000000106"

    private def queryParams: Seq[(String, String)] = Seq("exciseRegistrationId" -> id)

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
        .withQueryStringParameters(queryParams: _*)
    }
  }

  "POST /oracle/trader-known-facts (stub)" when {

    "application.conf points the services to the stub" should {

      s"return a success" when {
        s"the stub returns status code OK ($OK) and a body which can be mapped to JSON" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised(id)
          }
          val testResponseJson: JsObject = Json.toJsObject(testTraderKnownFactsResult)

          DownstreamStub.onSuccess(DownstreamStub.GET, "/trader-known-facts", OK, testResponseJson)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.OK
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe testResponseJson
        }
      }

      s"return a fail" when {
        "user is unauthorised" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.unauthorised()
          }

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.FORBIDDEN
        }
        "user is authorised with the wrong ERN" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised("WrongERN")
          }

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.FORBIDDEN
        }
        s"the stub returns status code OK ($OK) and a body which cannot be mapped to JSON" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised(id)
          }

          val testResponseJson: JsValue =
            JsNull

          DownstreamStub.onSuccess(DownstreamStub.GET, "/trader-known-facts", OK, testResponseJson)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(JsonValidationError)
        }
        s"the stub returns status code OK ($OK) and a body which is not JSON" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised(id)
          }

          val testResponse: Elem = <Message>Success!</Message>

          DownstreamStub.onSuccess(DownstreamStub.GET, "/trader-known-facts", OK, testResponse)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(JsonValidationError)
        }
        s"the stub returns status code other than OK ($OK)" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised(id)
          }

          val testResponseJson: JsObject = Json.toJsObject(testTraderKnownFactsResult)

          DownstreamStub.onSuccess(DownstreamStub.GET, "/trader-known-facts", BAD_REQUEST, testResponseJson)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(UnexpectedDownstreamResponseError)
        }
      }
    }
  }

}
