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

package uk.gov.hmrc.emcstfereferencedata.connectors


import play.api.http.{HeaderNames, MimeTypes, Status}
import play.api.libs.json.{Json, OFormat}
import uk.gov.hmrc.emcstfereferencedata.connector.EMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.mocks.config.MockAppConfig
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockHttpClient
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList, OtherDataReferenceListErrorModel}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

class EMCSStubConnectorSpec extends UnitSpec with Status with MimeTypes with HeaderNames with MockAppConfig with MockHttpClient {

  trait Test {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

    val connector = new EMCSStubConnector(mockHttpClient, mockAppConfig)

    val baseUrl: String = "http://test-BaseUrl"
    MockedAppConfig.referenceDataBaseUrl.returns(baseUrl)
  }

  "getOtherDataReferenceList" should {
    "return a success model" when {
      "downstream call is successful" in new Test {
        val response: HttpResponse = HttpResponse(
          status = Status.OK,
          json = Json.toJson(
            OtherDataReferenceList(
              List(OtherDataReference("test", "test", "test"))
            )
          ),
          headers = Map.empty
        )

        MockHttpClient.get(s"$baseUrl/otherReferenceDataTransportMode").returns(Future.successful(response))

        await(connector.getOtherDataReferenceList()) shouldBe OtherDataReferenceList(
          List(OtherDataReference("test", "test", "test"))
        )
      }
    }
    "return an error model" when {
      "downstream call is successful but doesn't match expected JSON" in new Test {

        case class TestModel(field: String)

        object TestModel {
          implicit val format: OFormat[TestModel] = Json.format
        }

        val response: HttpResponse = HttpResponse(status = Status.OK, json = Json.toJson(TestModel("test message")), headers = Map.empty)

        MockHttpClient.get(s"$baseUrl/otherReferenceDataTransportMode").returns(Future.successful(response))

        await(connector.getOtherDataReferenceList()) shouldBe OtherDataReferenceListErrorModel(500, "JSON validation error")
      }
      "downstream call is unsuccessful" in new Test {

        MockHttpClient.get(s"$baseUrl/otherReferenceDataTransportMode").returns(Future.successful(HttpResponse(Status.INTERNAL_SERVER_ERROR, body = "test message")))

        await(connector.getOtherDataReferenceList()) shouldBe OtherDataReferenceListErrorModel(500, "test message")
      }
    }
  }
}