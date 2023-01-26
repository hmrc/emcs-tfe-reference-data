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

package uk.gov.hmrc.emcstfereferencedata.connector

import javax.inject.{Inject, Singleton}
import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.models.response.{HelloWorldResponse, OtherDataReferenceList, OtherDataReferenceListErrorModel, OtherDataReferenceListResponseModel}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EMCSStubConnector @Inject()(val http: HttpClient,
                                  val config: AppConfig
                                 ) extends BaseConnector {

  override lazy val logger: Logger = Logger(this.getClass)
  lazy val url: String = s"${config.stubUrl()}/hello-world"

  lazy val getOtherDataReferenceListUrl: String = s"${config.stubUrl()}/otherReferenceDataTransportMode"


  def getMessage()(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[Either[String, HelloWorldResponse]] = {
    http.GET[HttpResponse](url).map {
      response =>
        response.status match {
        case OK => response.validateJson[HelloWorldResponse] match {
          case Some(valid) => Right(valid)
          case None =>
            logger.warn(s"Bad JSON response from reference-data-stub")
            Left("JSON validation error")
        }
        case status =>
          logger.warn(s"Unexpected status from reference-data-stub: $status")
          Left("Unexpected downstream response status")
      }
    }
  }

  def getOtherDataReferenceList()(implicit headerCarrier: HeaderCarrier, ec: ExecutionContext): Future[OtherDataReferenceListResponseModel] = {
    http.GET[HttpResponse](getOtherDataReferenceListUrl).map {
      response =>
        response.status match {
          case OK => response.validateJson[OtherDataReferenceList] match {
            case Some(valid) => valid
            case None =>
              logger.warn(s"Bad JSON response from reference-data-stub")
              OtherDataReferenceListErrorModel(500, "JSON validation error")
          }
          case status =>
            logger.warn(s"Unexpected status from reference-data-stub: $status")
            OtherDataReferenceListErrorModel(status, response.body)
        }
    }
  }

}
