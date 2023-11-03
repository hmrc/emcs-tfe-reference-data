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

package uk.gov.hmrc.emcstfereferencedata.config

import com.google.inject.{AbstractModule, Inject, Singleton}
import play.api.{Configuration, Environment}
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveAllCNCodes.{RetrieveAllCNCodesConnector, RetrieveAllCNCodesConnectorOracle, RetrieveAllCNCodesConnectorStub, RetrieveEPCForCNCodeConnectorStub}
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveCnCodeInformation._
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveEPCForCNCode.{RetrieveEPCForCNCodeConnector, RetrieveEPCForCNCodeConnectorOracle}
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveOtherReferenceData._
import uk.gov.hmrc.emcstfereferencedata.connector.retrievePackagingTypes._
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveProductCodes._
import uk.gov.hmrc.emcstfereferencedata.connector.retrieveTraderKnownFacts._
import uk.gov.hmrc.emcstfereferencedata.controllers.predicates.{AuthAction, AuthActionImpl}

@Singleton
class Module @Inject()(environment: Environment, config: Configuration) extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[AppConfig]).asEagerSingleton()
    bind(classOf[AuthAction]).to(classOf[AuthActionImpl])

    if (config.get[Boolean]("feature-switch.use-oracle")) {
      bind(classOf[RetrieveCnCodeInformationConnector]).to(classOf[RetrieveCnCodeInformationConnectorOracle])
      bind(classOf[RetrieveProductCodesConnector]).to(classOf[RetrieveProductCodesConnectorOracle])
      bind(classOf[RetrievePackagingTypesConnector]).to(classOf[RetrievePackagingTypesConnectorOracle])
      bind(classOf[RetrieveOtherReferenceDataConnector]).to(classOf[RetrieveOtherReferenceDataConnectorOracle])
      bind(classOf[RetrieveTraderKnownFactsConnector]).to(classOf[RetrieveTraderKnownFactsConnectorOracle])
      bind(classOf[RetrieveAllCNCodesConnector]).to(classOf[RetrieveAllCNCodesConnectorOracle])
      bind(classOf[RetrieveEPCForCNCodeConnector]).to(classOf[RetrieveEPCForCNCodeConnectorOracle])
    } else {
      bind(classOf[RetrieveCnCodeInformationConnector]).to(classOf[RetrieveCnCodeInformationConnectorStub])
      bind(classOf[RetrieveProductCodesConnector]).to(classOf[RetrieveProductCodesConnectorStub])
      bind(classOf[RetrievePackagingTypesConnector]).to(classOf[RetrievePackagingTypesConnectorStub])
      bind(classOf[RetrieveOtherReferenceDataConnector]).to(classOf[RetrieveOtherReferenceDataConnectorStub])
      bind(classOf[RetrieveTraderKnownFactsConnector]).to(classOf[RetrieveTraderKnownFactsConnectorStub])
      bind(classOf[RetrieveAllCNCodesConnector]).to(classOf[RetrieveAllCNCodesConnectorStub])
      bind(classOf[RetrieveEPCForCNCodeConnector]).to(classOf[RetrieveEPCForCNCodeConnectorStub])
    }

  }
}
