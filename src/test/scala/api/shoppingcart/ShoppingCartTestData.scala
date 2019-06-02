package api.shoppingcart

import api.common.entities.ApiErrors.{AlreadyExists, ResultNotFound}
import api.common.entities.{ApiErrors, ErrorMessages, Pure, ResultError}
import api.products.entities.ProductMetadata
import api.shoppingcart.entities.{ItemInfo, Products}
import cats.implicits._

import scala.concurrent.Future

trait ShoppingCartTestData extends ErrorMessages {

  object Input {
    val itemInfo = ItemInfo(itemCode = "item1", quantity = 2)
    val itemInfoRequest =
      """
        |{
        |"itemCode" : "item1",
        |"quantity" : 2
        |}
      """.stripMargin
  }

  object Output {
    import Input._

    val addedToCart: Either[ResultError, Pure[ItemInfo]] = Pure(itemInfo).asRight[ResultError]
    val alreadyExists: ApiErrors.ServiceError = AlreadyExists(ALREADY_EXISTS)
    val notFound = ResultNotFound(RESULT_NOT_FOUND)

    val resultErrorNotFound = ResultError(notFound)
    val resultErrorInUse = ResultError(alreadyExists)

    val alreadyExistsLeft = Left(alreadyExists)
    val notFoundLeft = Left(notFound)
    val addedProductsToCart = Products(itemInfo, "samsung", 400.0, "EUR")
    val productAddedPure = Pure(addedProductsToCart).asRight[ResultError]
    val resultNotFound = resultErrorNotFound.asLeft[Pure[ProductMetadata]]
    val resultInUse = resultErrorInUse.asLeft[Pure[ProductMetadata]]
  }

  object FutureData {
    import Output._
    val addedToCartFut: Future[Either[ResultError, Pure[Products]]] =  Future.successful(productAddedPure)
    val createPostError: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[ProductMetadata]])
    val productNotFound = Future.successful(resultErrorNotFound.asLeft[Pure[Products]])
  }

}
