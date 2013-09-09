package handler

import akka.actor.{Props, ActorRef}
import akka.io.Tcp
import akka.util.ByteString
import scala.concurrent.ExecutionContext.Implicits.global
import api.Api
import spray.http.HttpMethods._
import util.Conf

object ApiHandler {
  def props(connection: ActorRef): Props = Props(classOf[ApiHandler], connection)
}

class ApiHandler(connection: ActorRef) extends Handler {

  import Tcp._

  context.watch(connection)

  /**
   * Makes an api request to Google Elevation API with given location data and returns response to user.
   * @return
   */
  def receive = {
    case Tcp.Received(data) => {
      val uri = Conf.apiUrl + data.utf8String.trim()
      Api.httpRequest(method = GET, uri = uri) map { response =>
        respond(response.entity.asString)
      }
    }
    case _: Tcp.ConnectionClosed =>
      context.stop(self)
    case PeerClosed => context stop self
  }

  /**
   * Send given data to user
   * @param response
   */
  def respond(response: String) {
    connection ! Write(ByteString(response))
  }
}