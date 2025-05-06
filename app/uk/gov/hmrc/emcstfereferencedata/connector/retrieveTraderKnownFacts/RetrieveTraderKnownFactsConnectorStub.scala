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

import play.api.http.Status.OK
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse._
import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, TraderKnownFacts}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse, StringContextOps}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RetrieveTraderKnownFactsConnectorStub @Inject()(val http: HttpClientV2,
                                                       val config: AppConfig
                                                      ) extends RetrieveTraderKnownFactsConnector with BaseConnector {

  type ConnectorOutcome = Option[TraderKnownFacts]

  implicit object ReferenceDataReads extends HttpReads[Either[ErrorResponse, ConnectorOutcome]] {
    override def read(method: String, url: String, response: HttpResponse): Either[ErrorResponse, ConnectorOutcome] = {
      response.status match {
        case OK =>
          response.validateJson(TraderKnownFacts.fmt) match {
            case valid@Some(_) => Right(valid)
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
  def retrieveTraderKnownFacts(exciseRegistrationId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, ConnectorOutcome]] = {

    logger.info(s"[RetrieveTraderKnownFactsConnectorStub][retrieveTraderKnownFacts] retrieving trader known facts for exciseRegistrationId: $exciseRegistrationId")

    lazy val url: String = s"${config.stubUrl()}/trader-known-facts?exciseRegistrationId=$exciseRegistrationId"

    http
      .get(url"$url")
      .execute[Either[ErrorResponse, ConnectorOutcome]](ReferenceDataReads, ec)
      .recover {
        error =>
          logger.warn(s"[RetrieveTraderKnownFactsConnectorStub][retrieveTraderKnownFacts] error retrieving reference data from stub: $error")
          Left(UnexpectedDownstreamResponseError)
      }
  }

}
