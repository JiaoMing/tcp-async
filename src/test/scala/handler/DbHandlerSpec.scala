package handler

import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import org.scalatest.concurrent.Eventually._
import org.scalatest.{BeforeAndAfterAll, WordSpec}
import akka.actor.{Props, ActorSystem}
import akka.testkit.{TestActorRef, ImplicitSender, TestKit}
import com.github.mauricio.async.db.{RowData, QueryResult}
import concurrent.Future
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.{ArgumentMatcher, Matchers}
import collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global


class DbHandlerSpec(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with WordSpec
  with MustMatchers
  with BeforeAndAfterAll
  with MockitoSugar {

  def this() = this(ActorSystem("DbHandlerSpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "DbHandler.received" must {
    val testActorRef = TestActorRef[DbHandler](Props(new DbHandler(testActor)))
    val handler = testActorRef.underlyingActor
    val spyHandler = spy(handler)
    val mockInput = "mockInput"
    val mockQueryResultFuture = Future {
      mock[QueryResult]
    }

    doReturn(mockQueryResultFuture).when(spyHandler).execute(anyString(), anyString())
    doNothing().when(spyHandler).printAll()

    spyHandler.received(mockInput)
    "execute proper query with given parameter" in {
      class executeInputMatcher extends ArgumentMatcher[mutable.WrappedArray[Any]] {
        def matches(argument: Any) = {
          val array = argument.asInstanceOf[mutable.WrappedArray[Any]].array
          array.size == 1 && array(0).isInstanceOf[String] && array(0).asInstanceOf[String].startsWith(mockInput + "--")
        }
      }
      verify(spyHandler, times(1)).execute(Matchers.eq("INSERT INTO demo VALUES (?)"), argThat(new executeInputMatcher))
    }
    "call printall in the end" in {
      verify(spyHandler, times(1)).printAll()
    }
  }

  "DbHandler.printAll" must {
    val testActorRef = TestActorRef[DbHandler](Props(new DbHandler(testActor)))
    val handler = testActorRef.underlyingActor
    val spyHandler = spy(handler)

    val mockResultSet = List(mock[RowData], mock[RowData]).toSeq

    val mockResultSetResponse = Future {
      Option(mockResultSet)
    }

    doReturn(mockResultSetResponse).when(spyHandler).fetch(anyString(), any())
    doNothing().when(spyHandler).respond(any[RowData])
    doNothing().when(spyHandler).write(anyString())

    "call respond on resultSet.flatMap" in {
      spyHandler.printAll()
      eventually {
        verify(spyHandler, times(mockResultSet.size)).respond(any[RowData])
        mockResultSet.foreach{
          mockRowData =>
            verify(spyHandler, times(1)).respond(Matchers.eq(mockRowData))
        }
      }
    }
  }
}
