/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{OK, contentAsJson, status}
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockOracleDBConnector
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.{validOtherDataReferenceListJson, validOtherDataReferenceListModel}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

class OracleDBControllerSpec extends UnitSpec with MockOracleDBConnector {
  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  private val fakeRequest = FakeRequest("GET", "/other-reference-data-list")
  private val controller = new OracleDBController(Helpers.stubControllerComponents(), mockConnector)

  "getOtherDataReferenceList" should {
    s"return $OK with the retrieved payment details from the charge details" when {
      "the service returns the other reference data" in {
        MockConnector.executeTransportModeOptionList(validOtherDataReferenceListModel)

        val result = controller.show()(FakeRequest())

        status(result) shouldBe OK
        contentAsJson(result) shouldBe validOtherDataReferenceListJson
      }
    }
  }

}

