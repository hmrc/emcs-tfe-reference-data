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

package uk.gov.hmrc.emcstfereferencedata.connector


import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.connector.RetrieveCnCodeInformationConnector._
import uk.gov.hmrc.emcstfereferencedata.models.response.{CnCodeInformation, ErrorResponse}

import java.sql.{ResultSet, Types}
import javax.inject.{Inject, Singleton}
import scala.annotation.tailrec


@Singleton
class RetrieveCnCodeInformationConnector @Inject()(db: Database) extends BaseConnector {
  def retrieveCnCodeInformation(productCode: String): Either[ErrorResponse, Map[String, CnCodeInformation]] = {

    val storedProcedure = db.getConnection().prepareCall(storedProcedureQuery)

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

    if (result.isEmpty) Left(ErrorResponse.NoDataReturnedFromDatabaseError) else Right(result)

  }

}

object RetrieveCnCodeInformationConnector {

  private val storedProcedureQuery = "{call EMCS_DATA.EMCS_REFERENCE_DATA.getCNCodes(?, ?, ?, ?, ?)}"
  private val categoryCodeParameterKey = "pCategory_code"
  private val productCodeParameterKey = "pProduct_Code"
  private val cnCodeParameterKey = "pCN_Codes"
  private val productCodeCountParameterKey = "pProduct_count"
  private val cnCodeCountParameterKey = "pCN_Codes_Count"
  private val unitOfMeasureCodeKey = "UNIT_OF_MEASURE_CODE"
  private val cnCodeKey = "CN_CODE"
  private val cnCodeDescriptionKey = "CNCODEDESC"
}
