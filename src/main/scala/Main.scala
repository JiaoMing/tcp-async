import akka.actor.ActorSystem
import handler.{ ApiHandler, EchoHandler, DbHandler }
import server.TcpServer

object MainWithEchoHandler extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(EchoHandler), "ServerActor")
}

object MainWithApiHandler extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(ApiHandler), "ServerActor")
}

object MainWithDbHandler extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props(DbHandler), "ServerActor")
}