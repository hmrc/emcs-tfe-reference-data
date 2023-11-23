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
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveOtherReferenceData.RetrieveOtherReferenceDataConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.models.response.{Country, ErrorResponse}
import uk.gov.hmrc.emcstfereferencedata.utils.{Logging, StringUtils}
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RetrieveMemberStatesAndCountriesService @Inject()(connector: RetrieveOtherReferenceDataConnector) extends Logging {

  def get()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Seq[Country]]] = {
    val memberStatesAndCountriesResult: Future[Either[ErrorResponse, Map[String, String]]] = (for {
      memberStates <- EitherT(connector.retrieveMemberStates())
      countries <- EitherT(connector.retrieveCountries())
    } yield {
      val memberStatesAndCountries: Map[String, String] = memberStates ++ countries
      memberStatesAndCountries.map {
        case (k, v) => (k, StringUtils.removeHtmlEscapedCharactersAndAddSmartQuotes(v))
      }
    }).value

    memberStatesAndCountriesResult.map {
      case Left(value) => Left(value)
      case Right(countries) if countries.nonEmpty => Right(Country(countries).sortBy(_.country))
      case _ =>
        logger.warn(s"No data returned for member states or countries")
        Left(NoDataReturnedFromDatabaseError)
    }
  }
}
