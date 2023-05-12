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

package uk.gov.hmrc.emcstfereferencedata.retrievePackagingTypes

import play.api.http.Status
import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.fixtures.BaseFixtures
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.{IntegrationBaseSpec, TestDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RetrievePackagingTypesControllerWithOracleIntegrationSpec extends IntegrationBaseSpec with BaseFixtures with TestDatabase {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> true)

  private trait Test {

    private def uri: String = "/oracle/packaging-types"

    def request(): WSRequest = {
      buildRequest(uri)
    }
  }

  "POST /oracle/cn-code-information (oracle)" when {
    "application.conf points the service to Oracle" should {
      populateCandeDb().toEither match {
        case Left(_) =>
          fail("Could not populate CANDE DB, see above logs for errors")

        case Right(_) =>
          "return OK with JSON containing the Unit of Measure and CN Code Description" when {
            "supplied with a list of CN Codes and a list of Product Codes" in new Test {

              val testRequestJson: JsValue = Json.toJson(testPackagingTypes)

              val testResponseJson: JsObject = Json.toJsObject(testPackagingTypesResult)


              val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

              response.status shouldBe Status.OK
              response.header("Content-Type") shouldBe Some("application/json")
              response.body should include(testResponseJson.toString())
            }
          }

          "return Internal Server Error" when {
            "there is no data in the database" in new Test {

              val testRequestJson: JsArray = Json.arr("BEANS")

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
