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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveTraderKnownFacts

import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveTraderKnownFacts.RetrieveTraderKnownFactsConnector._
import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, TraderKnownFacts}
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{ResultSet, Types}
import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class RetrieveTraderKnownFactsConnectorOracle @Inject()(db: Database) extends RetrieveTraderKnownFactsConnector with BaseConnector {
  def retrieveTraderKnownFacts(exciseRegistrationId: String)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Option[TraderKnownFacts]]] =
    Future.successful {

      logger.info(s"[RetrieveTraderKnownFactsConnectorOracle][retrieveTraderKnownFacts] retrieving trader known facts for exciseRegistrationId: $exciseRegistrationId")

      db.withConnection {
        connection =>
          val storedProcedure = connection.prepareCall(storedProcedureQuery)

          storedProcedure.setString(exciseRegistrationIdParameterKey, exciseRegistrationId)
          storedProcedure.registerOutParameter(traderKfParameterKey, Types.REF_CURSOR)
          storedProcedure.execute()

          val resultSet: ResultSet = storedProcedure.getObject(traderKfParameterKey, classOf[ResultSet])

          @tailrec
          def buildResult(result: Option[TraderKnownFacts] = None): Option[TraderKnownFacts] =
            if (!resultSet.next()) {
              logger.debug(s"Known facts retrieved for $exciseRegistrationId: [$result]")
              result
            } else {
              val res = Some(TraderKnownFacts(
                traderName = getOptionalValue(resultSet, traderNameKey),
                addressLine1 = getOptionalValue(resultSet, address1Key),
                addressLine2 = getOptionalValue(resultSet, address2Key),
                addressLine3 = getOptionalValue(resultSet, address3Key),
                addressLine4 = getOptionalValue(resultSet, address4Key),
                addressLine5 = getOptionalValue(resultSet, address5Key),
                postcode = getOptionalValue(resultSet, postcodeKey)
              ))
              logger.info(s"Retrieved known facts: $res")
              buildResult(res)
            }

          val result = buildResult()

          storedProcedure.close()

          if (result.isEmpty) {
            logger.warn(s"[RetrieveTraderKnownFactsConnectorOracle][retrieveTraderKnownFacts] No trader known facts found for exciseRegistrationId: $exciseRegistrationId")
          }

          Right(result)
      }
    }

  private def getOptionalValue(resultSet: ResultSet, key: String): Option[String] = try {
    val result = resultSet.getString(key)

    // getString can return null if value isn't present
    // Wrapping in Option turns null into None and something into Some(something)
    Option(result)
  } catch {
    case _: Throwable =>
      logger.warn(s"Unable to retrieve $key as String from ResultSet")
      None
  }

}
