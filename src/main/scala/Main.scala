import akka.actor.{ Props, ActorSystem }
import handler._
import server.TcpServer

object MainWithEchoHandler extends App {
  val system = ActorSystem("server")
  implicit val handlerProps = EchoHandlerProps
  val service = system.actorOf(Props(new TcpServer[EchoHandler]), "ServerActor")
}

object MainWithApiHandler extends App {
  val system = ActorSystem("server")
  implicit val handlerProps = ApiHandlerProps
  val service = system.actorOf(Props(new TcpServer[ApiHandler]), "ServerActor")
}

object MainWithDbHandler extends App {
  val system = ActorSystem("server")
  implicit val handlerProps = DbHandlerProps
  val service = system.actorOf(Props(new TcpServer[DbHandler]), "ServerActor")
}