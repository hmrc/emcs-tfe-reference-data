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
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.fixtures.BaseFixtures
import uk.gov.hmrc.emcstfereferencedata.stubs.AuthStub
import uk.gov.hmrc.emcstfereferencedata.support.{IntegrationBaseSpec, TestDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RetrieveTraderKnownFactsControllerWithOracleIntegrationSpec extends IntegrationBaseSpec with BaseFixtures with TestDatabase {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> true)

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

  "POST /oracle/trader-known-facts (oracle)" when {
    "application.conf points the services to Oracle" should {
      populateCandeDb().toEither match {
        case Left(_) =>
          "error populating db" in {
            fail("Could not populate CANDE DB, see above logs for errors")
          }

        case Right(_) =>
          "return OK with JSON containing the trader known facts" when {
            "supplied with an ERN which belongs to the logged in user" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.authorised(id)
              }

              val testResponseJson: JsObject = Json.toJsObject(testTraderKnownFactsResult)


              val response: WSResponse = Await.result(request().get(), 1.minutes)

              response.status shouldBe Status.OK
              response.header("Content-Type") shouldBe Some("application/json")
              response.body should include(testResponseJson.toString())
            }
          }

          "return Forbidden" when {
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
          }

          "return NoContent" when {
            "there is no data in the database" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.authorised(id)
              }

              override def id: String = "BEANS"

              val response: WSResponse = Await.result(request().get(), 1.minutes)

              response.status shouldBe Status.NO_CONTENT
            }
          }
      }
    }
  }

}
