package api.products.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.{ Conflict, Created }
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, MediaTypes }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import api.common.entities.{ ApiResponseSuccess, Error }
import api.products.ProductsTestData
import api.products.domain.service.ProductService
import api.products.entities.ProductMetadata
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import cats.data.EitherT
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec }
import cats.implicits._

class ProductsRouteSpec extends WordSpec with Matchers with ScalatestRouteTest
  with MockFactory with BeforeAndAfterAll with ProductsTestData {

  import Input._
  import Output._
  import FutureData._

  implicit val as = ActorSystem("Test")
  implicit val ec = as.dispatcher
  implicit val mat = ActorMaterializer()

  val service = stub[ProductService]
  val productRoute = new ProductRoutes(service)

  "ProductsRoute" should {

    "Be able to add products which is not already added" in {
      val reqEntity = HttpEntity(MediaTypes.`application/json`, ByteString(addProductRequest))
      (service.addItem(_)).when(addProduct).returns(
        EitherT(
          createProductResponse))

      Post("/api/v1/products", reqEntity) ~> productRoute.productRoutes ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = ApiResponseSuccess[ProductMetadata](Created.intValue, addProduct.some)
        responseAs[ApiResponseSuccess[ProductMetadata]] shouldEqual result
      }
    }

    "Not be able to add products which is already added" in {
      val reqEntity = HttpEntity(MediaTypes.`application/json`, ByteString(addProductRequest))
      (service.addItem(_)).when(addProduct).returns(
        EitherT(
          createProductError))

      Post("/api/v1/products", reqEntity) ~> productRoute.productRoutes ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = Error(Conflict.intValue, alreadyExists.message.some)
        responseAs[Error] shouldEqual result
      }
    }

  }
}
