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

class ExciseProductCodeSpec extends UnitSpec {

  val wineExciseProductCode = ExciseProductCode(
    code = "W200",
    description = "Still wine and still fermented beverages other than wine and beer",
    category = "W",
    categoryDescription = "Wine and fermented beverages other than wine and beer"
  )

  "reads" should {
    "read JSON to a model" in {
      Json.obj(
        "code" -> "W200",
        "description" -> "Still wine and still fermented beverages other than wine and beer",
        "category" -> "W",
        "categoryDescription" -> "Wine and fermented beverages other than wine and beer"
      ).as[ExciseProductCode] shouldBe wineExciseProductCode
    }
  }

  "writes" should {
    "read JSON to a model" in {
      Json.toJson(wineExciseProductCode) shouldBe Json.obj(
        "code" -> "W200",
        "description" -> "Still wine and still fermented beverages other than wine and beer",
        "category" -> "W",
        "categoryDescription" -> "Wine and fermented beverages other than wine and beer"
      )
    }

    "replace &lsquo;, &rsquo; and ' with smart quotes" in {
      Json.toJson(wineExciseProductCode.copy(
        description = "This is a &lsquo;test', it's a good 'test&rsquo; and it will be 'tested'",
        categoryDescription = "bacon &amp; eggs"
      )) shouldBe Json.obj(
        "code" -> "W200",
        "description" -> "This is a ‘test’, it’s a good ‘test’ and it will be ‘tested’",
        "category" -> "W",
        "categoryDescription" -> "bacon & eggs"
      )
    }
  }

}
