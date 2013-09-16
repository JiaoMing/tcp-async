package server

import akka.io.{ IO, Tcp }
import java.net.InetSocketAddress
import util._
import handler._
import akka.io.Tcp.{ Register, Connected, CommandFailed, Bind }
import akka.actor.Props

object TcpServer {
  def props(handlerProps: HandlerProps): Props =
    Props(classOf[TcpServer], handlerProps)
}

class TcpServer(handlerProps: HandlerProps) extends Server {

  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(Conf.appHostName, Conf.appPort))

  override def receive = {
    case CommandFailed(_: Bind) => context stop self

    case Connected(remote, local) =>
      val handler = context.actorOf(handlerProps.props(sender))
      sender ! Register(handler)
  }

}