package api

//#quick-start-server
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration.Duration
import scala.util.{ Failure, Success }
import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import api.users.interpreter.repository.UserRepositoryInterpreter
import api.users.interpreter.service.UserCreationServiceInterpreter
import api.users.routes.UserRoutes
import com.softwaremill.macwire._
//#main-class
object QuickstartServer extends App {

  // set up ActorSystem and other dependencies here
  //#main-class
  //#server-bootstrapping
  implicit val system: ActorSystem = ActorSystem("functional-shopping-cart")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
  //#server-bootstrapping

  lazy val userRepo = wire[UserRepositoryInterpreter]
  lazy val userRoute = new UserRoutes(wire[UserCreationServiceInterpreter])
  //#http-server
  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(userRoute.userRoutes, "localhost", 8080)
  serverBinding.onComplete {
    case Success(bound) =>
      println(s"Server online at http://${bound.localAddress.getHostString}:${bound.localAddress.getPort}/")
    case Failure(e) =>
      Console.err.println(s"Server could not start!")
      e.printStackTrace()
      system.terminate()
  }

  Await.result(system.whenTerminated, Duration.Inf)
}
