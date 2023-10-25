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

case class CnCodeInformation(cnCodeDescription: String, exciseProductCode: String, exciseProductCodeDescription: String, unitOfMeasureCode: Int)

object CnCodeInformation {
  implicit val format: OFormat[CnCodeInformation] = Json.format[CnCodeInformation]

  implicit val mapReads: Reads[Map[String, CnCodeInformation]] = {
    case JsObject(underlying) => JsSuccess(underlying.map {
      case (k, v) => k -> v.as[CnCodeInformation]
    }.toMap)
    case other => JsError(s"Cannot parse JSON as Map[String, CnCodeInformation]: $other")
  }
}