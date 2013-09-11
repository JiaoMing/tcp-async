package api

import spray.client.pipelining._
import spray.http._
import spray.http.HttpRequest
import scala.concurrent.duration.DurationInt
import akka.actor.ActorSystem
import akka.io.IO
import akka.pattern.ask
import akka.util.Timeout.durationToTimeout
import spray.can.Http
import spray.http.ContentType.apply
import spray.http.Uri.apply
import spray.util.pimpFuture
import HttpMethods._

object Api {

  implicit val system = ActorSystem("api-spray-client")

  import system.dispatcher

  /**
   * Makes HTTP request
   * @param uri
   * @param data
   * @param method
   * @return
   */
  def httpRequest(method: HttpMethod = GET,
                  uri: String = "",
                  data: String = "",
                  contentType: ContentType = MediaTypes.`application/x-www-form-urlencoded`) = {
    val pipeline = sendReceive
    pipeline {
      HttpRequest(method = method,
        uri = uri,
        entity = HttpEntity(contentType, data))
    }
  }
}
