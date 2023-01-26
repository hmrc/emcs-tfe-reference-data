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

package uk.gov.hmrc.emcstfereferencedata.models

import play.api.libs.json.{JsSuccess, Json}
import uk.gov.hmrc.emcstfereferencedata.models.response.OtherDataReference
import uk.gov.hmrc.emcstfereferencedata.support.OtherDataReferenceListFixture.{validOtherDataReferenceJson, validOtherDataReferenceModel1}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class OtherDataReferenceSpec extends UnitSpec {

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
