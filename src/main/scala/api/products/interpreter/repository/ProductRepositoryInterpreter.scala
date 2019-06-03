package api.products.interpreter.repository

import api.common.entities.ApiErrors
import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound, ServiceError }
import api.products.domain.repository.ProductRepository
import api.products.entities.{ ProductErrorMessages, ProductMetadata }
import cats.implicits._

import scala.collection.mutable
import scala.concurrent.{ ExecutionContext, Future }

class ProductRepositoryInterpreter(implicit ec: ExecutionContext) extends ProductRepository[ProductMetadata]
  with ProductErrorMessages {

  /**
   * This is the place where all user details are stored.
   */
  private val items: mutable.TreeMap[String, ProductMetadata] = mutable.TreeMap.empty

  override def addProduct(product: ProductMetadata): Future[Either[ApiErrors.ServiceError, ProductMetadata]] = Future {
    if (items.contains(product.productCode)) AlreadyExists(ALREADY_EXISTS).asLeft[ProductMetadata]
    else {
      items += (product.productCode -> product)
      product.asRight[ServiceError]
    }
  }

  override def findProduct(category: String): Future[Either[ApiErrors.ServiceError, ProductMetadata]] = Future {
    items.get(category) match {
      case Some(item) => item.asRight[ServiceError]
      case None => ResultNotFound(RESULT_NOT_FOUND).asLeft[ProductMetadata]
    }
  }
}
