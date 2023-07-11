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

package uk.gov.hmrc.emcstfereferencedata.connector.retrievePackagingTypes

import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.BaseConnector
import uk.gov.hmrc.emcstfereferencedata.connector.retrievePackagingTypes.RetrievePackagingTypesConnector._
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{CallableStatement, ResultSet, Types}
import javax.inject.Inject
import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

class RetrievePackagingTypesConnectorOracle @Inject()(db: Database) extends RetrievePackagingTypesConnector with BaseConnector {
  def retrievePackagingTypes()(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[Either[ErrorResponse, Map[String, String]]] =
    Future.successful {

      db.withConnection {
        connection =>
          val storedProcedure: CallableStatement = connection.prepareCall(storedProcedureQuery)

          storedProcedure.setString(typeNameParameterKey, typeNameParameterValue)
          storedProcedure.setInt(sortByParameterKey, 1)
          storedProcedure.setString(sortOrderParameterKey, null)
          storedProcedure.setInt(startAtParameterKey, 0)
          storedProcedure.setInt(maxRecordsParameterKey, 1000)
          storedProcedure.setString(descriptionParameterKey, null)
          storedProcedure.registerOutParameter(totalCountKey, Types.NUMERIC)
          storedProcedure.registerOutParameter(displayNameKey, Types.VARCHAR)
          storedProcedure.registerOutParameter(referenceDataKey, Types.REF_CURSOR)
          storedProcedure.registerOutParameter(descriptionListKey, Types.REF_CURSOR)
          storedProcedure.execute()

          val resultSet = storedProcedure.getObject(referenceDataKey, classOf[ResultSet])

          @tailrec
          def buildResult(map: Map[String, String] = Map.empty): Map[String, String] =
            if (!resultSet.next()) {
              map
            } else {
              val code = resultSet.getString(codeKey)
              val description = resultSet.getString(descriptionKey)
              buildResult(map + (code -> description))
            }

          val result = buildResult()

          storedProcedure.close()

          if (result.isEmpty) Left(ErrorResponse.NoDataReturnedFromDatabaseError) else Right(result)
      }
    }
}
