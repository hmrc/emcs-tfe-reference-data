/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.config

import javax.inject.{Inject, Singleton}
import play.api.Configuration
import uk.gov.hmrc.emcstfereferencedata.models.{ConnectionDetail, DBOptions}
import uk.gov.hmrc.play.bootstrap.config.ServicesConfig
import scala.collection.convert.ImplicitConversions.{`collection AsScalaIterable`, `set asScala`}

@Singleton
class AppConfig @Inject()(servicesConfig :ServicesConfig, config: Configuration) {

  def stubUrl(): String = servicesConfig.baseUrl("emcs-tfe-reference-data-stub")

  lazy val dbQueryEnabled = config.get[Boolean]("dbQuery.enabled")
  lazy val dbExecProcedureEnabled = config.get[Boolean]("dbExecProcedure.enabled")

  lazy val dbDetails: Map[DBOptions, ConnectionDetail] = {

    val conf = config.underlying
    (for {
      database <- conf.getObject(s"databaseConnections.databases").keySet
      service <- conf.getObject(s"databaseConnections.databases.$database").keySet
      dbOption <- buildDBOptions(service, database)
      connectionDetails <- buildConnectionDetails(service, database)
      if connectionDetails.enabled
    } yield dbOption -> connectionDetails).toMap
  }

  private def buildDBOptions(service: String, database: String) = Some(DBOptions(service, database))


  private def buildJdbcUrl(service: String, database: String, datacentre: String) = {
    val host = dbOptionString(service, database, datacentre, "host")
    val port = dbOptionString(service, database, datacentre, "port")
    val sslEnabled = dbOptionBoolean(service, database, datacentre, "sslEnabled", _ => false)
    val dbName = dbOptionString(service, database, datacentre, "serviceName")

    if (sslEnabled) {
      val sslServerCertDn = dbOptionString(service, database, datacentre, "sslServerCertDn")
      s"""jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCPS)(HOST=$host)(PORT=$port))(CONNECT_DATA=(SERVICE_NAME=$dbName))(SECURITY=(SSL_SERVER_CERT_DN="$sslServerCertDn")))"""
    } else {
      s"jdbc:oracle:thin:@$host:$port/$dbName"
    }
  }

  private def datacentre(service: String, database: String) =
    dbOptionString(service, database, "datacentre", "datacentre", _ => config.get[String]("databaseConnections.defaultDatacentre"))

  private def buildConnectionDetails(service: String, database: String) = {
    val dc = datacentre(service, database)
    Some(ConnectionDetail(
      buildJdbcUrl(service, database, dc),
      dbOptionString(service, database, dc, "properties.user"),
      dbOptionString(service, database, dc, "properties.password"),
      dc,
      dbOptionString(service, database, dc, "properties.query", _ => "begin null; end;"),
      config.getOptional[Boolean](s"databaseConnections.databases.$database.$service.enabled").getOrElse(true)))
  }

  private def dbOptionHandler[A](key: String): A = throw new IllegalStateException(s"Invalid config for $key")

  private def dbOption[A](service: String, database: String, datacentre: String, key: String, f: String => Option[A], h: String => A) =
    f(s"databaseConnections.databases.$database.$service.$key").getOrElse(
      f(s"databaseConnections.servers.$database.$key").getOrElse(
        f(s"databaseConnections.datacentres.$datacentre.$key").getOrElse(
          h(s"databaseConnections.databases.$database.$service.$key")
        )))

  private def dbOptionString(service: String, database: String, datacentre: String, key: String, h: String => String = dbOptionHandler) =
    dbOption(service, database, datacentre, key, config.getOptional[String](_: String), h)

  private def dbOptionBoolean(service: String, database: String, datacentre: String, key: String, h: String => Boolean) =
    dbOption(service, database, datacentre, key, config.getOptional[Boolean](_: String), h)

}
