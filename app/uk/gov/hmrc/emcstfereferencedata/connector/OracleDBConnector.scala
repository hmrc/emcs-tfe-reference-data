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

import akka.actor.ActorSystem
import play.api.Logging
import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList, OtherDataReferenceListResponseModel}

import java.sql.ResultSet
import javax.inject.Inject
import scala.concurrent.{Future, blocking}

class OracleDBConnector @Inject()(db: Database, config: AppConfig, system: ActorSystem) extends RepositoryBase(db, config, system) with Logging {

  def readFromOracleDB(rs: ResultSet): List[OtherDataReference]= {
    val result = new Iterator[String] {

      def hasNext: Boolean = rs.next()

      def next(): String =
        (1 to rs.getMetaData.getColumnCount).map(rs.getString).mkString("", ",","")
    }

    result.map(_.split(",")).map(
      row => OtherDataReference( "TransportMode", row(1), row(0))
    ).toList
  }

  def executeTransportModeOptionList(): Future[OtherDataReferenceListResponseModel] = Future(blocking {
    val connection = db.getConnection(autocommit = false)


    val sqlStatement = "SELECT DESCRIPTION,CODE FROM EMCS_DATA.EMCS_OTHER_REF_DATA WHERE TYPE_NAME='TransportMode'"

    logger.debug(s"[OracleDBConnector][executeTransportMode] - SQL statement = $sqlStatement")

    try {
      val rs: ResultSet = connection.createStatement.executeQuery(sqlStatement)
      OtherDataReferenceList(readFromOracleDB(rs))
    } catch {
      case e: Throwable =>
        connection.rollback()
        connection.close()
        throw e
    }

  })

}
