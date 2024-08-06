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

package uk.gov.hmrc.emcstfereferencedata.connector.retrieveProductCodes


import cats.instances.either._
import cats.syntax.traverse._
import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveProductCodes.RetrieveProductCodesConnector._
import uk.gov.hmrc.emcstfereferencedata.models.request.{CnInformationItem, CnInformationRequest}
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{ResultSet, Types}
import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class RetrieveProductCodesConnectorOracle @Inject()(db: Database) extends RetrieveProductCodesConnector with BaseConnector {
  def retrieveProductCodes(cnInformationRequest: CnInformationRequest)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, CnCodeInformation]]] =
    Future.successful {

      logger.info(s"[RetrieveProductCodesConnectorOracle][retrieveProductCodes] retrieving CN Code information for items: ${cnInformationRequest.items}")

      db.withConnection {
        connection =>
          cnInformationRequest.items.map {
            case item@CnInformationItem(productCode, cnCode) =>
              val storedProcedure = connection.prepareCall(storedProcedureQuery)

              storedProcedure.setString(categoryCodeParameterKey, productCode.head.toString)
              storedProcedure.registerOutParameter(productCodeParameterKey, Types.REF_CURSOR)
              storedProcedure.registerOutParameter(productCodeCountParameterKey, Types.NUMERIC)
              storedProcedure.execute()

              val resultSet = storedProcedure.getObject(productCodeParameterKey, classOf[ResultSet])

              @tailrec
              def buildResult(map: Map[String, CnCodeInformation] = Map.empty): Map[String, CnCodeInformation] =
                if (!resultSet.next()) {
                  map
                } else {
                  val productCodeFromOracle = resultSet.getString(productCodeKey)
                  val unitOfMeasureCode = resultSet.getInt(unitOfMeasureCodeKey)

                  // using column index rather than column name as there are two "DESCRIPTION" columns
                  val description = resultSet.getString(descriptionKeyIndex)

                  // `if` statement to avoid the following scenario:
                  // S500 => 10000000 -> S500
                  // S600 => 10000000 -> S600
                  if(productCodeFromOracle == productCode) {
                    buildResult(map + (cnCode -> CnCodeInformation(cnCode = cnCode, cnCodeDescription = description, exciseProductCode = productCodeFromOracle, exciseProductCodeDescription = description, unitOfMeasureCode = unitOfMeasureCode)))
                  } else {
                    buildResult(map)
                  }
                }

              val result = buildResult()

              storedProcedure.close()

              if (result.isEmpty) {
                logger.warn(s"[RetrieveProductCodesConnectorOracle][retrieveProductCodes] No ProductCodes found for item: $item")
                Left(ErrorResponse.NoDataReturnedFromDatabaseError)
              } else {
                Right(result)
              }
          }
      }.sequence.map {
        value =>
          if (value.isEmpty) Map.empty else value.reduce(_ ++ _)
      }

    }

}
