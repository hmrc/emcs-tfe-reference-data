/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers._
import uk.gov.hmrc.emcstfereferencedata.mocks.service.MockEMCSStubService
import uk.gov.hmrc.emcstfereferencedata.models.response.OtherDataReferenceListErrorModel
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.{validOtherDataReferenceListJson, validOtherDataReferenceListModel}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class EMCSStubControllerSpec extends UnitSpec with MockEMCSStubService {


  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  private val fakeRequest = FakeRequest("GET", "/other-reference-data-list")
  private val controller = new EMCSStubController(Helpers.stubControllerComponents(), mockService)


  "getOtherDataReferenceList" should {
    s"return $OK with the retrieved payment details from the charge details" when {
      "the service returns the other reference data" in {
        MockService.getOtherDataReferenceList(validOtherDataReferenceListModel)

        val result = controller.getOtherDataReferenceList()(FakeRequest())

        status(result) shouldBe OK
        contentAsJson(result) shouldBe validOtherDataReferenceListJson
      }
    }

    s"return $NOT_FOUND" when {
      "the service returns an NOT_FOUND error" in {
        val errorJson = """{"code":"NO_DATA_FOUND","reason":"The remote endpoint has indicated that no data can be found."}"""
        MockService.getOtherDataReferenceList(OtherDataReferenceListErrorModel(NOT_FOUND, errorJson))

        val result = controller.getOtherDataReferenceList()(FakeRequest())

        status(result) shouldBe NOT_FOUND
        contentAsString(result) shouldBe errorJson
      }
    }

    s"return $INTERNAL_SERVER_ERROR" when {
      "the service returns an error" in {
        MockService.getOtherDataReferenceList(OtherDataReferenceListErrorModel(INTERNAL_SERVER_ERROR, ""))
        val result = controller.getOtherDataReferenceList()(FakeRequest())

        status(result) shouldBe INTERNAL_SERVER_ERROR
        contentAsString(result) shouldBe "Failed to retrieve other data reference list"
      }
    }
  }
}
