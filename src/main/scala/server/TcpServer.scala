package server

import akka.actor.{ActorRef, Props}
import akka.io.{IO, Tcp}
import java.net.InetSocketAddress
import util._
import handler._

object TcpServer {
  def props(): Props =
    Props(classOf[TcpServer])
}

class TcpServer extends Server {

  import Tcp._
  import context.system

  IO(Tcp) ! Bind(self, new InetSocketAddress(Conf.appHostName, Conf.appPort))

  override def receive = {
    case CommandFailed(_: Bind) => context stop self

    case Connected(remote, local) =>
      val handler = context.actorOf(DbHandler.props(sender))
      sender ! Register(handler)
  }

}