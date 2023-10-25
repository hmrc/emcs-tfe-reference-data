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

package uk.gov.hmrc.emcstfereferencedata.fixtures

import uk.gov.hmrc.emcstfereferencedata.models.request.{CnInformationItem, CnInformationRequest}
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, TraderKnownFacts}

trait BaseFixtures {

  val testErn: String = "SomeErn"
  val testCredId: String = "cred1234567891"
  val testInternalId: String = "int1234567891"
  val testCnCode1: String = "24029000"
  val testProductCode1: String = "T400"
  val testCnCodeInformationItem1: CnInformationItem = CnInformationItem(productCode = testProductCode1, cnCode = testCnCode1)
  val testCnCode2: String = "10000000"
  val testProductCode2: String = "S500"
  val testCnCodeInformationItem2: CnInformationItem = CnInformationItem(productCode = testProductCode2, cnCode = testCnCode2)
  val testCnCodeInformationRequest: CnInformationRequest = CnInformationRequest(items = Seq(testCnCodeInformationItem1, testCnCodeInformationItem2))
  val testCnCodeInformation1: CnCodeInformation = CnCodeInformation(
    cnCodeDescription = "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
    exciseProductCode = "T400",
    exciseProductCodeDescription = "Fine-cut tobacco for the rolling of cigarettes",
    unitOfMeasureCode = 1
  )
  val testCnCodeInformation2: CnCodeInformation = CnCodeInformation(
    cnCodeDescription = "Other products containing ethyl alcohol",
    exciseProductCode = "S500",
    exciseProductCodeDescription = "Other products containing ethyl alcohol",
    unitOfMeasureCode = 3
  )
  val testPackagingTypes: Seq[String] = Seq(
    "VP",
    "NE",
    "TO"
  )
  val testPackagingTypesResult: Map[String, String] = Map(
    "NE" -> "Unpacked or unpackaged",
    "TO" -> "Tun",
    "VP" -> "Vacuum-packed"
  )
  val testWineOperations: Seq[String] = Seq(
    "4",
    "11",
    "9"
  )
  val testWineOperationsResult: Map[String, String] = Map(
    "4" -> "The product has been sweetened",
    "11" -> "The product has been partially dealcoholised",
    "9" -> "The product has been made using oak chips"
  )
  val testTraderKnownFactsResult: TraderKnownFacts = TraderKnownFacts(
    traderName = "SEED TRADER 1629",
    addressLine1 = Some("629 High Street"),
    addressLine2 = Some("Any Suburb"),
    addressLine3 = Some("Any Town"),
    addressLine4 = Some("Any County"),
    addressLine5 = Some("UK"),
    postcode = Some("SS1 99AA")
  )
  val memberStatesResult: Map[String, String] = Map(
    "AT" -> "Austria",
    "BE" -> "Belgium",
    "BG" -> "Bulgaria",
    "HR" -> "Croatia",
    "CY" -> "Cyprus",
    "CZ" -> "Czech Republic",
    "DK" -> "Denmark",
    "EE" -> "Estonia",
    "FI" -> "Finland",
    "FR" -> "France",
    "DE" -> "Germany",
    "EL" -> "Greece",
    "HU" -> "Hungary",
    "IE" -> "Ireland",
    "IT" -> "Italy",
    "LV" -> "Latvia",
    "LT" -> "Lithuania",
    "LU" -> "Luxembourg",
    "MT" -> "Malta",
    "NL" -> "Netherlands",
    "PL" -> "Poland",
    "PT" -> "Portugal",
    "RO" -> "Romania",
    "SK" -> "Slovakia",
    "SI" -> "Slovenia",
    "ES" -> "Spain",
    "SE" -> "Sweden",
    "GB" -> "United Kingdom"
  )

  val transportUnitsResult: Map[String, String] = Map(
    "1" -> "Container",
    "2" -> "Vehicle",
    "3" -> "Trailer",
    "4" -> "Tractor",
    "5" -> "Fixed transport installations"
  )

}