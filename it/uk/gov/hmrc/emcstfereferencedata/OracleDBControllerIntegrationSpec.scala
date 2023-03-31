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

package uk.gov.hmrc.emcstfereferencedata

import play.api.http.Status
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.support.{IntegrationBaseSpec, TestDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class OracleDBControllerIntegrationSpec extends IntegrationBaseSpec with TestDatabase {

  private trait Test {

    def uri: String = "/oracle/transportMode "

    def request(): WSRequest = {
      buildRequest(uri)
    }
  }

  "Calling the EMCS stub endpoint" should {
    "return a success page" when {
      "all downstream calls are successful" in new Test {
        populateCandeDb().toEither match {
          case Left(_) =>
            fail("Could not populate CANDE DB, see above logs for errors")

          case Right(_) =>

            val response: WSResponse = Await.result(request().get(), 1.minutes)

            response.status shouldBe Status.OK
            response.header("Content-Type") shouldBe Some("application/json")
            response.body should include("Postal consignment")
        }
      }
    }
  }
}
