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

package test.uk.gov.hmrc.emcstfereferencedata.retrieveAllEPCCodes

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.libs.ws.{WSRequest, WSResponse}
import test.uk.gov.hmrc.emcstfereferencedata.stubs.AuthStub
import test.uk.gov.hmrc.emcstfereferencedata.support.{IntegrationBaseSpec, TestDatabase}
import uk.gov.hmrc.emcstfereferencedata.models.response.ExciseProductCode

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RetrieveAllEPCCodesControllerWithOracleIntegrationSpec extends IntegrationBaseSpec with TestDatabase {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> true)

  private trait Test {
    def setupStubs(): StubMapping

    private def uri: String = "/oracle/epc-codes"

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
    }
  }

  "POST /oracle/epc-codes (oracle)" when {
    "application.conf points the services to Oracle" should {
      populateCandeDb().toEither match {
        case Left(_) =>
          "error populating db" in {
            fail("Could not populate CANDE DB, see above logs for errors")
          }

        case Right(_) =>
          "return OK with JSON containing the CN Codes" in new Test {
            override def setupStubs(): StubMapping = {
              AuthStub.authorised()
            }


            val response: WSResponse = Await.result(request().get(), 1.minutes)

            response.status shouldBe Status.OK
            response.header("Content-Type") shouldBe Some("application/json")
            response.json.as[Seq[ExciseProductCode]].length shouldBe 30

          }

          "return Forbidden" when {
            "user is unauthorised" in new Test {
              override def setupStubs(): StubMapping = {
                AuthStub.unauthorised()
              }


              val response: WSResponse = Await.result(request().get(), 1.minutes)

              response.status shouldBe Status.FORBIDDEN
            }
          }
      }
    }
  }

}
