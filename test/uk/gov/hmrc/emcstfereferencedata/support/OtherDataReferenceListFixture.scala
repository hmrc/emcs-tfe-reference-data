/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.support

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList}

object OtherDataReferenceListFixture {

  val validOtherDataReferenceJson: JsValue = Json.parse(
    """
      |{
      |   "typeName":"TransportMode",
      |   "code":"0",
      |   "description":"Other"
      |}
			|""".stripMargin)

  val validOtherDataReferenceModel1 =
    OtherDataReference(typeName = "TransportMode", code = "0", description = "Other")


  val validOtherDataReferenceModel2 =
    OtherDataReference(typeName = "TransportMode", code = "5", description = "Postal consignment")


  val validOtherDataReferenceListJson: JsValue = Json.parse(
    """
      | {
      |   "otherRefdata":
      |     [
      |       {
      |          "typeName":"TransportMode",
      |          "code":"0",
      |          "description":"Other"
      |       },
      |       {
      |          "typeName":"TransportMode",
      |          "code":"5",
      |          "description":"Postal consignment"
      |       }
      |     ]
      | }
			|""".stripMargin)

  val validOtherDataReferenceListModel = OtherDataReferenceList(
    List(validOtherDataReferenceModel1,validOtherDataReferenceModel2)
  )

}
