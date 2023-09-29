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

package uk.gov.hmrc.emcstfereferencedata.controllers.predicates

import play.api.libs.json.Reads
import play.api.mvc.{Action, AnyContent, BaseControllerHelpers, Result}
import uk.gov.hmrc.emcstfereferencedata.models.auth.UserRequest

import scala.concurrent.Future

trait AuthActionHelper extends BaseControllerHelpers {
  val auth: AuthAction

  def authorisedUserGetRequestWithErn(ern: String)(block: UserRequest[_] => Future[Result]): Action[AnyContent] =
    auth(Some(ern)).async(block)

  def authorisedUserGetRequest(block: UserRequest[_] => Future[Result]): Action[AnyContent] =
    auth(None).async(block)

  def authorisedUserPostRequest[T](reads: Reads[T])(block: UserRequest[T] => Future[Result]): Action[T] =
    auth(None).async(parse.json[T](reads))(block)
}
