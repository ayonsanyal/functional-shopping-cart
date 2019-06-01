package api.products.domain.repository

import api.common.entities.ApiErrors.ServiceError

import scala.concurrent.Future
trait ProductRepository[Item] {

  def addProduct(product: Item): Future[Either[ServiceError, Item]]

  def findProduct(category: String): Future[Either[ServiceError, Item]]
}
