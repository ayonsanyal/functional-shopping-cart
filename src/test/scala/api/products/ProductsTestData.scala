package api.products

import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound, ServiceError }
import api.common.entities.{ ErrorMessages, Pure, ResultError }
import api.products.entities.{ ProductErrorMessages, ProductMetadata }
import cats.implicits._

import scala.concurrent.Future

trait ProductsTestData extends ProductErrorMessages {

  object Input {
    val addProduct = ProductMetadata(productCode = "mobile", brand = "samsung", unitPrice = 400, currency = "EUR")
    val addProductRequest = """
                              |{
                              |"productCode" : "mobile",
                              |"brand" : "samsung",
                              |"unitPrice" : 400,
                              |"currency" : "EUR"
                              |}
                            """.stripMargin
  }

  object Output {
    import Input._
    val productCreated = Pure(addProduct).asRight[ResultError]
    val alreadyExists = AlreadyExists(ALREADY_EXISTS)
    val notFound = ResultNotFound(RESULT_NOT_FOUND)
    val productAdded = addProduct.asRight[ServiceError]
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
    val createProductResponse: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(Pure(addProduct).asRight[ResultError])
    val createProductError: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[ProductMetadata]])

    val findProductRight: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(Pure(addProduct).asRight[ResultError])
    val findProductLeft: Future[Either[ResultError, Pure[ProductMetadata]]] =
      Future.successful(resultNotFound)
  }

}
