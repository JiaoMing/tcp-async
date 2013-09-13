import akka.actor.ActorSystem
import server.TcpServer

object Main extends App {
  val system = ActorSystem("server")
  val service = system.actorOf(TcpServer.props, "ServerActor")
}