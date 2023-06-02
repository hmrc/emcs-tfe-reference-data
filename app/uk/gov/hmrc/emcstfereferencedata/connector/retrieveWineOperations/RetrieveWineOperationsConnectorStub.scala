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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveWineOperations

import play.api.http.Status.OK
import play.api.libs.json.{JsError, JsObject, JsSuccess, Reads}
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpReads, HttpResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RetrieveWineOperationsConnectorStub @Inject()(val http: HttpClient,
                                                    val config: AppConfig
                                                      ) extends RetrieveWineOperationsConnector with BaseConnector {

  type ConnectorOutcome = Map[String, String]

  private val mapReads: Reads[Map[String, String]] = {
    case JsObject(underlying) => JsSuccess(underlying.map {
      case (k, v) => k -> v.as[String]
    }.toMap)
    case other => JsError(s"Cannot parse JSON as Map[String, String]: $other")
  }

  implicit object ReferenceDataReads extends HttpReads[Either[ErrorResponse, ConnectorOutcome]] {
    override def read(method: String, url: String, response: HttpResponse): Either[ErrorResponse, ConnectorOutcome] = {
      response.status match {
        case OK =>
          response.validateJson(mapReads) match {
            case Some(valid) => Right(valid)
            case None =>
              logger.warn(s"[read] Bad JSON response from emcs-tfe-reference-data-stub")
              Left(JsonValidationError)
          }
        case status =>
          logger.warn(s"[read] Unexpected status from emcs-tfe-reference-data-stub: $status")
          Left(UnexpectedDownstreamResponseError)
      }
    }
  }
  def retrieveWineOperations()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, ConnectorOutcome]] = {
    lazy val url: String = s"${config.stubUrl()}/wine-operations"

    http.GET(url)(ReferenceDataReads, hc, ec)
      .recover {
        error =>
          logger.warn(s"[retrieveWineOperations] error retrieving packaging types from stub: $error")
          Left(UnexpectedDownstreamResponseError)
      }
  }

}
