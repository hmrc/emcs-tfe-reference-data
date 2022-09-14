/*
 * Copyright 2022 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata.controllers

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test.{FakeRequest, Helpers}

class MicroserviceHelloWorldControllerSpec extends AnyWordSpec with Matchers {

  private val fakeRequest = FakeRequest("GET", "/hello-world")
  private val controller = new MicroserviceHelloWorldController(Helpers.stubControllerComponents())

  "GET /hello-world" should {
    "return 200" in {
      val result = controller.hello()(fakeRequest)
      status(result) shouldBe Status.OK
      contentAsJson(result) shouldBe Json.obj("message" -> "Success from reference-data")
    }
  }
}
