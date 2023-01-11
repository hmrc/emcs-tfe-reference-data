/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.libs.json.Json
import play.api.mvc.ControllerComponents
import uk.gov.hmrc.emcstfereferencedata.services.MemoryDBConnectivityResultCacheService
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import javax.inject.Inject
import scala.concurrent.Future

class DBConnectivityController @Inject()(val resultCache: MemoryDBConnectivityResultCacheService, cc: ControllerComponents) extends BackendController(cc) {
  def connectivity() = Action.async {
    Future.successful(Ok(Json.toJson(resultCache.all)))
  }
}