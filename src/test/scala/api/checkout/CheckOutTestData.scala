package api.checkout

import api.checkout.entities.{ CheckOutDetails, OrderId }
import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound, ServiceError }
import api.common.entities.{ ApiErrors, ErrorMessages, Pure, ResultError }
import api.products.entities.ProductMetadata
import api.shoppingcart.entities.{ ItemInfo, Products, ShoppingCart }
import api.users.entity.{ Address, User }
import cats.implicits._

import scala.concurrent.Future

trait CheckOutTestData extends ErrorMessages {

  object Input {
    val userDetails = User("ayon", "ayon.sanyal@mail.com", Address("Street1", 70, 22087, "Germany"))
    val shoppingCart = ShoppingCart(List(
      Products(ItemInfo("item1", 4), "Item Dummy1", 400.0, "EUR"),
      Products(ItemInfo("item2", 2), "Item Dummy2", 200.0, "EUR")), 600.0, "EUR")
    val userFound: Future[Either[ResultError, Pure[User]]] =
      Future.successful(Pure(userDetails).asRight[ResultError])

    val userNotFound = Future.successful(ResultError(ResultNotFound("User Not Found")).asLeft[Pure[User]])

    val email =
      """
        |{
        |"email": "ayon.sanyal@mail.com"
        |}
      """.stripMargin
  }

  object Output {
    import Input._

    val alreadyExists: ApiErrors.ServiceError = AlreadyExists(ALREADY_EXISTS)
    val notFound = ResultNotFound(RESULT_NOT_FOUND)
    val shoppingCartFound = Future.successful(Pure(shoppingCart).asRight[ResultError])
    val shoppingCartNotFound = Future.successful(ResultError(ResultNotFound("Shopping cart is empty,so cannot checkout"))
      .asLeft[Pure[ShoppingCart]])
    val resultErrorNotFound = ResultError(notFound)
    val resultErrorInUse = ResultError(alreadyExists)

    val alreadyExistsLeft = Left(alreadyExists)
    val notFoundLeft = Left(notFound)

    val resultNotFound = resultErrorNotFound.asLeft[Pure[OrderId]]
    val checkOutDetails = CheckOutDetails(shoppingCart, userDetails)
    val checkOutDetilsRight = checkOutDetails.asRight[ServiceError]
    val orderIdCreatedRight = OrderId("OrderSuccess").asRight[ServiceError]
    val orderIdCreated = Pure(OrderId("OrderSuccess")).asRight[ResultError]
  }

  object FutureData {
    import Output._
    val createPostError: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[ProductMetadata]])
    val productNotFound = Future.successful(resultErrorNotFound.asLeft[Pure[OrderId]])
    val checkOutDetailsFuture = Future.successful(orderIdCreatedRight)
  }

}
