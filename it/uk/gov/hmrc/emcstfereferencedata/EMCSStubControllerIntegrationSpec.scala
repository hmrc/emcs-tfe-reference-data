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
import uk.gov.hmrc.emcstfereferencedata.stubs.{AuthStub, DownstreamStub}
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.xml.Elem

class EMCSStubControllerIntegrationSpec extends IntegrationBaseSpec {

  private trait Test {
    def setupStubs(): StubMapping

    def uri: String = "/other-reference-data-list"

    def DownstreamStubUri: String = s"/otherReferenceDataTransportMode"

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
    }
  }

  "Calling the EMCS stub endpoint" when {
    "request is unauthorised" must {
      "return Forbidden" in new Test {

        override def setupStubs(): StubMapping = {
          AuthStub.unauthorised()
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.FORBIDDEN
      }
    }

    "request is authorised" when {
      "return a success page" when {
        "all downstream calls are successful" in new Test {

          val validOtherDataReferenceListJson: JsValue = Json.parse(
            """
              | {
              |   "otherRefdata":
              |     [
              |       {
              |          "typeName":"TransportMode",
              |          "code":"0",
              |          "description":"Other"
              |       },
              |       {
              |          "typeName":"TransportMode",
              |          "code":"5",
              |          "description":"Postal consignment"
              |       }
              |     ]
              | }
			      |""".stripMargin)

          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
            DownstreamStub.onSuccess(DownstreamStub.GET, DownstreamStubUri, Status.OK, validOtherDataReferenceListJson)
          }

          val response: WSResponse = await(request().get())
          response.status shouldBe Status.OK
          response.header("Content-Type") shouldBe Some("application/json")
          response.body should include("Postal consignment")
        }
      }
      "return an error page" when {
        "downstream call returns unexpected JSON" in new Test {
          val referenceDataResponseBody: JsValue = Json.parse(
            s"""
               |{
               |   "field": "test message"
               |}
               |""".stripMargin
          )

          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
            DownstreamStub.onSuccess(DownstreamStub.GET, DownstreamStubUri, Status.OK, referenceDataResponseBody)
          }

          val response: WSResponse = await(request().get())
          response.status shouldBe Status.INTERNAL_SERVER_ERROR
        }

        "downstream call returns something other than JSON" in new Test {
          val referenceDataResponseBody: Elem = <message>test message</message>

          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
            DownstreamStub.onSuccess(DownstreamStub.GET, DownstreamStubUri, Status.OK, referenceDataResponseBody)
          }

          val response: WSResponse = await(request().get())
          response.status shouldBe Status.INTERNAL_SERVER_ERROR
        }
        "downstream call returns a non-200 HTTP response" in new Test {
          val referenceDataResponseBody: JsValue = Json.parse(
            s"""
               |{
               |   "message": "test message"
               |}
               |""".stripMargin
          )

          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
            DownstreamStub.onSuccess(DownstreamStub.GET, DownstreamStubUri, Status.INTERNAL_SERVER_ERROR, referenceDataResponseBody)
          }

          val response: WSResponse = await(request().get())
          response.status shouldBe Status.INTERNAL_SERVER_ERROR
        }
      }
    }
  }
}
