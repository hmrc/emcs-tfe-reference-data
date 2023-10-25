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

import cats.data.EitherT
import cats.implicits._
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveCnCodeInformation.RetrieveCnCodeInformationConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveProductCodes.RetrieveProductCodesConnector
import uk.gov.hmrc.emcstfereferencedata.models.request.CnInformationRequest
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.{Inject, Singleton}
import scala.collection.Map
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RetrieveCnCodeInformationService @Inject()(
                                                  cnCodeConnector: RetrieveCnCodeInformationConnector,
                                                  productCodesConnector: RetrieveProductCodesConnector,
                                                ) {

  private val productCodesWithoutCnCode: Seq[String] = Seq("S500")

  def retrieveCnCodeInformation(cnInformationRequest: CnInformationRequest)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, CnCodeInformation]]] = {
    val cnCodeConnectorRequest = cnInformationRequest.copy(items = cnInformationRequest.items.filterNot(item => productCodesWithoutCnCode.contains(item.productCode)))
    val productCodesConnectorRequest = cnInformationRequest.copy(items = cnInformationRequest.items.filter(item => productCodesWithoutCnCode.contains(item.productCode)))

    val cnCodeConnectorResultF: Future[Either[ErrorResponse, Map[String, CnCodeInformation]]] = cnCodeConnector.retrieveCnCodeInformation(cnCodeConnectorRequest)
    val productCodesConnectorResultF: Future[Either[ErrorResponse, Map[String, CnCodeInformation]]] = productCodesConnector.retrieveProductCodes(productCodesConnectorRequest)

    val res: Future[Either[ErrorResponse, Map[String, CnCodeInformation]]] = (for {
      cnCodeConnectorResult <- EitherT(cnCodeConnectorResultF)
      productCodesConnectorResult <- EitherT(productCodesConnectorResultF)
    } yield {
      cnCodeConnectorResult ++ productCodesConnectorResult
    }).value

    res
  }

}
