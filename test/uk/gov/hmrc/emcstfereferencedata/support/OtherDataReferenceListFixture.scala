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

package uk.gov.hmrc.emcstfereferencedata.support

import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList, OtherDataReferenceListErrorModel}

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
    List(validOtherDataReferenceModel1, validOtherDataReferenceModel2)
  )

  val clientErrorResponse = OtherDataReferenceListErrorModel(Status.BAD_REQUEST, "bang")
  val serverErrorResponse = OtherDataReferenceListErrorModel(Status.INTERNAL_SERVER_ERROR, "bang")

}
