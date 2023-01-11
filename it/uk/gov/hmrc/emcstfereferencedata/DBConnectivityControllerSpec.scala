/*
 * Copyright 2023 HM Revenue & Customs
 *
 */

package uk.gov.hmrc.emcstfereferencedata

import com.github.tomakehurst.wiremock.stubbing.StubMapping
import org.scalatest.concurrent.ScalaFutures.convertScalaFuture
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.http.Status
import play.api.libs.ws.{WSRequest, WSResponse}
import uk.gov.hmrc.emcstfereferencedata.support.IntegrationBaseSpec

import scala.concurrent.Future

class DBConnectivityControllerSpec extends IntegrationBaseSpec {

  private trait Test {

    def uri: String = "/oracle/connectivity"

    def request(): WSRequest = {
      buildRequest(uri)
    }
  }

  def externalServices: Seq[String] = Seq()

  "DB Connectivity Result Cache" should {
    "successfully return all connections in configuration" in new Test {


      val response: WSResponse = await(request().get())


      response.status shouldBe 200
    }
  }
}
