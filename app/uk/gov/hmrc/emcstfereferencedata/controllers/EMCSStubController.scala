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
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.emcstfereferencedata.controllers.predicates.AuthAction
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReferenceList, OtherDataReferenceListErrorModel}
import uk.gov.hmrc.emcstfereferencedata.services.EMCSStubService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class EMCSStubController @Inject()(cc: ControllerComponents,
                                   service: EMCSStubService,
                                   authAction: AuthAction)
                                  (implicit ec: ExecutionContext) extends BackendController(cc){

  def getOtherDataReferenceList(): Action[AnyContent] = authAction.async { implicit request =>
    service.getOtherDataReferenceList map {
      case response:  OtherDataReferenceList => Ok(Json.toJson(response))
      case error: OtherDataReferenceListErrorModel if error.status >= 400 && error.status < 500 => Status(error.status)(error.reason)
      case _ => InternalServerError("Failed to retrieve other data reference list")
    }
  }

}
