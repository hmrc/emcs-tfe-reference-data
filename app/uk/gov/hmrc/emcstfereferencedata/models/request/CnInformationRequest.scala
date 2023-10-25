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

package uk.gov.hmrc.emcstfereferencedata.models.request

import play.api.libs.json.{Json, OFormat}


case class CnInformationRequest(items: Seq[CnInformationItem])

case class CnInformationItem(productCode: String, cnCode: String)

object CnInformationRequest {
  implicit val format: OFormat[CnInformationRequest] = Json.format[CnInformationRequest]
}

object CnInformationItem {
  implicit val format: OFormat[CnInformationItem] = Json.format[CnInformationItem]
}
