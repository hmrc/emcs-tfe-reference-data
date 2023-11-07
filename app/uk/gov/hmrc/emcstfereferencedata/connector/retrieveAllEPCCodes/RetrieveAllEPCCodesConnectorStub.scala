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

import play.api.http.Status.OK
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.{JsonValidationError, UnexpectedDownstreamResponseError}
import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, ExciseProductCode}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpReads, HttpResponse}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class RetrieveAllEPCCodesConnectorStub @Inject()(
                                                 http: HttpClient,
                                                val config: AppConfig) extends RetrieveAllEPCCodesConnector with BaseConnector {

  private val responseHttpReads: HttpReads[Either[ErrorResponse, Seq[ExciseProductCode]]] = (_: String, _: String, response: HttpResponse) => {
    response.status match {
      case OK =>
        response.validateJson[Seq[ExciseProductCode]] match {
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

  def retrieveAllEPCCodes()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Either[ErrorResponse, Seq[ExciseProductCode]]] = {
    val url: String = s"${config.stubUrl()}/epc-codes"
    http.GET(url)(responseHttpReads, implicitly, implicitly)
  }


}
