/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models.response

import play.api.libs.json.{Json, OFormat}

sealed trait OtherDataReferenceListResponseModel


case class OtherDataReferenceListModelErrorModel(status: Int, reason: String) extends OtherDataReferenceListResponseModel


case class OtherDataReferenceList(otherRefdata: List[OtherDataReference]) extends OtherDataReferenceListResponseModel

object OtherDataReferenceList {
  implicit val format: OFormat[OtherDataReferenceList] = Json.format
}

object OtherDataReferenceListModelErrorModel {
  implicit val format: OFormat[OtherDataReferenceListModelErrorModel] = Json.format
}