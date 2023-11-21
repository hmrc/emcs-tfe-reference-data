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

package uk.gov.hmrc.emcstfereferencedata.retrieveAllEPCCodes

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import play.api.http.Status
import play.api.http.Status.{BAD_REQUEST, OK}
import play.api.libs.json.{JsNull, JsValue, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.{JsonValidationError, UnexpectedDownstreamResponseError}
import uk.gov.hmrc.emcstfereferencedata.stubs.{AuthStub, DownstreamStub}
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.xml.Elem

class RetrieveAllEPCCodesControllerWithStubIntegrationSpec extends IntegrationBaseSpec {

  override def servicesConfig: Map[String, _] = super.servicesConfig + ("feature-switch.use-oracle" -> false)

  private trait Test {
    def setupStubs(): StubMapping

    private def uri: String = "/oracle/epc-codes"

    def request(): WSRequest = {
      setupStubs()
      buildRequest(uri)
    }
  }

  "POST /oracle/epc-codes (stub)" when {

    "application.conf points the services to the stub" should {

      s"return a success" when {
        s"the stub returns status code OK ($OK) and a body which can be mapped to JSON" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
          }


          val testResponseJson: JsValue =
            Json.parse(
              """
                |[
                |  {
                |    "code": "B000",
                |    "description": "Beer",
                |    "category": "B",
                |    "categoryDescription": "Beer"
                |  },
                |  {
                |    "code": "E200",
                |    "description": "Vegetable and animal oils Products falling within CN codes 1507 to 1518, if these are intended for use as heating fuel or motor fuel (Article 20(1)(a))",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E300",
                |    "description": "Mineral oils Products falling within CN codes 2707 10, 2707 20, 2707 30 and 2707 50 (Article 20(1)(b))",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E410",
                |    "description": "Leaded petrol",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E420",
                |    "description": "Unleaded petrol",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E430",
                |    "description": "Gasoil, unmarked",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E440",
                |    "description": "Gasoil, marked",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E450",
                |    "description": "Kerosene, unmarked",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E460",
                |    "description": "Kerosene, marked",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E470",
                |    "description": "Heavy fuel oil",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E480",
                |    "description": "Products falling within CN codes 2710 12 21, 2710 12 25, 2710 19 29 in bulk commercial movements",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E490",
                |    "description": "Products falling within CN codes 2710 12 to 2710 19 68, not specified above",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E500",
                |    "description": "Liquified Petroleum gases (LPG) Products falling within CN codes 2711 (except 2711 11, 2711 21 and 2711 29)",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E600",
                |    "description": "Saturated acyclic hydrocarbons Products falling within CN code 2901 10",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E700",
                |    "description": "Cyclic hydrocarbons Products falling within CN codes 2902 20, 2902 30, 2902 41, 2902 42, 2902 43 and 2902 44",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E800",
                |    "description": "Methanol (methyl alcohol) Products falling within CN code 2905 11 00, which are not of synthetic origin, if these are intended for use as heating fuel or motor fuel",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E910",
                |    "description": "Fatty-acid mono-alkyl esters, containing by volume 96,5 % or more of esters (FAMAE) falling within CN code 3824 90 99",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E920",
                |    "description": "Products falling within CN code 3824 90 99, if these are intended for use as heating fuel or motor fuel – others than Fatty-acid mono-alkyl esters, containing by volume 96,5 % or more of esters (FAMAE)",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "E930",
                |    "description": "Additives falling within CN codes 3811 11, 3811 19 00 and 3811 90 00",
                |    "category": "E",
                |    "categoryDescription": "Energy Products"
                |  },
                |  {
                |    "code": "I000",
                |    "description": "Intermediate products",
                |    "category": "I",
                |    "categoryDescription": "Intermediate products"
                |  },
                |  {
                |    "code": "S200",
                |    "description": "Spirituous beverages",
                |    "category": "S",
                |    "categoryDescription": "Ethyl alcohol and spirits"
                |  },
                |  {
                |    "code": "S300",
                |    "description": "Ethyl alcohol",
                |    "category": "S",
                |    "categoryDescription": "Ethyl alcohol and spirits"
                |  },
                |  {
                |    "code": "S400",
                |    "description": "Partially denatured alcohol",
                |    "category": "S",
                |    "categoryDescription": "Ethyl alcohol and spirits"
                |  },
                |  {
                |    "code": "S500",
                |    "description": "Other products containing ethyl alcohol",
                |    "category": "S",
                |    "categoryDescription": "Ethyl alcohol and spirits"
                |  },
                |  {
                |    "code": "T200",
                |    "description": "Cigarettes",
                |    "category": "T",
                |    "categoryDescription": "Manufactured tobacco products"
                |  },
                |  {
                |    "code": "T300",
                |    "description": "Cigars & cigarillos",
                |    "category": "T",
                |    "categoryDescription": "Manufactured tobacco products"
                |  },
                |  {
                |    "code": "T400",
                |    "description": "Fine-cut tobacco for the rolling of cigarettes",
                |    "category": "T",
                |    "categoryDescription": "Manufactured tobacco products"
                |  },
                |  {
                |    "code": "T500",
                |    "description": "Other smoking tobacco",
                |    "category": "T",
                |    "categoryDescription": "Manufactured tobacco products"
                |  },
                |  {
                |    "code": "W200",
                |    "description": "Still wine and still fermented beverages other than wine and beer",
                |    "category": "W",
                |    "categoryDescription": "Wine and fermented beverages other than wine and beer"
                |  },
                |  {
                |    "code": "W300",
                |    "description": "Sparkling wine and sparkling fermented beverages other than wine and beer",
                |    "category": "W",
                |    "categoryDescription": "Wine and fermented beverages other than wine and beer"
                |  }
                |]""".stripMargin)

          DownstreamStub.onSuccess(DownstreamStub.GET, "/epc-codes", OK, testResponseJson)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.OK
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe testResponseJson
        }
      }

      s"return a fail" when {
        "user is unauthorised" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.unauthorised()
          }

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.FORBIDDEN
        }

        s"the stub returns status code OK ($OK) and a body which cannot be mapped to JSON" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
          }

          val testResponseJson: JsValue = JsNull

          DownstreamStub.onSuccess(DownstreamStub.GET, "/epc-codes", OK, testResponseJson)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(JsonValidationError)
        }
        s"the stub returns status code OK ($OK) and a body which is not JSON" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
          }


          val testResponse: Elem = <Message>Success!</Message>

          DownstreamStub.onSuccess(DownstreamStub.GET, "/epc-codes", OK, testResponse)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(JsonValidationError)
        }
        s"the stub returns status code other than OK ($OK)" in new Test {
          override def setupStubs(): StubMapping = {
            AuthStub.authorised()
          }


          val testResponseJson: JsValue =
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

          DownstreamStub.onSuccess(DownstreamStub.GET, "/epc-codes", BAD_REQUEST, testResponseJson)

          val response: WSResponse = Await.result(request().get(), 1.minutes)

          response.status shouldBe Status.INTERNAL_SERVER_ERROR
          response.header("Content-Type") shouldBe Some("application/json")
          response.json shouldBe Json.toJson(UnexpectedDownstreamResponseError)
        }
      }
    }
  }

}
