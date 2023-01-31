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

        (() => mockResultSet.getMetaData()).expects().returns(mockResultSetMetaData).anyNumberOfTimes()
        (() =>mockResultSetMetaData.getColumnCount).expects().returns(columnNames.size).anyNumberOfTimes()

      def getColumnName(indexPosition: Int): CallHandler1[Int, String] = {
        (mockResultSetMetaData.getColumnClassName(_: Int)).expects(indexPosition).returns(columnNames(indexPosition - 1)).noMoreThanOnce()
      }


        (1 to columnNames.size).foreach(
          x => getColumnName(x)
        )


      def getColumnValues(indexPosition: Int, rowLevelValues: Seq[String]): CallHandler1[Int, String] = {
        (mockResultSet.getString(_: Int)).expects(indexPosition).returns(rowLevelValues(indexPosition - 1)).noMoreThanOnce()
      }

      def getRowValues(columnSize: Int, rowLevelValues: Seq[String]): Unit = {
        (1 to columnSize).foreach(
          x => getColumnValues(x, rowLevelValues)
        )
      }

        rowValues match {
          case Some(rowLevelValues) =>
            getRowValues(columnNames.size, rowLevelValues)
            (() => mockResultSet.isBeforeFirst).expects().returns(true).noMoreThanOnce()
            (() => mockResultSet.isBeforeFirst).expects().returns(false).anyNumberOfTimes()
            (() => mockResultSet.next).expects().returns(true).noMoreThanOnce()
            (() => mockResultSet.next).expects().returns(false).anyNumberOfTimes()
            (() => mockResultSet.getRow).expects().returns(1).anyNumberOfTimes()
          case None =>
            (() => mockResultSet.isBeforeFirst).expects().returns(false).anyNumberOfTimes()
            (() => mockResultSet.next()).expects().returns(true).anyNumberOfTimes()


        }

      (mockDatabase.getConnection(_: Boolean)).expects(*).returns(mockConnection).anyNumberOfTimes()
      (() => mockConnection.createStatement()).expects().returns(mockStatement).anyNumberOfTimes()
      (mockStatement.executeQuery(_: String)).expects(*).returns(mockResultSet).anyNumberOfTimes()

      (() => mockConnection.rollback()).expects().returns({}).anyNumberOfTimes()
      (() => mockConnection.commit()).expects().returns({}).anyNumberOfTimes()
      (() => mockConnection.close()).expects().returns({}).anyNumberOfTimes()
    }
  }
}
