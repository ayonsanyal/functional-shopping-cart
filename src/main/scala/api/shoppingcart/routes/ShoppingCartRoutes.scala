package api.shoppingcart.routes

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.Directives.{as, entity, pathEnd, pathPrefix}
import akka.http.scaladsl.server.directives.MethodDirectives.post
import api.common.ApiResponseMapperService
import api.shoppingcart.domain.service.ShoppingCartService
import api.shoppingcart.entities.{ItemInfo, Products}
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext

class ShoppingCartRoutes(shppingCartSvc:ShoppingCartService)
                        (implicit executionContext: ExecutionContext, asystem: ActorSystem)
  extends ApiResponseMapperService {

  lazy val log = Logging(asystem, classOf[ShoppingCartRoutes])
  lazy val shoppingCartRoute =
    pathPrefix("api") {
      pathPrefix("v1") {
        pathPrefix("shoppingcart"){
          pathEnd {
            post {
              entity(as[ItemInfo]) { item =>
                mapToApiResponse[Products](shppingCartSvc.addToCart(item), "POST")
              }
            }
          }
        }
      }
    }
}
