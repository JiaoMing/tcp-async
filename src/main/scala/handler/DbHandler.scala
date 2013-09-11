package handler

import akka.actor.{Props, ActorRef}
import akka.io.Tcp
import db.DB
import akka.util.ByteString
import scala.concurrent.ExecutionContext.Implicits.global
import com.github.mauricio.async.db.RowData
import java.util.Date
import scala.Array

object DbHandler {
  def props(connection: ActorRef): Props = Props(classOf[DbHandler], connection)
}

class DbHandler(connection: ActorRef) extends Handler {

  import Tcp._

  context.watch(connection)

  /**
   * Writes incoming message to database and returns all data in db to user
   * @return
   */
  def receive = {
    case Received(data) => {
      DB.execute("insert into demo values (?)", Array(data.utf8String.trim() + "--" + new Date()))
      connection ! Write(ByteString("values in db are: \n"))
      DB.rawQuery("select * from demo") map (result => {
        result.rows.get.map(data => {
          respond(data)
        })
      })
    }
    case _: ConnectionClosed =>
      context.stop(self)
    case PeerClosed => context stop self
  }

  /**
   * Convert given data and send it to user
   * @param response
   */
  def respond(response: RowData) {
    connection ! Write(ByteString(response("data").asInstanceOf[String] + "\n"))
  }
}