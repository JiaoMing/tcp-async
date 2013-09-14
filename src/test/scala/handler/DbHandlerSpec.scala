package handler

import org.scalatest.matchers.MustMatchers
import org.scalatest.{ BeforeAndAfterAll, WordSpec }
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }

class DbHandlerSpec(_system: ActorSystem)
    extends TestKit(_system)
    with ImplicitSender
    with WordSpec
    with MustMatchers
    with BeforeAndAfterAll {

  def this() = this(ActorSystem("DbHandlerSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "A DbHandler" must {

    /*
    "close itself if peer closed" in {
      val handler = system.actorOf(DbHandler.props(testActor))
      watch(handler)
      handler ! PeerClosed
      expectTerminated(handler)
    }
    */

  }

}
