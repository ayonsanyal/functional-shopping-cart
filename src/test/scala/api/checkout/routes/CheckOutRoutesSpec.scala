package api.checkout.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes.{ Created, NotFound }
import akka.http.scaladsl.model.{ ContentTypes, HttpEntity, MediaTypes }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import api.checkout
import api.checkout.CheckOutTestData
import api.checkout.domain.service.CheckOutService
import api.checkout.entities.OrderId
import api.common.entities.{ ApiResponseSuccess, Error, Pure, ResultError }
import api.shoppingcart.domain.service.ShoppingCartService
import api.shoppingcart.entities.Products
import cats.data.EitherT
import cats.implicits._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec }

import scala.concurrent.Future

class CheckOutRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest
  with MockFactory with BeforeAndAfterAll with CheckOutTestData {

  import FutureData._
  import Input._
  import Output._

  implicit val as = ActorSystem("Test")
  implicit val ec = as.dispatcher
  implicit val mat = ActorMaterializer()

  val service = stub[CheckOutService]
  val checkOutoute = new CheckOutRoutes(service)

  "CheckoutRoute" should {

    "Be able to checkout items in shopping cart and return an orderId" in {
      val reqEntity = HttpEntity(MediaTypes.`application/json`, ByteString(email))
      (service.checkOut(_)).when(userDetails.email).returns(
        EitherT(
          Future.successful(Pure(OrderId("DummySuccess")).asRight[ResultError])))

      Post("/api/v1/checkout", reqEntity) ~> checkOutoute.productRoutes ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = ApiResponseSuccess[OrderId](Created.intValue, OrderId("DummySuccess").some)
        responseAs[ApiResponseSuccess[OrderId]] shouldEqual result
      }
    }

    "Not Be able to add items in shopping cart id the product code is not found" in {

      (service.checkOut(_)).when(userDetails.email).returns(
        EitherT(
          Future.successful(resultNotFound)))
      val reqEntity = HttpEntity(MediaTypes.`application/json`, ByteString(email))
      Post("/api/v1/checkout", reqEntity) ~> checkOutoute.productRoutes ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = Error(NotFound.intValue, notFound.message.some)
        responseAs[Error] shouldEqual result
      }
    }
  }
}
