/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models

import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList}
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.{validOtherDataReferenceJson, validOtherDataReferenceListJson, validOtherDataReferenceListModel, validOtherDataReferenceModel1}

class OtherDataReferenceSpec extends WordSpec with Matchers {

  "OtherDataReference" should {
    "read from json" when {
      "the json is complete" in {
        Json.fromJson[OtherDataReference](validOtherDataReferenceJson) shouldBe JsSuccess(validOtherDataReferenceModel1)
      }
    }
    "write to json" when {
      "the model is complete" in {
        Json.toJson(validOtherDataReferenceModel1) shouldBe validOtherDataReferenceJson
      }
    }
  }
}
