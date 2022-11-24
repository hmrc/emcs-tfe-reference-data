/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata

import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockEMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReferenceListErrorModel, OtherDataReferenceListResponseModel}
import uk.gov.hmrc.emcstfereferencedata.services.EMCSStubService
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.validOtherDataReferenceListModel
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global

class EMCSStubServiceSpec extends UnitSpec with MockEMCSStubConnector {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  object TestEMCSStubService extends EMCSStubService(mockConnector)

  "The EMCSStubService" should {

    "getOtherDataReferenceList method is called" when {

       val service = new EMCSStubService(mockConnector)

      "a successful response is returned from the EMCSStubConnector" when {

        "return a correctly formatted OtherDataReferenceListModel" in {
          val resp: OtherDataReferenceListResponseModel = validOtherDataReferenceListModel
          MockConnector.getOtherDataReferenceList(resp)
          service.getOtherDataReferenceList.futureValue shouldBe validOtherDataReferenceListModel
        }
      }

      "an Error Response is returned from the EMCSStubConnector" when {

        "return a correctly formatted OtherDataReferenceListErrorModel model" in {
          MockConnector.getOtherDataReferenceList(OtherDataReferenceListErrorModel(500, "INTERNAL SERVER ERROR"))
          service.getOtherDataReferenceList.futureValue shouldBe OtherDataReferenceListErrorModel(500, "INTERNAL SERVER ERROR")
        }
      }
    }
  }

}
