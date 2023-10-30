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

import uk.gov.hmrc.emcstfereferencedata.models.request.CnInformationRequest
import uk.gov.hmrc.emcstfereferencedata.models.response.{CNCode, CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveAllCNCodes {
  def retrieveAllCnCodes()(implicit ec: ExecutionContext): Future[Either[ErrorResponse, Seq[CNCode]]]
}

object RetrieveAllCNCodes {
  private[retrieveAllCNCodes] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getAllCNCodes(?, ?)}"
  private[retrieveAllCNCodes] val cnCodesParameterKey = "pCN_Codes"
  private[retrieveAllCNCodes] val cnCodeCountParameterKey = "pCN_Codes_Count"
  private[retrieveAllCNCodes] val cnCodeKey = "CN_CODE"
  private[retrieveAllCNCodes] val cnCodeDescriptionKey = "CNCODEDESC"
}