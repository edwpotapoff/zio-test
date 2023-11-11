import zio.actors.Actor.Stateful
import zio.actors._
import zio._
import zio.Console._


sealed trait Message[+_]
case object Increase extends Message[Unit]
case object Get      extends Message[Int]

object CounterActorExample extends ZIOAppDefault {

  // Definition of stateful actor
  val counterActor: Stateful[Any, Int, Message] =
    new Stateful[Any, Int, Message] {
      override def receive[A](
                               state: Int,
                               msg: Message[A],
                               context: Context
                             ): UIO[(Int, A)] =
        msg match {
          case Increase => ZIO.succeed((state + 1, ()))
          case Get      => ZIO.succeed((state, state))
        }
    }

  val myApp: ZIO[Any, Throwable, Int] =
    for {
      system <- ActorSystem("MyActorSystem")
      actor  <- system.make("counter", zio.actors.Supervisor.none, 0, counterActor)
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      _      <- actor ! Increase
      s      <- actor ? Get
    } yield s

  override def run: ZIO[Any with ZIOAppArgs with Scope, Nothing, ExitCode] =
    myApp.flatMap(state => printLine(s"The final state of counter: $state"))
      .exitCode
}