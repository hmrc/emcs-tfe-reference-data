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

package uk.gov.hmrc.emcstfereferencedata.mocks.services

import org.scalamock.handlers.CallHandler3
import org.scalamock.scalatest.MockFactory
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse
import uk.gov.hmrc.emcstfereferencedata.services.RetrievePackagingTypesService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockRetrievePackagingTypesService extends MockFactory {
  lazy val mockService: RetrievePackagingTypesService = mock[RetrievePackagingTypesService]

  object MockService {
    def retrievePackagingTypes(packagingTypesList: Seq[String])(response: Future[Either[ErrorResponse, Map[String, String]]]): CallHandler3[Seq[String], HeaderCarrier, ExecutionContext, Future[Either[ErrorResponse, collection.Map[String, String]]]] =
      (mockService.retrievePackagingTypes(_: Seq[String])(_: HeaderCarrier, _: ExecutionContext)).expects(packagingTypesList, *, *).returns(response)

    def retrievePackagingTypes(optIsCountable: Option[Boolean])(response: Future[Either[ErrorResponse, Map[String, String]]]): CallHandler3[Option[Boolean], HeaderCarrier, ExecutionContext, Future[Either[ErrorResponse, collection.Map[String, String]]]] =
      (mockService.retrievePackagingTypes(_: Option[Boolean])(_: HeaderCarrier, _: ExecutionContext)).expects(optIsCountable, *, *).returns(response)
  }

}
