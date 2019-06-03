package api.products.routes

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.Directives.{ as, entity, pathPrefix, _ }
import akka.http.scaladsl.server.directives.MethodDirectives.post
import api.common.ApiResponseMapperService
import api.products.domain.service.ProductService
import api.products.entities.ProductMetadata
import io.circe.generic.auto._

import scala.concurrent.ExecutionContext
class ProductRoutes(prodSvc: ProductService)(implicit
  executionContext: ExecutionContext,
  asystem: ActorSystem) extends ApiResponseMapperService {

  lazy val log = Logging(asystem, classOf[ProductRoutes])

  lazy val productRoutes =
    pathPrefix("api") {
      pathPrefix("v1") {
        pathPrefix("products") {
          pathEnd {
            post {
              entity(as[ProductMetadata]) { products =>
                mapToApiResponse(prodSvc.addItem(products), "POST")
              }
            }
          }
        }
      }
    }
}
