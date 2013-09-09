package server

import akka.actor.Actor

trait Server extends Actor {
  def receive: Receive
}