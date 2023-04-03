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

import uk.gov.hmrc.emcstfereferencedata.models.response.CnCodeInformation

trait BaseFixtures {

  val testErn: String = "SomeErn"
  val testCredId: String = "cred1234567891"
  val testInternalId: String = "int1234567891"
  val testCnCode: String = "24029000"
  val testProductCode: String = "T400"
  val testCnCodeList: Seq[String] = Seq(testCnCode)
  val testProductCodeList: Seq[String] = Seq(testProductCode)
  val testCnCodeInformation: CnCodeInformation = CnCodeInformation(
    cnCodeDescription = "Cigars, cheroots, cigarillos and cigarettes not containing tobacco",
    unitOfMeasureCode = 1
  )

}