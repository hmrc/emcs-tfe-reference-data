/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.models

import play.api.libs.json.{Json, Reads, Writes}

import java.util.Date

case class ConnectivityAttempt(connectionOK: Boolean, error: Option[String])

object ConnectivityAttempt {
  implicit val formats = Json.format[ConnectivityAttempt]
}

case class ConnectivityResult(name: String,
                              order: String,
                              owningGroup: Option[String],
                              connectionOK: Boolean,
                              connectionString: String,
                              connectivityAttempts: Seq[ConnectivityAttempt],
                              dataCentre: String)

object ConnectivityResult {
  implicit val formats = Json.format[ConnectivityResult]
}

case class DataCentreConnectivityResult(name: String, allOk: Boolean, results: Seq[ConnectivityResult])

object DataCentreConnectivityResult {
  implicit val formats = Json.format[DataCentreConnectivityResult]
}

case class EMCSDbTestResult(datacentres: Seq[DataCentreConnectivityResult], resultDate: Date, resultsStateLastChangedDate: Date)

object EMCSDbTestResult {
  implicit val dateW = Writes.dateWrites("yyyy-MM-dd'T'HH:mm:ssZ")
  implicit val dateR = Reads.dateReads("yyyy-MM-dd'T'HH:mm:ssZ")
  implicit val formats = Json.format[EMCSDbTestResult]
}


case class DBOptions(service: String, database: String) {
  val name = s"$service-$database"
}

object DBOptions {
  def apply(dbName: String): DBOptions = {
    apply(dbName.substring(0, dbName.lastIndexOf("-")), dbName.substring(dbName.lastIndexOf("-") + 1))
  }
}

case class ConnectionDetail(jdbcUrl: String, user: String, password: String, datacentre: String, query: String, enabled: Boolean)
