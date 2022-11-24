/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models

import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.emcstfereferencedata.models.response.OtherDataReferenceList
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.{validOtherDataReferenceListJson, validOtherDataReferenceListModel}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class OtherDataReferenceListSpec extends UnitSpec {

  "OtherDataReferenceSpecList" should {
    "read from json" when {
      "the json is complete" in {
        Json.fromJson[OtherDataReferenceList](validOtherDataReferenceListJson) shouldBe JsSuccess(validOtherDataReferenceListModel)
      }
    }
    "write to json" when {
      "the model is complete" in {
        Json.toJson(validOtherDataReferenceListModel) shouldBe validOtherDataReferenceListJson
      }
    }
  }
}
