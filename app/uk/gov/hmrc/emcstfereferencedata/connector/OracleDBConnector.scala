/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.connector

import akka.actor.ActorSystem
import play.api.Logging
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList, OtherDataReferenceListResponseModel}

import java.sql.{Connection, ResultSet}
import javax.inject.Inject
import scala.concurrent.{Future, blocking}

class OracleDBConnector @Inject()(db: Database, config: AppConfig, system: ActorSystem) extends RepositoryBase(db, config, system) with Logging {

  def readFromOracleDB(rs: ResultSet): List[OtherDataReference]= {
    val result = (rs.isBeforeFirst, new Iterator[String] {
      private var available: Boolean = rs.next()

      val columnCount: Int = rs.getMetaData.getColumnCount

      def hasNext: Boolean = available

      def next(): String = {
        val rowDetails = (1 to columnCount).map(rs.getString).mkString("", ",","")
        val response = if (rs.getRow==1) rowDetails; else rowDetails

        available = rs.next()

        response
      }
    })


    result._2.map(_.split(",")).map(
      row => OtherDataReference( "TransportMode", row(1), row(0))
    ).toList


  }

  def executeTransportModeOptionList(): Future[OtherDataReferenceListResponseModel] = Future(blocking {
    val connection = db.getConnection(autocommit = false)


    val sqlStatement = "SELECT DESCRIPTION,CODE FROM EMCS_DATA.EMCS_OTHER_REF_DATA WHERE TYPE_NAME='TransportMode'"

    logger.debug(s"[OracleDBConnector][executeTransportMode] - SQL statement = $sqlStatement")

    try {
      val rs: ResultSet              = connection.createStatement.executeQuery(sqlStatement)
      val (transportModeList)        = readFromOracleDB(rs)

      OtherDataReferenceList(transportModeList)
    } catch {
      case e: Throwable =>
        connection.rollback()
        connection.close()
        throw e
    }

  })

}
