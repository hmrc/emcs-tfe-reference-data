package uk.gov.hmrc.emcstfereferencedata

import play.api.http.Status
import play.api.libs.json.{JsObject, Json}
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.support.{IntegrationBaseSpec, TestDatabase}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class RetrieveCnCodeInformationControllerIntegrationSpec extends IntegrationBaseSpec with TestDatabase {

  private trait Test {

    private def uri: String = "/oracle/cn-code-information"

    def request(): WSRequest = {
      buildRequest(uri)
    }
  }

  "POST /oracle/cn-code-information" should {
    populateCandeDb().toEither match {
      case Left(_) =>
        fail("Could not populate CANDE DB, see above logs for errors")

      case Right(_) =>
        "return OK with JSON containing the Unit of Measure and CN Code Description" when {
          "supplied with a list of CN Codes and a list of Product Codes" in new Test {

            val testRequestJson: JsObject =
              Json.obj(
                "productCodeList" -> Json.arr("T400"),
                "cnCodeList" -> Json.arr("24029000")
              )

            val testResponseJson: JsObject =
              Json.obj(
                "24029000" -> Json.obj(
                  "cnCodeDescription" -> "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
                  "unitOfMeasureCode" -> 1
                )
              )


            val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

            response.status shouldBe Status.OK
            response.header("Content-Type") shouldBe Some("application/json")
            response.body should include(testResponseJson.toString())
          }
        }

        "return Internal Server Error" when {
          "there is no data in the database" in new Test {
            val testRequestJson: JsObject =
              Json.obj(
                "productCodeList" -> Json.arr("T4000"),
                "cnCodeList" -> Json.arr("24029000")
              )

            val response: WSResponse = Await.result(request().post(testRequestJson), 1.minutes)

            response.status shouldBe Status.INTERNAL_SERVER_ERROR
            response.header("Content-Type") shouldBe Some("application/json")
            response.body should include(NoDataReturnedFromDatabaseError.message)
          }
        }
      }
    }

}
