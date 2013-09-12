package handler

import akka.actor._
import akka.io.Tcp
import java.net.InetSocketAddress

object EchoHandler {
  def props(connection: ActorRef): Props =
    Props(classOf[EchoHandler], connection)
}

class EchoHandler(connection: ActorRef) extends Handler {

  import Tcp._

  context.watch(connection)

  /**
   * Echoes incoming message.
   * @return
   */
  def receive = {
    case Received(data) => {
      val text = data.utf8String.trim
      text match {
        case "close" => context.stop(self)
        case _ => connection ! Write(data)
      }
    }

    case PeerClosed => context stop self
  }
}