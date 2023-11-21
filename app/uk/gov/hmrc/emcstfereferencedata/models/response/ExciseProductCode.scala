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

import play.api.libs.json._
import uk.gov.hmrc.emcstfereferencedata.utils.StringUtils

case class ExciseProductCode(code: String, description: String, category: String, categoryDescription: String)


object ExciseProductCode {
  implicit val reads: Reads[ExciseProductCode] = Json.reads[ExciseProductCode]

  implicit val writes: OWrites[ExciseProductCode] = (o: ExciseProductCode) => Json.obj(
    "code" -> o.code,
    "description" -> StringUtils.removeHtmlEscapedCharactersAndAddSmartQuotes(o.description),
    "category" -> o.category,
    "categoryDescription" -> StringUtils.removeHtmlEscapedCharactersAndAddSmartQuotes(o.categoryDescription)
  )
}
