package handler

import akka.actor.{Actor, ActorSystem, Props}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

@Singleton
class TickService @Inject()(ec: ExecutionContext) {

  def start(listener: Listener): Unit = {
    implicit val actorSystem: ActorSystem = ActorSystem()
    val props = Props(classOf[PingActor], listener)
    val myActor = actorSystem.actorOf(props)
    actorSystem.scheduler.scheduleAtFixedRate(0.seconds, 1.second, myActor, "tick")(executor = ec)
  }
}

trait Listener {
  def tick(): Unit
}

class PingActor(l: Listener) extends Actor {
  def receive: Receive = {
    case _: String =>
      l.tick()
  }
}
