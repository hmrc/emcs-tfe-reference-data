/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.connector

import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.models.response.HelloWorldResponse
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpResponse}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class EMCSStubConnector @Inject()(val http: HttpClient,
                                  val config: AppConfig
                                 ) extends BaseConnector {

  override lazy val logger: Logger = Logger(this.getClass)
  lazy val url: String = s"${config.stubUrl}/hello-world"

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

}
