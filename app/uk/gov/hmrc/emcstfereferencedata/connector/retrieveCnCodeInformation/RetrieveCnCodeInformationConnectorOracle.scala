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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveCnCodeInformation


import cats.instances.either._
import cats.syntax.traverse._
import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveCnCodeInformation.RetrieveCnCodeInformationConnector._
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{ResultSet, Types}
import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class RetrieveCnCodeInformationConnectorOracle @Inject()(db: Database) extends RetrieveCnCodeInformationConnector with BaseConnector {
  def retrieveCnCodeInformation(productCodes: Seq[String])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, CnCodeInformation]]] =
    Future.successful {

      logger.info(s"[RetrieveCnCodeInformationConnectorOracle][retrieveCnCodeInformation] retrieving CN Code information for productCodes: $productCodes")

      db.withConnection {
        connection =>
          productCodes.map {
            productCode =>
              val storedProcedure = connection.prepareCall(storedProcedureQuery)

              storedProcedure.setString(categoryCodeParameterKey, productCode.head.toString)
              storedProcedure.setString(productCodeParameterKey, productCode)
              storedProcedure.registerOutParameter(cnCodeParameterKey, Types.REF_CURSOR)
              storedProcedure.registerOutParameter(productCodeCountParameterKey, Types.NUMERIC)
              storedProcedure.registerOutParameter(cnCodeCountParameterKey, Types.NUMERIC)
              storedProcedure.execute()

              val resultSet = storedProcedure.getObject(cnCodeParameterKey, classOf[ResultSet])

              @tailrec
              def buildResult(map: Map[String, CnCodeInformation] = Map.empty): Map[String, CnCodeInformation] =
                if (!resultSet.next()) {
                  map
                } else {
                  val unitOfMeasureCode = resultSet.getInt(unitOfMeasureCodeKey)
                  val cnCode = resultSet.getString(cnCodeKey)
                  val cdCodeDescription = resultSet.getString(cnCodeDescriptionKey)
                  buildResult(map + (cnCode -> CnCodeInformation(cdCodeDescription, unitOfMeasureCode)))
                }

              val result = buildResult()

              storedProcedure.close()

              if (result.isEmpty) {
                logger.warn(s"[RetrieveCnCodeInformationConnectorOracle][retrieveCnCodeInformation] No CN Code found for productCodes: $productCodes")
                Left(ErrorResponse.NoDataReturnedFromDatabaseError)
              } else {
                Right(result)
              }
          }
      }.sequence.map {
        _.reduce(_ ++ _)
      }

    }

}
