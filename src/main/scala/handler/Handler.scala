package handler

import akka.actor.Actor

trait Handler extends Actor {
  def receive: Receive
}
