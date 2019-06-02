package api.products

import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound }
import api.common.entities.{ ErrorMessages, Pure, ResultError }
import api.products.entities.ProductMetadata
import cats.implicits._

import scala.concurrent.Future

trait ProductsTestData extends ErrorMessages {

  object Input {
    val addProduct = ProductMetadata("mobile", "samsung", 400, "EUR")
  }

  object Output {
    import Input._
    val productCreated = Pure(addProduct).asRight[ResultError]
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

    import Input._
    import Output._
    val createPostResponse: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(Pure(addProduct).asRight[ResultError])
    val createPostError: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[ProductMetadata]])

    val findPostRight: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(Pure(addProduct).asRight[ResultError])
    val findPostLeft: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultNotFound)
  }

}
