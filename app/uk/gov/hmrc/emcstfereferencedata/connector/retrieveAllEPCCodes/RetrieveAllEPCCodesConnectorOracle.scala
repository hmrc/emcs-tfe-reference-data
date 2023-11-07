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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveAllEPCCodes

import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveAllEPCCodes.RetrieveAllEPCCodesConnector._
import uk.gov.hmrc.emcstfereferencedata.models.response.{ErrorResponse, ExciseProductCode}
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{ResultSet, Types}
import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class RetrieveAllEPCCodesConnectorOracle @Inject()(db: Database) extends RetrieveAllEPCCodesConnector with BaseConnector {
  def retrieveAllEPCCodes()(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Either[ErrorResponse, Seq[ExciseProductCode]]] =
    Future.successful {

      logger.info(s"[RetrieveCnCodeInformationConnectorOracle][retrieveAllEPCCodes] retrieving all EPC Codes")

      db.withConnection {
        connection =>

          val storedProcedure = connection.prepareCall(storedProcedureQuery)

          storedProcedure.registerOutParameter(epcCodesParameterKey, Types.REF_CURSOR)
          storedProcedure.registerOutParameter(epcCodeCountParameterKey, Types.NUMERIC)
          storedProcedure.execute()

          val resultSet: ResultSet = storedProcedure.getObject(epcCodesParameterKey, classOf[ResultSet])

          @tailrec
          def buildResult(seq: Seq[ExciseProductCode] = Seq.empty): Seq[ExciseProductCode] =
            if (!resultSet.next()) {
              seq
            } else {
              buildResult(seq :+ ExciseProductCode(
                resultSet.getString(epcCodeKey),
                resultSet.getString(epcCodeDescriptionKey),
                resultSet.getString(categoryCodeKey),
                resultSet.getString(categoryCodeDescriptionKey)
              ) )
            }

          val result = buildResult()

          storedProcedure.close()

          if (result.isEmpty) {
            logger.warn(s"[RetrieveCnCodeInformationConnectorOracle][retrieveCnCodeInformation] No CN Code found")
            Left(ErrorResponse.NoDataReturnedFromDatabaseError)
          } else {
            Right(result)
          }
      }

    }

}
