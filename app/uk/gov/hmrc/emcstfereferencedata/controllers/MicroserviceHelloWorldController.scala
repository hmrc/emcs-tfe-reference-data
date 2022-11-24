/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.emcstfereferencedata.connector.EMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.HelloWorldResponse
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.ExecutionContext

@Singleton()
class MicroserviceHelloWorldController @Inject()(cc: ControllerComponents, connector: EMCSStubConnector)
                                                (implicit ec: ExecutionContext) extends BackendController(cc) {


  def hello(): Action[AnyContent] = Action.async {
    implicit request => connector.getMessage().map {
      case Left(value) => InternalServerError(Json.toJson(HelloWorldResponse(value)))
      case Right(value) => Ok(Json.toJson(HelloWorldResponse(value.message)))
    }
  }
}
