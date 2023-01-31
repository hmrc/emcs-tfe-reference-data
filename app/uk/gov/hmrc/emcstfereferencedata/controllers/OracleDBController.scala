/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.emcstfereferencedata.connector.OracleDBConnector
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton()
class OracleDBController @Inject()(cc: ControllerComponents, connector: OracleDBConnector)
                                  (implicit ec: ExecutionContext) extends BackendController(cc) {

  def show(): Action[AnyContent] = Action.async {
    implicit request => connector.executeTransportMode().map {
      result => Ok(Json.toJson(result))
    }
  }

}
