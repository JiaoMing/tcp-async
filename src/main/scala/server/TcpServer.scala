package server

import akka.actor.{ActorRef, Props, Actor}
import akka.io.{IO, Tcp}
import java.net.InetSocketAddress
import util._
import handler.EchoHandler

object TcpServer {
  def props(connection: ActorRef = null, remote: InetSocketAddress = null): Props =
    Props(classOf[TcpServer])
}

class TcpServer extends Server {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(Conf.appHostName, Conf.appPort))

  override def receive = {
    case CommandFailed(_: Bind) => context stop self

    case Tcp.Connected(remote, local) =>
      val handler = context.actorOf(EchoHandler.props(sender))
      sender ! Register(handler)
  }

}