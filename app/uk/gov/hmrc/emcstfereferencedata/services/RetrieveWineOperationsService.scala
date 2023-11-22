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

package uk.gov.hmrc.emcstfereferencedata.services

import uk.gov.hmrc.emcstfereferencedata.connector.retrieveOtherReferenceData.RetrieveOtherReferenceDataConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.utils.Logging
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.{Inject, Singleton}
import scala.collection.Map
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RetrieveWineOperationsService @Inject()(connector: RetrieveOtherReferenceDataConnector) extends Logging {

  def retrieveWineOperations()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] = {
    connector.retrieveWineOperations()
      .map {
        case Left(value) => Left(value)
        case Right(value) if value.nonEmpty => Right(value)
        case _ =>
          logger.warn(s"No data returned for all wine operations")
          Left(NoDataReturnedFromDatabaseError)
      }
  }

  def retrieveWineOperations(wineOperationsList: Seq[String])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] = {
    connector.retrieveWineOperations()
      .map(
        _.map {
          _.collect {
            case (key, value) if wineOperationsList.contains(key) => key -> value
          }
        } match {
          case Left(value) => Left(value)
          case Right(value) if value.nonEmpty => Right(value)
          case _ =>
            logger.warn(s"No data returned for input wine operations: $wineOperationsList")
            Left(NoDataReturnedFromDatabaseError)
        }
      )
  }

}
