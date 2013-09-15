package handler

import akka.actor._
import akka.util.ByteString
import akka.io.Tcp.Write

object EchoHandler extends HandlerProp {
  def props: Props = Props(classOf[EchoHandler])
}

class EchoHandler extends Handler {

  /**
   * Echoes incoming message.
   * @return
   */
  def received(data: String) = connection ! Write(ByteString(data + "\n"))
}