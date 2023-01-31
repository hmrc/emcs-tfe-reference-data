/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.connector

import akka.actor.ActorSystem
import akka.util.ByteString
import play.api.Logging
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.models.response.TransportModeSQLResult

import java.sql.{Connection, ResultSet}
import javax.inject.Inject
import scala.concurrent.{Future, blocking}

class OracleDBConnector @Inject()(db: Database, config: AppConfig, system: ActorSystem) extends RepositoryBase(db, config, system) with Logging {

  def makeCSVIterator(rs: ResultSet): (Boolean, Iterator[ByteString]) = {
    (rs.isBeforeFirst, new Iterator[ByteString] {
      private var available: Boolean = rs.next()

      val columnCount: Int = rs.getMetaData.getColumnCount

      val columnNames: String = (1 to columnCount).map(rs.getMetaData.getColumnName).mkString("",",","\n")

      def hasNext: Boolean = available

      def next(): ByteString = {
        val rowDetails = (1 to columnCount).map(rs.getString).mkString("", ",", "\n")
        val response = if (rs.getRow==1) columnNames ++ rowDetails; else rowDetails

        available = rs.next()

        ByteString(response)
      }
    })
  }

  def executeTransportMode(): Future[TransportModeSQLResult] = Future(blocking {
    val connection = db.getConnection(autocommit = false)


    val sqlStatement = "SELECT DESCRIPTION,CODE FROM EMCS_DATA.EMCS_OTHER_REF_DATA WHERE TYPE_NAME='TransportMode'"

    logger.debug(s"[OracleDBConnector][executeTransportMode] - SQL statement = $sqlStatement")

    try {
      val rs: ResultSet                   = connection.createStatement.executeQuery(sqlStatement)
      val (containsRows, iterator)        = makeCSVIterator(rs)

      TransportModeSQLResult(iterator.map(_.toString()).toList, containsRows)
    } catch {
      case e: Throwable =>
        connection.rollback()
        connection.close()
        throw e
    }

  })

}
