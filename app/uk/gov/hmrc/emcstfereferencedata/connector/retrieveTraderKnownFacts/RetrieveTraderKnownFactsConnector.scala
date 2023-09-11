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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveTraderKnownFacts

import uk.gov.hmrc.emcstfereferencedata.models.response.{TraderKnownFacts, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait RetrieveTraderKnownFactsConnector {
  def retrieveTraderKnownFacts(exciseRegistrationId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Option[TraderKnownFacts]]]
}

object RetrieveTraderKnownFactsConnector {
  private[retrieveTraderKnownFacts] val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getTraderKnownFacts(?, ?)}"
  private[retrieveTraderKnownFacts] val exciseRegistrationIdParameterKey = "pExcise_Registration_Id"
  private[retrieveTraderKnownFacts] val traderKfParameterKey = "pTraderKF"
  private[retrieveTraderKnownFacts] val traderNameKey = "TRADER_NAME"
  private[retrieveTraderKnownFacts] val address1Key = "ADDRESS_LINE1"
  private[retrieveTraderKnownFacts] val address2Key = "ADDRESS_LINE2"
  private[retrieveTraderKnownFacts] val address3Key = "ADDRESS_LINE3"
  private[retrieveTraderKnownFacts] val address4Key = "ADDRESS_LINE4"
  private[retrieveTraderKnownFacts] val address5Key = "ADDRESS_LINE5"
  private[retrieveTraderKnownFacts] val postcodeKey = "POSTCODE"
}