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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveEPCForCNCode

import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveEPCForCNCode.RetrieveEPCForCNCodeConnector._
import uk.gov.hmrc.emcstfereferencedata.models.response.{EPCInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{ResultSet, Types}
import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class RetrieveEPCForCNCodeConnectorOracle @Inject()(db: Database) extends RetrieveEPCForCNCodeConnector with BaseConnector {
  def retrieveEPCForCNCode(cnCode: String)(implicit ec: ExecutionContext, hc: HeaderCarrier): Future[Either[ErrorResponse, Seq[EPCInformation]]] =
    Future.successful {

      logger.info(s"[retrieveEPCForCNCode] retrieving EPC Information")

      db.withConnection {
        connection =>

          val storedProcedure = connection.prepareCall(storedProcedureQuery)
          storedProcedure.setString(cnCodeParameterKey, cnCode)
          storedProcedure.registerOutParameter(cnCodesProductParameterKey, Types.REF_CURSOR)
          storedProcedure.execute()

          val resultSet: ResultSet = storedProcedure.getObject(cnCodesProductParameterKey, classOf[ResultSet])

          @tailrec
          def buildResult(seq: Seq[EPCInformation] = Seq.empty): Seq[EPCInformation] =
            if (!resultSet.next()) {
              seq
            } else {
              buildResult(seq :+ EPCInformation(
                resultSet.getString(cnCodeKey),
                resultSet.getString(cnCodeDescriptionKey),
                resultSet.getString(epcCodeKey),
                resultSet.getString(epcCodeDescriptionKey),
                resultSet.getString(epcCategoryKey),
                resultSet.getString(epcCategoryDescriptionKey)
              ) )
            }

          val result = buildResult()

          storedProcedure.close()

          if (result.isEmpty) {
            logger.warn(s"[retrieveEPCForCNCode] No EPC Code found for: $cnCode")
            Left(ErrorResponse.NoDataReturnedFromDatabaseError)
          } else {
            Right(result)
          }
      }

    }

}
