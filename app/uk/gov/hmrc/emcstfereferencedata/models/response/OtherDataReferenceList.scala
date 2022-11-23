/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models.response

import play.api.libs.json.{Json, OFormat}

case class OtherDataReferenceList(otherRefdata: List[OtherDataReference]) {

}

object OtherDataReferenceList {
  implicit val format: OFormat[OtherDataReferenceList] = Json.format
}