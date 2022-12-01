/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.stubs.ReferenceDataStub
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.xml.Elem

class EMCSStubControllerIntegrationSpec extends IntegrationBaseSpec {

  private trait Test {
    def setupStubs(): StubMapping

    def uri: String = "/other-reference-data-list"
    def referenceDataStubUri: String = s"/otherReferenceDataTransportMode"

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
    }
  }

  "Calling the EMCS stub endpoint" should {
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
          ReferenceDataStub.onSuccess(ReferenceDataStub.GET, referenceDataStubUri, Status.OK, validOtherDataReferenceListJson)
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
          ReferenceDataStub.onSuccess(ReferenceDataStub.GET, referenceDataStubUri, Status.OK, referenceDataResponseBody)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.INTERNAL_SERVER_ERROR
      }

      "downstream call returns something other than JSON" in new Test {
        val referenceDataResponseBody: Elem = <message>test message</message>

        override def setupStubs(): StubMapping = {
          ReferenceDataStub.onSuccess(ReferenceDataStub.GET, referenceDataStubUri, Status.OK, referenceDataResponseBody)
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
          ReferenceDataStub.onSuccess(ReferenceDataStub.GET, referenceDataStubUri, Status.INTERNAL_SERVER_ERROR, referenceDataResponseBody)
        }

        val response: WSResponse = await(request().get())
        response.status shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
