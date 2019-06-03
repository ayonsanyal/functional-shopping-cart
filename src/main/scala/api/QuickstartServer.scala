package api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import api.checkout.interpreter.repository.CheckOutRepositoryInterpreter
import api.checkout.interpreter.service.CheckOutServiceInterpreter
import api.checkout.routes.CheckOutRoutes
import api.products.interpreter.repository.ProductRepositoryInterpreter
import api.products.interpreter.service.ProductServiceInterpreter
import api.products.routes.ProductRoutes
import api.shoppingcart.interpreter.repository.ShoppingCartRepositoryInterpreter
import api.shoppingcart.interpreter.service.ShoppingCartServiceInterpreter
import api.shoppingcart.routes.ShoppingCartRoutes
import api.users.interpreter.repository.UserRepositoryInterpreter
import api.users.interpreter.service.UserCreationServiceInterpreter
import api.users.routes.UserRoutes
import com.softwaremill.macwire._

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.util.{ Failure, Success }

object QuickstartServer extends App {

  implicit val system: ActorSystem = ActorSystem("functional-shopping-cart")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher

  //initiating routes for Users
  lazy val userRepo = wire[UserRepositoryInterpreter]
  lazy val userSvc = wire[UserCreationServiceInterpreter]
  val userRoute: UserRoutes = wire[UserRoutes]

  // initiating routes for products
  lazy val productRepo = wire[ProductRepositoryInterpreter]
  lazy val productService = wire[ProductServiceInterpreter]
  val productRoute: ProductRoutes = wire[ProductRoutes]

  //Initiating routes for shopping cart
  lazy val shoppingCartRepo = new ShoppingCartRepositoryInterpreter("EUR")
  lazy val shoppingCartSvc = wire[ShoppingCartServiceInterpreter]
  val shoppingCartRoute: ShoppingCartRoutes = wire[ShoppingCartRoutes]

  //Initiating routes for checkOut
  lazy val checkOutRepo = wire[CheckOutRepositoryInterpreter]
  lazy val checkOutSvc = wire[CheckOutServiceInterpreter]
  val checkOutRoutes: CheckOutRoutes = wire[CheckOutRoutes]

  //Initialising routes for the app
  val routes = userRoute.userRoutes ~ productRoute.productRoutes ~
    shoppingCartRoute.shoppingCartRoute ~ checkOutRoutes.productRoutes

  val serverBinding: Future[Http.ServerBinding] = Http().bindAndHandle(routes, "localhost", 8080)
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
