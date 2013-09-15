package handler

import akka.actor.{ Props, ActorRef, Actor }
import akka.io.Tcp._
import akka.io.Tcp.Received
import scala.reflect.ClassTag

class HandlerProps[T <: Handler: ClassTag] {
  def props(connection: ActorRef): Props =
    Props(implicitly[ClassTag[T]].runtimeClass, connection)
}

abstract class Handler(val connection: ActorRef) extends Actor {

  val abort = "(?i)abort".r
  val confirmedClose = "(?i)confirmedclose".r
  val close = "(?i)close".r

  def receive: Receive = {
    case Received(data) =>
      data.utf8String.trim match {
        case abort() => sender ! Abort
        case confirmedClose() => sender ! ConfirmedClose
        case close() => sender ! Close
        case str => received(str)
      }
    case PeerClosed =>
      peerClosed()
      stop()
    case Closed =>
      closed()
      stop()
    case ConfirmedClosed =>
      confirmedClosed()
      stop()
    case Aborted =>
      aborted()
      stop()
  }

  def received(str: String): Unit

  def peerClosed() {
    println("PeerClosed")
  }

  def closed() {
    println("Closed")
  }

  def confirmedClosed() {
    println("ConfirmedClosed")
  }

  def aborted() {
    println("Aborted")
  }

  def stop() {
    println("Stopping")
    context stop self
  }
}
