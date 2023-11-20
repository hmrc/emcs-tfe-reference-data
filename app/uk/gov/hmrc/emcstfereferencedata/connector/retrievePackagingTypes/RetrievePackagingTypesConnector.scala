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

package uk.gov.hmrc.emcstfereferencedata.connector.retrievePackagingTypes

import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, PackagingType}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrievePackagingTypesConnector {
  def retrievePackagingTypes()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, PackagingType]]]
}

object RetrievePackagingTypesConnector {
  private[retrievePackagingTypes] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getOtherReferenceData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
  private[retrievePackagingTypes] val typeNameParameterKey = "pType_Name"
  private[retrievePackagingTypes] val typeNameParameterValue = "PackagingCode"
  private[retrievePackagingTypes] val sortByParameterKey = "pSort_By"
  private[retrievePackagingTypes] val sortOrderParameterKey = "pSort_Order"
  private[retrievePackagingTypes] val startAtParameterKey = "pStart_At"
  private[retrievePackagingTypes] val maxRecordsParameterKey = "pMax_Records"
  private[retrievePackagingTypes] val descriptionParameterKey = "pDescription"
  private[retrievePackagingTypes] val totalCountKey = "pTotal_Count"
  private[retrievePackagingTypes] val displayNameKey = "pDisplay_Name"
  private[retrievePackagingTypes] val referenceDataKey = "pReference_Data"
  private[retrievePackagingTypes] val descriptionListKey = "pDescription_list"
  private[retrievePackagingTypes] val codeKey = "CODE"
  private[retrievePackagingTypes] val descriptionKey = "DESCRIPTION"
  private[retrievePackagingTypes] val countableKey = "COUNTABLE"
}