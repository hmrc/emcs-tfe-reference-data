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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveAllCNCodes

import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveAllCNCodesConnector {
  def retrieveAllCnCodes(exciseProductCode: String)(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Either[ErrorResponse, Seq[CnCodeInformation]]]
}

object RetrieveAllCNCodesConnector {
  private[retrieveAllCNCodes] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getCNCodes(?, ?, ?, ?, ?)}"
  private[retrieveAllCNCodes] val categoryCodeParameterKey = "pCategory_code"
  private[retrieveAllCNCodes] val productCodeParameterKey = "pProduct_Code"
  private[retrieveAllCNCodes] val cnCodeParameterKey = "pCN_Codes"
  private[retrieveAllCNCodes] val productCodeCountParameterKey = "pProduct_count"
  private[retrieveAllCNCodes] val cnCodeCountParameterKey = "pCN_Codes_Count"
  private[retrieveAllCNCodes] val unitOfMeasureCodeKey = "UNIT_OF_MEASURE_CODE"
  private[retrieveAllCNCodes] val cnCodeKey = "CN_CODE"
  private[retrieveAllCNCodes] val cnCodeDescriptionKey = "CNCODEDESC"
  private[retrieveAllCNCodes] val exciseProductCodeDescriptionKey = "PRODDESC"
}
