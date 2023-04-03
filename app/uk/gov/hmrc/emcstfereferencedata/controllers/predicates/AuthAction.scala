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

import com.google.inject.Inject
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core.AffinityGroup.Organisation
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.emcstfereferencedata.config.EnrolmentKeys
import uk.gov.hmrc.emcstfereferencedata.models.auth.UserRequest
import uk.gov.hmrc.emcstfereferencedata.utils.Logging
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter

import javax.inject.Singleton
import scala.concurrent.{ExecutionContext, Future}

trait AuthAction extends ActionBuilder[UserRequest, AnyContent] with ActionFunction[Request, UserRequest]

@Singleton
class AuthActionImpl @Inject()(override val authConnector: AuthConnector,
                               val parser: BodyParsers.Default
                              )(implicit val executionContext: ExecutionContext) extends AuthAction with AuthorisedFunctions with Logging {

  override def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequest(request)

    implicit val req = request

    authorised().retrieve(Retrievals.affinityGroup and Retrievals.allEnrolments and Retrievals.internalId and Retrievals.credentials) {

      case Some(Organisation) ~ enrolments ~ Some(internalId) ~ Some(credentials) =>
        checkOrganisationEMCSEnrolment(enrolments, internalId, credentials.providerId)(block)

      case Some(Organisation) ~ _ ~ None ~ _ =>
        logger.warn("[invokeBlock] InternalId could not be retrieved from Auth")
        Future.successful(Unauthorized)

      case Some(Organisation) ~ _ ~ _ ~ None =>
        logger.warn("[invokeBlock] Credentials could not be retrieved from Auth")
        Future.successful(Unauthorized)

      case Some(affinityGroup) ~ _ ~ _ ~ _ =>
        logger.warn(s"[invokeBlock] User has incompatible AffinityGroup of '$affinityGroup'")
        Future.successful(Unauthorized)

      case _ =>
        logger.warn(s"[invokeBlock] User has no AffinityGroup")
        Future.successful(Unauthorized)

    } recover {
      case _: NoActiveSession =>
        Unauthorized
      case x: AuthorisationException =>
        logger.debug(s"[invokeBlock] Authorisation Exception ${x.reason}")
        Forbidden
    }
  }

  private def checkOrganisationEMCSEnrolment[A](enrolments: Enrolments,
                                                internalId: String,
                                                credId: String
                                               )(block: UserRequest[A] => Future[Result])
                                               (implicit request: Request[A]): Future[Result] =
    enrolments.enrolments.filter(enrolment => enrolment.key == EnrolmentKeys.EMCS_ENROLMENT) match {
      case emcsEnrolments if emcsEnrolments.isEmpty =>
        logger.debug(s"[checkOrganisationEMCSEnrolment] No ${EnrolmentKeys.EMCS_ENROLMENT} enrolment found")
        Future.successful(Forbidden)
      case emcsEnrolments =>
        if (emcsEnrolments.exists(_.isActivated)) block(UserRequest(request, internalId, credId)) else {
          logger.debug(s"[checkOrganisationEMCSEnrolment] ${EnrolmentKeys.EMCS_ENROLMENT} enrolment found but not activated")
          Future.successful(Forbidden)
        }
    }
}
