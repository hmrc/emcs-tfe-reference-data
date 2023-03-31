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

import cats.instances.either._
import cats.syntax.traverse._
import uk.gov.hmrc.emcstfereferencedata.connector.RetrieveCnCodeInformationConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}

import javax.inject.{Inject, Singleton}
import scala.collection.Map

@Singleton
class RetrieveCnCodeInformationService @Inject()(retrieveCnCodeInformationConnector: RetrieveCnCodeInformationConnector) {

  def retrieveCnCodeInformation(productCodeList: Seq[String],
                                cnCodeList: Seq[String]): Either[ErrorResponse, Map[String, CnCodeInformation]] =
    productCodeList.map {
      productCode =>
        retrieveCnCodeInformationConnector.retrieveCnCodeInformation(productCode).map {
          _.collect {
            case (key, value) if cnCodeList.contains(key) => key -> value
          }
        }
    }.sequence.map {
      _.fold(Map[String, CnCodeInformation]())(_ ++ _)
    }

}
