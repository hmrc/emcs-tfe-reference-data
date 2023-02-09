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

package uk.gov.hmrc.emcstfereferencedata.connectors

import akka.actor.ActorSystem
import play.api.http.{HeaderNames, MimeTypes, Status}
import uk.gov.hmrc.emcstfereferencedata.connector.OracleDBConnector
import uk.gov.hmrc.emcstfereferencedata.mocks.config.MockAppConfig
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.{MockDatabase, MockHttpClient}
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReference, OtherDataReferenceList, OtherDataReferenceListResponseModel}
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier

import java.sql.{Connection, ResultSet, ResultSetMetaData, Statement}
import scala.Option.when
import scala.concurrent.ExecutionContext

class OracleDBConnectorSpec extends UnitSpec with Status with MimeTypes with HeaderNames with MockAppConfig with MockHttpClient with MockDatabase {

  class Test( columnNames: Seq[String] = Seq("A", "B", "C"),
              rowValues: Option[Seq[String]] = Some(Seq("mode", "2", "transportMode"))
            ) {
    implicit val hc: HeaderCarrier = HeaderCarrier()
    implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]
    val connector = new OracleDBConnector(mockDatabase, mockAppConfig, app.actorSystem)

    MockedDatabase.mockADatabaseAndResultSet(
      columnNames = columnNames,
      rowValues = rowValues
    )
  }

  "executeTransportModeOptionList" should {

    "return transport mode list" in new Test() {

      val reportResults: OtherDataReferenceListResponseModel = await(connector.executeTransportModeOptionList())

      reportResults.asInstanceOf[OtherDataReferenceList] shouldBe OtherDataReferenceList(
        List(
          OtherDataReference("TransportMode", "2", "mode")
        )
      )
    }

    "return an indication that no data has been found" in new Test(rowValues = None) {
      val reportResults: OtherDataReferenceListResponseModel = await(connector.executeTransportModeOptionList())

      reportResults.asInstanceOf[OtherDataReferenceList].otherRefdata.isEmpty shouldBe true
    }

  }
}

