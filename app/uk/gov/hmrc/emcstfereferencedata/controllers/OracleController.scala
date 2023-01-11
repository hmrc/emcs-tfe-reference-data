/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.Logger
import play.api.http.ContentTypes
import play.api.mvc.{AnyContent, ControllerComponents, Request}
import uk.gov.hmrc.emcstfereferencedata.config.AppConfig
import uk.gov.hmrc.emcstfereferencedata.models.DBOptions
import uk.gov.hmrc.emcstfereferencedata.services.OracleDBService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OracleController @Inject()(val connectivityService: OracleDBService, config: AppConfig, cc: ControllerComponents)
                                (implicit ec: ExecutionContext)
  extends BackendController(cc) {
  val logger = Logger(getClass)

  def query(dbName: String) = Action.async { implicit request =>
    execute(dbName, config.dbQueryEnabled, connectivityService.queryDatabase)
  }

  //def execProcedure(dbName: String) = Action.async { implicit request =>
  //  execute(dbName, config.dbExecProcedureEnabled, connectivityService.execProcedure)
  //}

  private def execute(dbName: String, enabled: Boolean, f: (DBOptions, String) => Future[String])(implicit request: Request[AnyContent]) = {
    if (enabled) {
      val query = request.body.asFormUrlEncoded.map(_.getOrElse("query", throw new IllegalArgumentException("Expected query"))).get.head
      f(DBOptions(dbName), query).map(Ok(_).as(ContentTypes.TEXT))
    } else {
      logger.warn(s"Feature disabled")
      Future.successful(NotFound("Feature disabled"))
    }
  }
}
