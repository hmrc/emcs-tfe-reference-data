/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.emcstfereferencedata.connector.OracleDBConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.{OtherDataReferenceList, OtherDataReferenceListErrorModel}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class OracleDBController @Inject()(cc: ControllerComponents, connector: OracleDBConnector)
                                  (implicit ec: ExecutionContext) extends BackendController(cc) {

  def show(): Action[AnyContent] = Action.async {
    implicit request => connector.executeTransportModeOptionList().map {
      case response:  OtherDataReferenceList => Ok(Json.toJson(response))
      case error: OtherDataReferenceListErrorModel if error.status >= 400 && error.status < 500 => Status(error.status)(error.reason)
      case _ => InternalServerError("Failed to retrieve other data reference list")
    }
  }

}
