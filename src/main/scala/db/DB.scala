package db

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.github.mauricio.async.db.{ QueryResult, ResultSet }

object DB {
  val pool = Pool.pool

  /**
   * Creates a prepared statement with the given query
   * and passes it to the connection pool with given values.
   */
  def execute(query: String, values: Any*): Future[QueryResult] = values match {
    case _ :: _ => pool.sendPreparedStatement(query, values)
    case Nil => pool.sendQuery(query)
  }

  /**
   * Creates a prepared statement with the given query
   * and passes it to the connection pool with given values.
   * @return ResultSet of the query
   */
  def fetch(query: String, values: Any*): Future[Option[ResultSet]] =
    execute(query, values).map(_.rows)

}
