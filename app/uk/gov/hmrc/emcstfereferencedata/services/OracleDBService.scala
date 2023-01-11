/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.services

import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.models.DBOptions

import java.sql.{DriverManager, ResultSet}
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class OracleDBService @Inject()(config: AppConfig)(implicit ec: ExecutionContext) {

  def mapRsToString(resultSet: ResultSet): String = {
    val rsmd = resultSet.getMetaData
    val columnsNumber = rsmd.getColumnCount
    val output = new StringBuffer
    while (resultSet.next()) {
      for (i <- Range(1, columnsNumber + 1)) {
        output.append(if (i > 1) ", " else "Record ")
          .append(rsmd.getColumnName(i))
          .append(": ")
          .append(buildStringFromResultSetValue(resultSet, i))
      }
      output.append("\n\n")
    }
    output.toString
  }

  def buildStringFromResultSetValue(rs: ResultSet, i: Int): String = rs.getObject(i) match {
    case rs: ResultSet => mapRsToString(rs)
    case _ => rs.getString(i)
  }

  def queryDatabase(db: DBOptions, query: String) = Future {
    config.dbDetails.get(db).fold(
      "Invalid DB"
    )(cd =>
      Try(DriverManager.getConnection(cd.jdbcUrl, cd.user, cd.password)) match {
        case Failure(f) =>
          "Cannot connect to database"
        case Success(connection) =>
          try {
            val statement = connection.createStatement()
            mapRsToString(statement.executeQuery(query))
          } finally {
            connection.close()
          }
      }
    )
  }
}
