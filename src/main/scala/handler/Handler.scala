package handler

import akka.actor.{Props, ActorRef, Actor}
import akka.io.Tcp._
import akka.io.Tcp.Received

trait HandlerProp {
  def props: Props
}

case class RegisterConnection(connection: ActorRef)

trait Handler extends Actor {

  val abort = "(?i)abort".r
  val confirmedClose = "(?i)confirmedclose".r
  val close = "(?i)close".r

  var connection: ActorRef = null

  def init: Receive = {
    case RegisterConnection(c) =>
      connection = c
      context become handling
    case _ =>
      println("Please register connection first")
  }

  def handling: Receive = {
    case Received(data) =>
      data.utf8String.trim match {
        case abort() => sender ! Abort
        case confirmedClose() => sender ! ConfirmedClose
        case close() => sender ! Close
        case str => received(str)
      }
    case PeerClosed =>
      peerClosed
      stop
    case Closed =>
      closed
      stop
    case ConfirmedClosed =>
      confirmedClosed
      stop
    case Aborted =>
      aborted
      stop
  }

  def receive: Receive = init

  def received(str: String): Unit

  def peerClosed: Unit = {
    println("PeerClosed")
  }

  def closed: Unit = {
    println("Closed")
  }

  def confirmedClosed: Unit = {
    println("ConfirmedClosed")
  }

  def aborted: Unit = {
    println("Aborted")
  }

  def stop = {
    println("Stopping")
    context stop self
  }
}
