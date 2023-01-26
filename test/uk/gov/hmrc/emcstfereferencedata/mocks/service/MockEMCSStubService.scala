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
