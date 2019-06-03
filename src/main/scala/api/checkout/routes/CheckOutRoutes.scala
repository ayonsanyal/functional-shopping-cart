package api.checkout.routes

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import api.checkout.domain.service.CheckOutService
import api.checkout.entities.{ CheckOutDetails, OrderId, UserEmail }
import api.common.ApiResponseMapperService
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
class CheckOutRoutes(checkOutSvc: CheckOutService)(implicit
  executionContext: ExecutionContext,
  asystem: ActorSystem) extends ApiResponseMapperService {

  lazy val log = Logging(asystem, classOf[CheckOutRoutes])

  lazy val productRoutes =
    pathPrefix("api") {
      pathPrefix("v1") {
        pathPrefix("checkout") {
          pathEnd {
            post {
              entity(as[UserEmail]) { userEmail =>
                mapToApiResponse[OrderId](checkOutSvc.checkOut(userEmail.email), "POST")
              }
            }
          } ~
            path(Segment) { orderId =>
              get {
                mapToApiResponse[CheckOutDetails](checkOutSvc.findCheckOutDetails(OrderId(orderId)), "GET")
              }
            }
        }
      }
    }
}
