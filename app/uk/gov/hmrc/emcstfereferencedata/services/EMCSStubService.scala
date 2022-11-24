/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.services

import play.api.Logger
import uk.gov.hmrc.emcstfereferencedata.connector.EMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReferenceList, OtherDataReferenceListErrorModel, OtherDataReferenceListResponseModel}
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.{Inject, Singleton}
import scala.concurrent.{Future, ExecutionContext}
@Singleton
class EMCSStubService @Inject()(connector: EMCSStubConnector)(implicit ec: ExecutionContext) {
  lazy val logger: Logger = Logger(this.getClass)

  def getOtherDataReferenceList(implicit headerCarrier: HeaderCarrier): Future[OtherDataReferenceListResponseModel] = {
    logger.debug("[getBusinessDetailsService][getBusinessDetails] - Requesting Income Source Details from Connector")
    connector.getOtherDataReferenceList().map {
      case success: OtherDataReferenceList =>
        logger.debug(s"[getOtherDataReferenceList][getOtherDataReferenceList] - Retrieved Other Data Reference List:\n\n$success")
        success
      case error: OtherDataReferenceListErrorModel =>
        logger.error(s"[getOtherDataReferenceList][getOtherDataReferenceList] - Retrieved Other Data Reference List:\n\n$error")
        OtherDataReferenceListErrorModel(error.status, error.reason)
    }
  }

}
