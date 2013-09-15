package server

import akka.actor.Props
import akka.io.{IO, Tcp}
import java.net.InetSocketAddress
import util._
import handler._
import akka.io.Tcp.{Register, Connected, CommandFailed, Bind}

object TcpServer {
  def props(handlerProp: HandlerProp): Props = Props(classOf[TcpServer], handlerProp)
}

class TcpServer(handlerProp: HandlerProp) extends Server {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(Conf.appHostName, Conf.appPort))

  override def receive = {
    case CommandFailed(_: Bind) => context stop self

    case Connected(remote, local) =>
      val handler = context.actorOf(handlerProp.props)
      handler ! RegisterConnection(sender)
      sender ! Register(handler)
  }

}