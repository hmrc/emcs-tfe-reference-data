/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.service

import org.scalamock.handlers.CallHandler1
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.models.response.OtherDataReferenceListResponseModel
import uk.gov.hmrc.emcstfereferencedata.services.EMCSStubService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

trait MockEMCSStubService extends MockFactory {
  lazy val mockService: EMCSStubService = mock[EMCSStubService]

  object MockService {
    def getOtherDataReferenceList(response: OtherDataReferenceListResponseModel): CallHandler1[HeaderCarrier, Future[OtherDataReferenceListResponseModel]] = {
      (mockService.getOtherDataReferenceList(_: HeaderCarrier))
        .expects(*).returns(Future.successful(response))
    }
  }
}
