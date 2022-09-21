/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}
import uk.gov.hmrc.emcstfereferencedata.mocks.connectors.MockEMCSStubConnector
import uk.gov.hmrc.emcstfereferencedata.models.response.HelloWorldResponse
import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

class MicroserviceHelloWorldControllerSpec extends UnitSpec with MockEMCSStubConnector {

  implicit val hc: HeaderCarrier = HeaderCarrier()
  implicit val ec: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  private val fakeRequest = FakeRequest("GET", "/hello-world")
  private val controller = new MicroserviceHelloWorldController(Helpers.stubControllerComponents(), mockConnector)


  "GET /hello-world" should {
    "return 200" when {
      "service returns a Right" in {

        MockConnector.getMessage().returns(Future.successful(Right(HelloWorldResponse("Success from reference-data-stub"))))

        val result = controller.hello()(fakeRequest)

        status(result) shouldBe Status.OK
        contentAsJson(result) shouldBe Json.obj("message" -> "Success from reference-data-stub")
      }
    }
    "return 500" when {
      "service returns a Left" in {

        MockConnector.getMessage().returns(Future.successful(Left("connection Time out")))

        val result = controller.hello()(fakeRequest)

        status(result) shouldBe Status.INTERNAL_SERVER_ERROR
      }
    }
  }
}
