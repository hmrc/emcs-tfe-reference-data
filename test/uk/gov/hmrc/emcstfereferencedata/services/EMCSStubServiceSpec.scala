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

package uk.gov.hmrc.emcstfereferencedata.services

import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockEMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReferenceListErrorModel, OtherDataReferenceListResponseModel}
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.validOtherDataReferenceListModel
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class EMCSStubServiceSpec extends UnitSpec with MockEMCSStubConnector {

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
