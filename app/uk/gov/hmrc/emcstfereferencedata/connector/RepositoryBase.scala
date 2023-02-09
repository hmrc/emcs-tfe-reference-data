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

import java.sql.{Connection, Types}
import akka.actor.ActorSystem
import play.api.db.Database
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext

abstract class RepositoryBase(db: Database, config: AppConfig, system: ActorSystem) {

  lazy implicit val ec: ExecutionContext = system.dispatchers.lookup("database.dispatcher")

  private lazy val appLogging = if (config.appLogging) "Y" else "N"
  lazy val transactionTimeoutSeconds = config.transactionTimeout.toSeconds.toInt

  def register(connection: Connection, hc: HeaderCarrier, userId: Option[String] = None) = {
    val cs = connection.prepareCall("call DB_LOGGER.Register(?, ?, ?, ?)")
    cs.setQueryTimeout(transactionTimeoutSeconds)
    userId.fold(cs.setNull(1, Types.VARCHAR))(id => cs.setString(1, id))
    hc.sessionId.fold(cs.setNull(2, Types.VARCHAR))(id => cs.setString(2, id.value))
    hc.requestId.fold(cs.setNull(3, Types.VARCHAR))(id => cs.setString(3, id.value))
    cs.setString(4, appLogging)

    cs.execute

    true
  }
}