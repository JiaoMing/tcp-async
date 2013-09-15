package handler

import akka.actor.Props
import akka.util.ByteString
import scala.concurrent.ExecutionContext.Implicits.global
import api.Api
import spray.http.HttpMethods._
import util.Conf
import akka.io.Tcp.Write

object ApiHandler extends HandlerProp {
  def props: Props = Props(classOf[ApiHandler])
}

class ApiHandler extends Handler {

  /**
   * Makes an api request to Google Elevation API with given location data and returns response to user.
   * @return
   */
  def received(data: String) = {
    val uri = Conf.apiUrl + data
    Api.httpRequest(method = GET, uri = uri) map {
      response =>
        respond(response.entity.asString)
    }
  }

  /**
   * Send given data to user
   * @param response
   */
  def respond(response: String) {
    connection ! Write(ByteString(response + "\n"))
  }
}