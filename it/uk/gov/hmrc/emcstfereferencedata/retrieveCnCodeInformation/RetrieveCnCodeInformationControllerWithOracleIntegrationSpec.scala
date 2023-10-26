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

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.stubs.AuthStub
import uk.gov.hmrc.emcstfereferencedata.support.{IntegrationBaseSpec, TestDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RetrieveCnCodeInformationControllerWithOracleIntegrationSpec extends IntegrationBaseSpec with TestDatabase {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> true)

  private trait Test {
    def setupStubs(): StubMapping

    private def uri: String = "/oracle/cn-code-information"

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
    }
  }

  "POST /oracle/cn-code-information (oracle)" when {
    "application.conf points the services to Oracle" should {
      populateCandeDb().toEither match {
        case Left(_) =>
          fail("Could not populate CANDE DB, see above logs for errors")

        case Right(_) =>
          "return OK with JSON containing the Unit of Measure and CN Code Description" when {
            "searching for S500 with other Product Codes" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.authorised()
              }

              val testRequestJson: JsObject =
                Json.obj(
                  "items" -> Json.arr(
                    Json.obj(
                      "productCode" -> testProductCode1,
                      "cnCode" -> testCnCode1
                    ),
                    Json.obj(
                      "productCode" -> testProductCode2,
                      "cnCode" -> testCnCode2
                    )
                  )
                )

              val testResponseJson: JsObject =
                Json.obj(
                  "24029000" -> Json.obj(
                    "cnCodeDescription" -> "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
                    "exciseProductCode" -> "T400",
                    "exciseProductCodeDescription" -> "Fine-cut tobacco for the rolling of cigarettes",
                    "unitOfMeasureCode" -> 1
                  ),
                  "10000000" -> Json.obj(
                    "cnCodeDescription" -> "Other products containing ethyl alcohol",
                    "exciseProductCode" -> "S500",
                    "exciseProductCodeDescription" -> "Other products containing ethyl alcohol",
                    "unitOfMeasureCode" -> 3
                  )
                )


              val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

              response.status shouldBe Status.OK
              response.header("Content-Type") shouldBe Some("application/json")
              response.json shouldBe testResponseJson
            }
            "searching for S500 by itself" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.authorised()
              }

              val testRequestJson: JsObject =
                Json.obj(
                  "items" -> Json.arr(
                    Json.obj(
                      "productCode" -> testProductCode2,
                      "cnCode" -> testCnCode2
                    )
                  )
                )

              val testResponseJson: JsObject =
                Json.obj(
                  "10000000" -> Json.obj(
                    "cnCodeDescription" -> "Other products containing ethyl alcohol",
                    "exciseProductCode" -> "S500",
                    "exciseProductCodeDescription" -> "Other products containing ethyl alcohol",
                    "unitOfMeasureCode" -> 3
                  )
                )


              val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

              response.status shouldBe Status.OK
              response.header("Content-Type") shouldBe Some("application/json")
              response.json shouldBe testResponseJson
            }
            "searching for Product Codes which aren't S500" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.authorised()
              }

              val testRequestJson: JsObject =
                Json.obj(
                  "items" -> Json.arr(
                    Json.obj(
                      "productCode" -> testProductCode1,
                      "cnCode" -> testCnCode1
                    )
                  )
                )

              val testResponseJson: JsObject =
                Json.obj(
                  "24029000" -> Json.obj(
                    "cnCodeDescription" -> "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
                    "exciseProductCode" -> "T400",
                    "exciseProductCodeDescription" -> "Fine-cut tobacco for the rolling of cigarettes",
                    "unitOfMeasureCode" -> 1
                  )
                )


              val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

              response.status shouldBe Status.OK
              response.header("Content-Type") shouldBe Some("application/json")
              response.json shouldBe testResponseJson
            }
          }

          "return Forbidden" when {
            "user is unauthorised" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.unauthorised()
              }

              val testRequestJson: JsObject =
                Json.obj(
                  "items" -> Json.arr(
                    Json.obj(
                      "productCode" -> testProductCode1,
                      "cnCode" -> testCnCode1
                    ),
                    Json.obj(
                      "productCode" -> testProductCode2,
                      "cnCode" -> testCnCode2
                    )
                  )
                )

              val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

              response.status shouldBe Status.FORBIDDEN
            }
          }

          "return Internal Server Error" when {
            "there is no data in the database" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.authorised()
              }

              val testRequestJson: JsObject =
                Json.obj(
                  "items" -> Json.arr(
                    Json.obj(
                      "productCode" -> "testProductCode1",
                      "cnCode" -> "testCnCode1"
                    ),
                    Json.obj(
                      "productCode" -> testProductCode2,
                      "cnCode" -> testCnCode2
                    )
                  )
                )

              val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

              response.status shouldBe Status.INTERNAL_SERVER_ERROR
              response.header("Content-Type") shouldBe Some("application/json")
              response.body should include(NoDataReturnedFromDatabaseError.message)
            }
          }
      }
    }
  }

}
