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

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.http.Status
import play.api.test.Helpers.{OK, contentAsJson, contentAsString, status}
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockOracleDBConnector
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.{clientErrorResponse, serverErrorResponse, validOtherDataReferenceListJson, validOtherDataReferenceListModel}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class OracleDBControllerSpec extends UnitSpec with MockOracleDBConnector {

  private val fakeRequest = FakeRequest("GET", "/other-reference-data-list")
  private val controller = new OracleDBController(Helpers.stubControllerComponents(), mockConnector)

  "getOtherDataReferenceList" should {
    s"return $OK with the retrieved payment details from the charge details" when {
      "the service returns the other reference data" in {
        MockConnector.executeTransportModeOptionList(validOtherDataReferenceListModel)

        val result = controller.show()(fakeRequest)

        status(result) shouldBe OK
        contentAsJson(result) shouldBe validOtherDataReferenceListJson
      }
    }

    s"return a client error response" when {
      "the service returns a client error" in {
        MockConnector.executeTransportModeOptionList(clientErrorResponse)

        val result = controller.show()(fakeRequest)

        status(result) shouldBe clientErrorResponse.status
        contentAsString(result) shouldBe clientErrorResponse.reason
      }
    }

    s"return a server error response" when {
      "the service returns a server error" in {
        MockConnector.executeTransportModeOptionList(serverErrorResponse)

        val result = controller.show()(fakeRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
        contentAsString(result) shouldBe "Failed to retrieve other data reference list"
      }
    }
  }

}

