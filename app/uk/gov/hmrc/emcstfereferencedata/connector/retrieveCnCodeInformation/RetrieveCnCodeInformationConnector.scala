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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveCnCodeInformation

import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveCnCodeInformationConnector {
  def retrieveCnCodeInformation(productCodes: Seq[String])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, CnCodeInformation]]]
}

object RetrieveCnCodeInformationConnector {
  private[connector] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getCNCodes(?, ?, ?, ?, ?)}"
  private[connector] val categoryCodeParameterKey = "pCategory_code"
  private[connector] val productCodeParameterKey = "pProduct_Code"
  private[connector] val cnCodeParameterKey = "pCN_Codes"
  private[connector] val productCodeCountParameterKey = "pProduct_count"
  private[connector] val cnCodeCountParameterKey = "pCN_Codes_Count"
  private[connector] val unitOfMeasureCodeKey = "UNIT_OF_MEASURE_CODE"
  private[connector] val cnCodeKey = "CN_CODE"
  private[connector] val cnCodeDescriptionKey = "CNCODEDESC"
}