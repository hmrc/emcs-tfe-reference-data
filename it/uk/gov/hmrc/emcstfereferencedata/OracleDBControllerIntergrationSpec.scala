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

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.xml.Elem

class OracleDBControllerIntergrationSpec extends IntegrationBaseSpec {

  private trait Test {

    def uri: String = "/oracle/transportMode "

    def request(): WSRequest = {
      buildRequest(uri)
    }
  }

  "Calling the EMCS stub endpoint" should {
    "return a success page" when {
      "all downstream calls are successful" in new Test {


        val response: WSResponse = await(request().get())
        response.status shouldBe Status.OK
        response.header("Content-Type") shouldBe Some("application/json")
        response.body should include("Postal consignment")
      }
    }
  }
}
