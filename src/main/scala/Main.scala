import akka.actor.{ Props, ActorSystem }
import handler._
import server.TcpServer

object MainWithEchoHandler extends App {
  val system = ActorSystem("server")
  implicit val handlerProps = new HandlerProps[EchoHandler]
  val service = system.actorOf(Props(new TcpServer[EchoHandler]), "ServerActor")
}

object MainWithApiHandler extends App {
  val system = ActorSystem("server")
  implicit val handlerProps = new HandlerProps[ApiHandler]
  val service = system.actorOf(Props(new TcpServer[ApiHandler]), "ServerActor")
}

object MainWithDbHandler extends App {
  val system = ActorSystem("server")
  implicit val handlerProps = new HandlerProps[DbHandler]
  val service = system.actorOf(Props(new TcpServer[DbHandler]), "ServerActor")
}