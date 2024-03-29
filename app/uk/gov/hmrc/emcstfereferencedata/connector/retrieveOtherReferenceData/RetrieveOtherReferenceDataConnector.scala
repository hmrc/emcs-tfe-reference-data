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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveOtherReferenceData

import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveOtherReferenceDataConnector {
  def retrieveOtherReferenceData(typeName: TypeName)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]]

  def retrieveWineOperations()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] =
    retrieveOtherReferenceData(WineOperations)

  def retrieveMemberStates()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] =
    retrieveOtherReferenceData(MemberStates)

  def retrieveCountries()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] =
    retrieveOtherReferenceData(Countries)

  def retrieveTransportUnits()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] =
    retrieveOtherReferenceData(TransportUnits)

  def retrieveTypesOfDocument()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] =
    retrieveOtherReferenceData(TypeOfDocument)
}

object RetrieveOtherReferenceDataConnector {
  private[retrieveOtherReferenceData] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getOtherReferenceData(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}"
  private[retrieveOtherReferenceData] val typeNameParameterKey = "pType_Name"
  private[retrieveOtherReferenceData] val sortByParameterKey = "pSort_By"
  private[retrieveOtherReferenceData] val sortOrderParameterKey = "pSort_Order"
  private[retrieveOtherReferenceData] val startAtParameterKey = "pStart_At"
  private[retrieveOtherReferenceData] val maxRecordsParameterKey = "pMax_Records"
  private[retrieveOtherReferenceData] val descriptionParameterKey = "pDescription"
  private[retrieveOtherReferenceData] val totalCountKey = "pTotal_Count"
  private[retrieveOtherReferenceData] val displayNameKey = "pDisplay_Name"
  private[retrieveOtherReferenceData] val referenceDataKey = "pReference_Data"
  private[retrieveOtherReferenceData] val descriptionListKey = "pDescription_list"
  private[retrieveOtherReferenceData] val codeKey = "CODE"
  private[retrieveOtherReferenceData] val descriptionKey = "DESCRIPTION"
}