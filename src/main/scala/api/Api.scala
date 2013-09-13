package api

import spray.client.pipelining._
import spray.http._
import spray.http.HttpRequest
import akka.actor.ActorSystem
import spray.http.ContentType.apply
import spray.http.Uri.apply
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
                  uri: String,
                  data: String = "") = {
    val pipeline = sendReceive
    pipeline {
      HttpRequest(method = method,
        uri = uri,
        entity = HttpEntity(data))
    }
  }
}
