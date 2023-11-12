import zio.actors.Actor.Stateful
import zio.actors._
import zio.Console._
import zio.{UIO, Unsafe, ZIO}


sealed trait Message[+_]
case object Increase extends Message[Unit]
case object Get      extends Message[Int]

object CounterActorExample {

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
      _ <- ZIO.fail(new RuntimeException("first exception"))
      s      <- actor ? Get
    } yield s

//  def run: ZIO[Any with ZIOAppArgs with Scope, Nothing, ExitCode] =
//    myApp.flatMap(state => printLine(s"The final state of counter: $state"))
//      .exitCode

  def main(args: Array[String]): Unit = {
    val runtime = zio.Runtime.default

      Unsafe.unsafe { implicit unsafe =>
        runtime.unsafe.run(myApp.tap(state => printLine(s"The final state of counter: $state"))).getOrThrowFiberFailure()
      }
  }
}