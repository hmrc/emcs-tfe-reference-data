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

package uk.gov.hmrc.emcstfereferencedata.support

import java.net.ConnectException
import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.Try

trait TestDatabase {
  this: IntegrationBaseSpec =>

  def populateCandeDb(): Try[Unit] = Try {
    logger.info("Attempting to populate CANDE Oracle DB with EMCS data ********")
    Await.result(
      awaitable = client
        .url("http://localhost:9255/classic-services-data-tier-manager/data-models/emcs")
        .post(""),
      atMost = 3.minutes
    )
  }.map { db =>
    logger.info("Finished CANDE Oracle DB Population ********")
    logger.info(s"- Status: ${db.status} ********")
    logger.info(s"- Body  :\n\n ${db.body} ********")
  } recover {
    case e: ConnectException =>
      logger.error("******** ERROR ********")
      logger.error("You must start CLASSIC_SERVICES_DATA_TIER_MANAGER services:")
      logger.error("  - `sm2 --start CLASSIC_SERVICES_DATA_TIER_MANAGER`")
      throw e
    case e =>
      logger.error("******** ERROR ********")
      logger.error("An unexpected error occurred when trying to populate the CANDE DB")
      logger.error(s"${e.getMessage}")
      throw e
  }

}
