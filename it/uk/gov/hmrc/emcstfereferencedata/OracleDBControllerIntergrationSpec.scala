/*
 * Copyright 2023 HM Revenue & Customs
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
