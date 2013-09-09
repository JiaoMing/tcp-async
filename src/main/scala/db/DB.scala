package db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.github.mauricio.async.db.RowData

object DB {
  val pool = Pool.pool

  /**
   * Creates an prepared statement with given query
   * and passes it to connection pool with given values.
   * @param query
   * @param values
   * @return
   */
  def execute(query: String, values: Seq[Any]): Future[IndexedSeq[RowData]] = {
    pool.sendPreparedStatement(query, values).map(result => result.rows.get.map(item => item))
  }

  /**
   * Passes given query to connection pool.
   * @param query
   * @return
   */
  def rawQuery(query: String) = {
    pool.sendQuery(query)
  }
}
