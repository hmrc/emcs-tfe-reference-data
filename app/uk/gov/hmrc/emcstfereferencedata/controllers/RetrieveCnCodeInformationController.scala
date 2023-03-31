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

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, ControllerComponents}
import uk.gov.hmrc.emcstfereferencedata.models.request.CnInformationRequest
import uk.gov.hmrc.emcstfereferencedata.models.response.ErrorResponse.NoDataReturnedFromDatabaseError
import uk.gov.hmrc.emcstfereferencedata.services.RetrieveCnCodeInformationService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}

@Singleton()
class RetrieveCnCodeInformationController @Inject()(cc: ControllerComponents,
                                                    retrieveCnCodeInformationService: RetrieveCnCodeInformationService
                                                   ) extends BackendController(cc) {


  def show: Action[CnInformationRequest] = Action(parse.json[CnInformationRequest] {
    json =>
      for {
        productCodeList <- (json \ "productCodeList").validate[Seq[String]]
        cnCodeList <- (json \ "cnCodeList").validate[Seq[String]]
      } yield CnInformationRequest(productCodeList, cnCodeList)
  }) {
    implicit request =>
      retrieveCnCodeInformationService.retrieveCnCodeInformation(request.body.productCodeList, request.body.cnCodeList) match {
        case Right(response) =>
          Ok(Json.toJson(response))
        case Left(error@NoDataReturnedFromDatabaseError) =>
          InternalServerError(Json.toJson(error))
      }
  }

}