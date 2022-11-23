/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models.response

import play.api.libs.json.{Json, OFormat}

case class OtherDataReference(typeName: String, code: String, description: String)

object OtherDataReference {
  implicit val format: OFormat[OtherDataReference] = Json.format
}