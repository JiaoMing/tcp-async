package api

import org.scalatest.matchers.MustMatchers
import org.scalatest.WordSpec
import spray.http.HttpMethods.GET
import spray.http.HttpMethods.POST

import spray.http.HttpRequest
import spray.testkit.ScalatestRouteTest
import spray.routing.HttpService
import org.mockito.Mockito
import org.mockito.Matchers
import org.scalatest.mock.MockitoSugar

class ApiSpec extends WordSpec
  with MustMatchers
  with MockitoSugar
  with ScalatestRouteTest
  with HttpService {

  def actorRefFactory = system

  "An Api createHttpRequest" must {

    val getJson = "{get: true}"
    val postJson = "{post: true}"

    val testRoute =
      path("test") {
        get {
          complete(getJson)
        } ~
        post {
          complete(postJson)
        }
      } ~
      path("datatest") {
        post {
          entity(as[String]) {
            data => complete(data)
          }
        }
      }

    "create HttpRequest that queries the given url using get method by default" in {
      Api.createHttpRequest("/test", GET, "") ~> testRoute ~> check {
        entityAs[String] must equal(getJson)
      }
    }

    "create HttpRequest that queries the given url using the given method" in {
      Api.createHttpRequest("/test", POST, "") ~> testRoute ~> check {
        entityAs[String] must equal(postJson)
      }
    }

    "create HttpRequest that queries the given url using the given method and data" in {
      val data = "somedata"
      Api.createHttpRequest("/datatest", POST, data) ~> testRoute ~> check {
        entityAs[String] must equal(data)
      }
    }
  }

  "An Api httpRequest" must {

    val url = "http://www.example.com"

    "createHttpRequest with given url using GET method and empty data by default" in {
      val api = Mockito.spy(new Api)

      api.httpRequest(url)

      Mockito.verify(api).createHttpRequest(Matchers.eq(url), Matchers.eq(GET), Matchers.eq(""))
    }

    "createHttpRequest with given url and method using empty data by default" in {
      val api = Mockito.spy(new Api)

      api.httpRequest(url, POST)

      Mockito.verify(api).createHttpRequest(Matchers.eq(url), Matchers.eq(POST), Matchers.eq(""))
    }

    "createHttpRequest with given url, method and data" in {
      val api = Mockito.spy(new Api)
      val data = "somedata"

      api.httpRequest(url, POST, data)

      Mockito.verify(api).createHttpRequest(Matchers.eq(url), Matchers.eq(POST), Matchers.eq(data))
    }

    "call sendAndReceive using HttpResponse from createHttpRequest" in {
      val api = Mockito.spy(new Api)

      val httpRequest = mock[HttpRequest]
      val sendAndReceive = mock[spray.client.pipelining.SendReceive]

      Mockito.doReturn(httpRequest).when(api).createHttpRequest(Matchers.any(), Matchers.any(), Matchers.any())
      Mockito.doReturn(sendAndReceive).when(api).sendAndReceive

      api.httpRequest("/test")

      Mockito.verify(sendAndReceive).apply(httpRequest)
    }
  }
}
