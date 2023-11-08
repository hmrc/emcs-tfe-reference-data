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
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, TypeOfDocument}
import uk.gov.hmrc.emcstfereferencedata.utils.Logging
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RetrieveTypeOfDocumentService @Inject()(connector: RetrieveOtherReferenceDataConnector) extends Logging {

  def retrieveTypesOfDocument()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Seq[TypeOfDocument]]] =
    connector.retrieveTypesOfDocument()
      .map {
        case Left(value) => Left(value)
        case Right(typesOfDocument) if typesOfDocument.nonEmpty => Right(TypeOfDocument(typesOfDocument))
        case _ =>
          logger.warn(s"No data returned for TypeOfDocument")
          Left(NoDataReturnedFromDatabaseError)
      }

}
