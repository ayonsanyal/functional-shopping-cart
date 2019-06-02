package api.shoppingcart

import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound }
import api.common.entities.{ ErrorMessages, Pure, ResultError }
import api.products.entities.ProductMetadata
import api.shoppingcart.entities.{ ItemInfo, Products }
import cats.implicits._

import scala.concurrent.Future

trait ShoppingCartTestData extends ErrorMessages {

  object Input {
    val itemInfo = ItemInfo("item1", 2)
  }

  object Output {
    import Input._

    val productCreated = Pure(itemInfo).asRight[ResultError]
    val alreadyExists = AlreadyExists(ALREADY_EXISTS)
    val notFound = ResultNotFound(RESULT_NOT_FOUND)

    val resultErrorNotFound = ResultError(notFound)
    val resultErrorInUse = ResultError(alreadyExists)

    val alreadyExistsLeft = Left(alreadyExists)
    val notFoundLeft = Left(notFound)

    val resultNotFound = resultErrorNotFound.asLeft[Pure[ProductMetadata]]
    val resultInUse = resultErrorInUse.asLeft[Pure[ProductMetadata]]
  }

  object FutureData {

    import Output._

    val createPostError: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[ProductMetadata]])
  }

}
