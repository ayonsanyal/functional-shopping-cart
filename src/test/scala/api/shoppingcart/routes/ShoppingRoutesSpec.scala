package api.shoppingcart.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.{Created, NotFound}
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, MediaTypes}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import api.common.entities.{ApiResponseSuccess, Error}
import api.shoppingcart.ShoppingCartTestData
import api.shoppingcart.domain.service.ShoppingCartService
import api.shoppingcart.entities.Products
import cats.data.EitherT
import cats.implicits._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
class ShoppingRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest
  with MockFactory with BeforeAndAfterAll with ShoppingCartTestData {

  import FutureData._
  import Input._
  import Output._

  implicit val as = ActorSystem("Test")
  implicit val ec = as.dispatcher
  implicit val mat = ActorMaterializer()

  val service = stub[ShoppingCartService]
  val shoppingCartRoute = new ShoppingCartRoutes(service)

  "ShoppingCartRoute" should {

    "Be able to add items in shopping cart which is not already added" in {
      val reqEntity = HttpEntity(MediaTypes.`application/json`, ByteString(itemInfoRequest))
      (service.addToCart(_)).when(itemInfo).returns(
        EitherT(
          addedToCartFut)
      )

      Post("/api/v1/shoppingcart", reqEntity) ~> shoppingCartRoute.shoppingCartRoute ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = ApiResponseSuccess[Products](Created.intValue, addedProductsToCart.some)
        responseAs[ApiResponseSuccess[Products]] shouldEqual result
      }
    }

    "Not Be able to add items in shopping cart id the product code is not found" in {
      val reqEntity = HttpEntity(MediaTypes.`application/json`, ByteString(itemInfoRequest))
      (service.addToCart(_)).when(itemInfo).returns(
        EitherT(
          productNotFound)
      )

      Post("/api/v1/shoppingcart", reqEntity) ~> shoppingCartRoute.shoppingCartRoute ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = Error(NotFound.intValue, notFound.message.some)
        responseAs[Error] shouldEqual result
      }
    }
  }
}
