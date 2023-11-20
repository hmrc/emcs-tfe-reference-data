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

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.emcstfereferencedata.models.response.PackagingType

trait PackagingTypeFixtures {
  val testPackagingTypes: Seq[String] = Seq(
    "VP",
    "NE",
    "TO"
  )
  val testPackagingTypesConnectorResult: Map[String, PackagingType] = Map(
    "VP" -> PackagingType("VP", "Vacuum-packed", true),
    "NE" -> PackagingType("NE", "Unpacked or unpackaged", false),
    "TO" -> PackagingType("TO", "Tun", true)
  )
  val testPackagingTypesServiceResult: Map[String, String] = Map(
    "NE" -> "Unpacked or unpackaged",
    "TO" -> "Tun",
    "VP" -> "Vacuum-packed"
  )
  val testPackagingTypesServiceResultOrdered: Map[String, String] = Map(
    "TO" -> "Tun",
    "NE" -> "Unpacked or unpackaged",
    "VP" -> "Vacuum-packed"
  )
  val allPackagingTypesServiceResultOrderedJson: JsValue = Json.parse(
    """
      |{
      |    "AE": "Aerosol",
      |    "AM": "Ampoule, non protected",
      |    "AP": "Ampoule, protected",
      |    "AT": "Atomizer",
      |    "BG": "Bag",
      |    "BL": "Bale, compressed",
      |    "BN": "Bale, non-compressed",
      |    "BF": "Balloon, non-protected",
      |    "BP": "Balloon, protected",
      |    "BR": "Bar",
      |    "BA": "Barrel",
      |    "BZ": "Bars, in bundle/bunch/truss",
      |    "BK": "Basket",
      |    "CB": "Beer crate",
      |    "BI": "Bin",
      |    "BD": "Board",
      |    "BY": "Board, in bundle/bunch/truss",
      |    "BB": "Bobbin",
      |    "BT": "Bolt",
      |    "BS": "Bottle, non-protected, bulbous",
      |    "BO": "Bottle, non-protected, cylindrical",
      |    "BV": "Bottle, protected bulbous",
      |    "BQ": "Bottle, protected cylindrical",
      |    "BC": "Bottlecrate, bottlerack",
      |    "BX": "Box",
      |    "BJ": "Bucket",
      |    "VG": "Bulk, gas (at 1031 mbar and 15&deg;C)",
      |    "VQ": "Bulk, liquefied gas(abn.temp/press)",
      |    "VL": "Bulk, liquid",
      |    "VY": "Bulk, solid, fine (powders)",
      |    "VR": "Bulk, solid, granular (grains)",
      |    "VO": "Bulk, solid, large (nodules)",
      |    "BH": "Bunch",
      |    "BE": "Bundle",
      |    "BU": "Butt",
      |    "CG": "Cage",
      |    "CX": "Can, cylindrical",
      |    "CA": "Can, rectangular",
      |    "CI": "Canister",
      |    "CZ": "Canvas",
      |    "CO": "Carboy, non-protected",
      |    "CP": "Carboy, protected",
      |    "CT": "Carton",
      |    "CS": "Case",
      |    "CK": "Cask",
      |    "CH": "Chest",
      |    "CC": "Churn",
      |    "CF": "Coffer",
      |    "CJ": "Coffin",
      |    "CL": "Coil",
      |    "CV": "Cover",
      |    "CR": "Crate",
      |    "CE": "Creel",
      |    "CU": "Cup",
      |    "CY": "Cylinder",
      |    "DJ": "Demijohn, non-protected",
      |    "DP": "Demijohn, protected",
      |    "DR": "Drum",
      |    "EN": "Envelope",
      |    "FP": "Filmpack",
      |    "FI": "Firkin",
      |    "FL": "Flask",
      |    "FO": "Footlocker",
      |    "FR": "Frame",
      |    "FD": "Framed crate",
      |    "FC": "Fruit crate",
      |    "GB": "Gas bottle",
      |    "GI": "Girder",
      |    "GZ": "Girders, in bundle/bunch/truss",
      |    "HR": "Hamper",
      |    "HG": "Hogshead",
      |    "IN": "Ingot",
      |    "IZ": "Ingots, in bundle/bunch/truss",
      |    "JR": "Jar",
      |    "JY": "Jerrican, cylindrical",
      |    "JC": "Jerrican, rectangular",
      |    "JG": "Jug",
      |    "JT": "Jutebag",
      |    "KG": "Keg",
      |    "LG": "Log",
      |    "LZ": "Logs, in bundle/ bunch/truss",
      |    "MT": "Mat",
      |    "MX": "Match box",
      |    "MC": "Milk crate",
      |    "MB": "Multiply bag",
      |    "MS": "Multiwall sack",
      |    "NS": "Nest",
      |    "NT": "Net",
      |    "PK": "Package",
      |    "PA": "Packet",
      |    "PL": "Pail",
      |    "PX": "Pallet",
      |    "PC": "Parcel",
      |    "PI": "Pipe",
      |    "PZ": "Pipes/Planks, in bundle/bunch/truss",
      |    "PH": "Pitcher",
      |    "PN": "Plank",
      |    "PG": "Plate",
      |    "PY": "Plates, in bundle/bunch/truss",
      |    "PT": "Pot",
      |    "PO": "Pouch",
      |    "RT": "Rednet",
      |    "RL": "Reel",
      |    "RG": "Ring",
      |    "RD": "Rod",
      |    "RZ": "Rods, in bundle/bunch/truss",
      |    "RO": "Roll",
      |    "SH": "Sachet",
      |    "SA": "Sack",
      |    "SE": "Sea-chest",
      |    "SC": "Shallow crate",
      |    "ST": "Sheet",
      |    "SM": "Sheetmetal",
      |    "SZ": "Sheets, in bundle/bunch/truss",
      |    "SW": "Shrinkwrapped",
      |    "SK": "Skeleton case",
      |    "SL": "Slipsheet",
      |    "SD": "Spindle",
      |    "SU": "Suitcase",
      |    "TY": "Tank, cylindrical",
      |    "TK": "Tank, rectangular",
      |    "TC": "Tea-chest",
      |    "TN": "Tin",
      |    "PU": "Tray/Tray pack",
      |    "TR": "Trunk",
      |    "TS": "Truss",
      |    "TB": "Tub",
      |    "TU": "Tube",
      |    "TD": "Tube, collapsible/Collapsible tube",
      |    "TZ": "Tubes, in bundle/ bunch/truss",
      |    "TO": "Tun",
      |    "NE": "Unpacked or unpackaged",
      |    "VP": "Vacuum-packed",
      |    "VA": "Vat",
      |    "VI": "Vial",
      |    "WB": "Wickerbottle"
      |}
      |""".stripMargin)
}
