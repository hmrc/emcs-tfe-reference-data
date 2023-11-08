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
    "GB" -> "United Kingdom",
    "XI" -> "United Kingdom (Northern Ireland)"
  )

  val transportUnitsResult: Map[String, String] = Map(
    "1" -> "Container",
    "2" -> "Vehicle",
    "3" -> "Trailer",
    "4" -> "Tractor",
    "5" -> "Fixed transport installations"
  )

  val typesOfDocumentResult: Map[String, String] = Map(
    "0" -> "Other",
    "1" -> "e-AD",
    "2" -> "SAAD or e-SAD",
    "3" -> "Invoice",
    "4" -> "Delivery note",
    "5" -> "CMR",
    "6" -> "Bill of lading",
    "7" -> "Way Bill",
    "8" -> "Contract",
    "9" -> "Trader's Application",
    "10" -> "Official record",
    "11" -> "Request",
    "12" -> "Answer",
    "13" -> "Fallback documents, Fallback Printout",
    "14" -> "Photo",
    "15" -> "Export Declaration",
    "16" -> "Anticipated Export Record",
    "17" -> "Exit Results",
    "18" -> "SAD (Single Administrative Document)",
    "19" -> "Certificate of independent small producer of alcoholic beverages",
    "A004" -> "Certificate of authenticity Tobacco",
    "C006" -> "Export permit (Council Decision (EU) 2017/37 (OJ L 11))",
    "C014" -> "V I 1 document",
    "C015" -> "V I 2 extract",
    "C017" -> "V I 1 document annotated in compliance with Regulation (EU) No 2018/273, Article 25 (2)",
    "C018" -> "V I 2 extract annotated in compliance with Regulation (EU) No 2018/273, Article 25 (2)",
    "C620" -> "T2LF document",
    "C622" -> "Certificate of customs status",
    "C624" -> "Form 302",
    "C651" -> "Electronic administrative document (e-AD), as referred to in Article 3(1) of Reg. (EC) No 684/2009",
    "C652" -> "Accompanying documents for the carriage of wine products",
    "C654" -> "Authorisation for products intended strictly for medical purposes",
    "C658" -> "Fallback Accompanying Document for movements of excise goods under suspension of excise duty (FAD), as referred to in Article 8(1) Reg. (EC) No 684/2009",
    "C659" -> "Prior written declaration",
    "C664" -> "CN22 declaration according to Article 144 of the Regulation (UCC DA) No 2015/2446",
    "C665" -> "CN23 declaration according to Article 144 of the Regulation (UCC DA) No 2015/2446",
    "C667" -> "Laboratory analysis",
    "N720" -> "Consignment note CIM",
    "N722" -> "Road list - SMGS",
    "N730" -> "Road consignment note",
    "Y040" -> "VAT identification number issued in the Member State of importation for the importer designated or recognised under Article 201 of the VAT Directive as liable for payment of VAT",
    "Y041" -> "VAT identification number of the customer who is liable for the VAT on the intra-Community acquisition of goods in accordance with Article 200 of the VAT Directive",
    "Y042" -> "VAT identification number issued in the Member State of importation for tax representative",
    "Y044" -> "Evidence that the imported goods are intended to be transported or dispatched from the Member State of importation to another Member State",
    "Y946" -> "Goods necessary for the official purposes of diplomatic or consular missions of Member States in the DPRK or international organisations enjoying immunities in accordance with international law, or to the personal effects of their staff (Art 10.3 of Regulation (EU) 2017/1509)"
  )

}
