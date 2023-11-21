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

package uk.gov.hmrc.emcstfereferencedata.models.response

import play.api.libs.json.Json
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class CnCodeInformationSpec extends UnitSpec {

  val testCommodityCodeTobacco: CnCodeInformation = CnCodeInformation(
    cnCode = "24029000",
    cnCodeDescription = "Cigarettes containing tobacco / other",
    exciseProductCode = "T400",
    exciseProductCodeDescription = "Cigarettes",
    unitOfMeasureCode = 1
  )

  "reads" should {
    "read JSON to a model" in {
      Json.obj(
        "cnCode" -> "24029000",
        "cnCodeDescription" -> "Cigarettes containing tobacco / other",
        "exciseProductCode" -> "T400",
        "exciseProductCodeDescription" -> "Cigarettes",
        "unitOfMeasureCode" -> 1
      ).as[CnCodeInformation] shouldBe testCommodityCodeTobacco
    }
  }

  "mapReads" should {
    "read JSON to a model" in {
      Json.obj(
        "24029000" -> Json.obj(
          "cnCode" -> "24029000",
          "cnCodeDescription" -> "Cigarettes containing tobacco / other",
          "exciseProductCode" -> "T400",
          "exciseProductCodeDescription" -> "Cigarettes",
          "unitOfMeasureCode" -> 1
        )
      ).as[Map[String, CnCodeInformation]] shouldBe Map("24029000" -> testCommodityCodeTobacco)
    }
  }

  "writes" should {
    "read JSON to a model" in {
      Json.toJson(testCommodityCodeTobacco) shouldBe Json.obj(
        "cnCode" -> "24029000",
        "cnCodeDescription" -> "Cigarettes containing tobacco / other",
        "exciseProductCode" -> "T400",
        "exciseProductCodeDescription" -> "Cigarettes",
        "unitOfMeasureCode" -> 1
      )
    }

    "replace &lsquo;, &rsquo; and ' with smart quotes" in {
      Json.toJson(testCommodityCodeTobacco.copy(
        exciseProductCodeDescription = "bacon &amp; eggs",
        cnCodeDescription = "This is a &lsquo;test', it's a good 'test&rsquo; and it will be 'tested'"
      )) shouldBe Json.obj(
        "cnCode" -> "24029000",
        "cnCodeDescription" -> "This is a ‘test’, it’s a good ‘test’ and it will be ‘tested’",
        "exciseProductCode" -> "T400",
        "exciseProductCodeDescription" -> "bacon & eggs",
        "unitOfMeasureCode" -> 1
      )
    }
  }

}
