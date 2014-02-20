package it.create.theater

import akka.actor.{Props, ActorSystem}
import akka.kernel.Bootable


class JobServer extends Bootable {

  def startup = {
    println("started!")
  }

  def shutdown = {
    println("shut down!")
  }
}
