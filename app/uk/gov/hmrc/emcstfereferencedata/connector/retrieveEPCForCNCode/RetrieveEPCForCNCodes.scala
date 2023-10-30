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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveEPCForCNCode

import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveEPCForCNCodes {
  def retrieveEPCForCNCode(cnCode: String)(implicit ec: ExecutionContext): Future[Either[ErrorResponse, Seq[CnCodeInformation]]]
}

object RetrieveEPCForCNCodes {
  private[retrieveEPCForCNCode] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getEPCCodesForCNCode(?, ?)}"
  private[retrieveEPCForCNCode] val cnCodeParameterKey = "pCN_Code"
  private[retrieveEPCForCNCode] val cnCodesProductParameterKey = "pCN_Product_Codes"
  private[retrieveEPCForCNCode] val cnCodeKey = "CN_CODE"
  private[retrieveEPCForCNCode] val cnCodeDescriptionKey = "CN_CODE_DESCRIPTION"
  private[retrieveEPCForCNCode] val epcCodeKey = "EPC_PRODUCT_CODE"
  private[retrieveEPCForCNCode] val epcCodeDescriptionKey = "EPC_DESCRIPTION"
}