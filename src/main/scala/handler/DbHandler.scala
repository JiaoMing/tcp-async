package handler

import db.DB
import akka.util.ByteString
import com.github.mauricio.async.db.RowData
import java.util.Date
import scala.Array
import akka.io.Tcp.Write
import akka.actor.{ Props, ActorRef }
import scala.concurrent.ExecutionContext.Implicits.global

object DbHandlerProps extends HandlerProps {
  def props(connection: ActorRef) = Props(classOf[DbHandler], connection)
}

class DbHandler(connection: ActorRef) extends Handler(connection) {

  /**
   * Writes incoming message to database and returns all data in db to user
   * @return
   */
  def received(data: String) = {
    DB.execute("INSERT INTO demo VALUES (?)", Array(data + "--" + new Date))
    connection ! Write(ByteString("values in db are: \n"))
    for {
      queryResult <- DB.rawQuery("SELECT * FROM demo")
      resultSet <- queryResult.rows
      result <- resultSet
    } respond(result)
  }

  /**
   * Convert given data and send it to user
   * @param response
   */
  def respond(response: RowData) {
    connection ! Write(ByteString(response("data").asInstanceOf[String] + "\n"))
  }
}