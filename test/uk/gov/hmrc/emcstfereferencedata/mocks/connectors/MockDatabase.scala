/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.mocks.connectors


import org.scalamock.scalatest.MockFactory
import play.api.db.Database

import java.sql.{Connection, ResultSet, ResultSetMetaData, Statement}

trait MockDatabase extends MockFactory {

  val mockDatabase: Database = mock[Database]

  object MockedDatabase extends MockFactory {
    def mockADatabaseAndResultSet(columnNames: Seq[String], rowValues:Option[Seq[String]]): Unit = {
      val mockConnection = mock[Connection]
      val mockStatement = mock[Statement]
      val mockResultSet = mock[ResultSet]
      val mockResultSetMetaData = mock[ResultSetMetaData]

      (() => mockResultSet.getMetaData).expects().returns(mockResultSetMetaData).anyNumberOfTimes()
      (() => mockResultSetMetaData.getColumnCount).expects().returns(columnNames.size).anyNumberOfTimes()

      (1 to columnNames.size).foreach(x => (mockResultSetMetaData.getColumnName(_: Int)).expects(x).returns(columnNames(x-1)).noMoreThanOnce())

      rowValues match {
        case Some(rowLevelValues) =>
          (1 to columnNames.size).foreach(x => (mockResultSet.getString(_: Int)).expects(x).returns(rowLevelValues(x-1)).noMoreThanOnce())

          (() => mockResultSet.isBeforeFirst()).expects().returns(true).noMoreThanOnce()
          (() => mockResultSet.isBeforeFirst()).expects().returns(false).anyNumberOfTimes()
          (() => mockResultSet.next()).expects().returns(true).noMoreThanOnce()
          (() => mockResultSet.next()).expects().returns(false).anyNumberOfTimes()
          (() => mockResultSet.getRow()).expects().returns(1).anyNumberOfTimes()

        case None =>
          (() => mockResultSet.isBeforeFirst()).expects().returns(false).anyNumberOfTimes()
          (() => mockResultSet.next()).expects().returns(false).anyNumberOfTimes()
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
