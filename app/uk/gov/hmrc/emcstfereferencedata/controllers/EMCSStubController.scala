/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReferenceList, OtherDataReferenceListErrorModel}
import uk.gov.hmrc.emcstfereferencedata.services.EMCSStubService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class EMCSStubController @Inject()(cc: ControllerComponents, service: EMCSStubService)
                                  (implicit ec: ExecutionContext) extends BackendController(cc){

  def getOtherDataReferenceList(): Action[AnyContent] = Action.async { implicit request =>
    service.getOtherDataReferenceList map {
      case response:  OtherDataReferenceList => Ok(Json.toJson(response))
      case error: OtherDataReferenceListErrorModel if error.status >= 400 && error.status < 500 => Status(error.status)(error.reason)
      case _ => InternalServerError("Failed to retrieve other data reference list")
    }
  }

}
