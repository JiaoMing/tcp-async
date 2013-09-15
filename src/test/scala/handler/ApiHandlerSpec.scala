package handler

import org.scalatest.matchers.MustMatchers
import org.scalatest.{ BeforeAndAfterAll, WordSpec }
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }
import akka.io.Tcp._
import akka.util.ByteString
import akka.io.Tcp.Received

class ApiHandlerSpec(_system: ActorSystem)
    extends TestKit(_system)
    with ImplicitSender
    with WordSpec
    with MustMatchers
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("ApiHandlerSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An ApiHandler" must {

    "return the elevation" in {
      val handler = system.actorOf(ApiHandler.props)
      handler ! RegisterConnection(testActor)
      val data = ByteString("hello")
      handler ! Received(data)
      val Write(message, _) = expectMsgPF() { case message: Write => message }
      message.utf8String must not be ('empty)
    }

    "close itself if peer closed" in {
      val handler = system.actorOf(ApiHandler.props)
      handler ! RegisterConnection(testActor)
      watch(handler)
      handler ! PeerClosed
      expectTerminated(handler)
    }

  }

}
