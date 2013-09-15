package handler

import akka.actor._
import akka.util.ByteString
import akka.io.Tcp.Write

class EchoHandler(connection: ActorRef) extends Handler(connection) {

  /**
   * Echoes incoming message.
   */
  def received(data: String) = connection ! Write(ByteString(data + "\n"))
}