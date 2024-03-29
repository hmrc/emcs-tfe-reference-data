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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveAllEPCCodes

import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, ExciseProductCode}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveAllEPCCodesConnector {
  def retrieveAllEPCCodes()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Either[ErrorResponse, Seq[ExciseProductCode]]]
}

object RetrieveAllEPCCodesConnector {
  private[retrieveAllEPCCodes] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getAllExciseProductCodes(?, ?)}"
  private[retrieveAllEPCCodes] val epcCodesParameterKey = "pEPC_Codes"
  private[retrieveAllEPCCodes] val epcCodeCountParameterKey = "pEPC_Codes_Count"
  private[retrieveAllEPCCodes] val epcCodeKey = "EPC_CODE"
  private[retrieveAllEPCCodes] val epcCodeDescriptionKey = "EPC_DESCRIPTION"
  private[retrieveAllEPCCodes] val categoryCodeKey = "CATEGORY_CODE"
  private[retrieveAllEPCCodes] val categoryCodeDescriptionKey = "CATEGORY_DESCRIPTION"
}
