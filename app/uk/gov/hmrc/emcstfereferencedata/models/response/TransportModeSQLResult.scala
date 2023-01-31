/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models.response


import play.api.libs.json.{Json, OFormat}

case class TransportModeSQLResult (iterator: List[String], containsRows: Boolean)

object TransportModeSQLResult {
  implicit val format: OFormat[TransportModeSQLResult] = Json.format
}