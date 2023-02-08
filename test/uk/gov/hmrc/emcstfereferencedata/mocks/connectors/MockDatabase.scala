/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.connectors


import org.scalamock.handlers.{CallHandler0, CallHandler1, CallHandler2}
import org.scalamock.scalatest.MockFactory
import play.api.db.Database
import org.scalatest.matchers._
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{Connection, ResultSet, ResultSetMetaData, Statement}
import scala.Option.when
import scala.concurrent.{ExecutionContext, Future}

trait MockDatabase extends MockFactory {

  val mockDatabase: Database = mock[Database]

  object MockedDatabase extends MockFactory {
    def mockADatabaseAndResultSet(columnNames: Seq[String], rowValues:Option[Seq[String]]) = {
      val mockConnection = mock[Connection]
      val mockStatement = mock[Statement]
      val mockResultSet = mock[ResultSet]
      val mockResultSetMetaData = mock[ResultSetMetaData]

      def getMetaData(): CallHandler0[ResultSetMetaData] = {
        (mockResultSet.getMetaData()).returns(mockResultSetMetaData)
      }


      when(mockResultSetMetaData.getColumnCount).thenReturn(columnNames.size)

      (1 to columnNames.size).foreach(x => when(mockResultSetMetaData.getColumnName(x)).thenReturn(columnNames(x-1)))

      rowValues match {
        case Some(rowLevelValues) =>
          (1 to columnNames.size).foreach(x => when(mockResultSet.getString(x)).thenReturn(rowLevelValues(x-1)))

          when(mockResultSet.isBeforeFirst).thenReturn(true).thenReturn(false)
          when(mockResultSet.next()).thenReturn(true).thenReturn(false)
          when(mockResultSet.getRow).thenReturn(1)

        case None =>
          when(mockResultSet.isBeforeFirst).thenReturn(false)
          when(mockResultSet.next()).thenReturn(false)
      }


      (mockDatabase.getConnection.expects(*)).returns(mockConnection)
      (mockConnection.createStatement()).returns(mockStatement)
      (mockStatement.executeQuery().expects(*)).returns(mockResultSet)
    }
  }
}
