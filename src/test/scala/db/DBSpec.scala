package db

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.mock.MockitoSugar
import com.github.mauricio.async.db.pool.ConnectionPool
import org.mockito.Mockito._
import org.mockito.{ArgumentMatcher, Matchers, Mockito}
import com.github.mauricio.async.db.{ RowData, Connection, QueryResult }
import scala.concurrent.Future
import scala.collection.mutable

class DBSpec extends WordSpec
  with MustMatchers
  with MockitoSugar {

  val mockPool = mock[ConnectionPool[Connection]]
  class Database extends DB

  val query = "DEMO (?,?)"
  val inputParam1 = "input1"
  val inputParam2 = "input2"

  "A DB execute" must {
    val spyDB = spy(new Database)

    doReturn(mockPool).when(spyDB).pool

    "executes \"sendPreparedStatement\" given query and parameters" in {
      spyDB.execute(query, inputParam1, inputParam2)
      Mockito.verify(mockPool).sendPreparedStatement(Matchers.eq(query), Matchers.eq(Array(inputParam1, inputParam2)))
    }

    "executes \"sendQuery\" given query and parameters" in {
      spyDB.execute(query)
      Mockito.verify(mockPool).sendQuery(Matchers.eq(query))
    }

  }

  "A DB fetch" must {
    val mockRowDataSeqFuture = mock[Future[Option[Seq[RowData]]]]
    val spyDB = spy(new Database)

    doReturn(mockPool).when(spyDB).pool

    "executes \"execute\" given query and parameters " in {
      doReturn(mockRowDataSeqFuture).when(spyDB).execute(Matchers.anyString(), Matchers.any())
      spyDB.fetch(query, inputParam1, inputParam2)

      val executeInputMatcher = new ArgumentMatcher[mutable.WrappedArray[Any]] {
        def matches(argument: Any) = {
          val array = argument.asInstanceOf[mutable.WrappedArray[Any]].array
          array.size == 2 && array(0).equals(inputParam1) && array(1).equals(inputParam2)
        }
      }
      Mockito.verify(spyDB).execute(Matchers.eq(query), Matchers.argThat(executeInputMatcher))
    }

  }
}