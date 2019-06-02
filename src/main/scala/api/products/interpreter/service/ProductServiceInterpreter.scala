package api.products.interpreter.service

import api.common.{ ResultToResponseService, ServiceResult }
import api.products.domain.repository.ProductRepository
import api.products.domain.service.ProductService
import api.products.entities.ProductMetadata
import cats.data.EitherT

import scala.concurrent.ExecutionContext

class ProductServiceInterpreter(repo: ProductRepository[ProductMetadata])(implicit ec: ExecutionContext)
  extends ProductService
  with ResultToResponseService {

  override def addItem(item: ProductMetadata): ServiceResult[ProductMetadata] = EitherT {
    repo.addProduct(item).map(transformResult[ProductMetadata])
  }

  override def findItem(itemCategory: String): ServiceResult[ProductMetadata] = EitherT {
    repo.findProduct(itemCategory).map(transformResult[ProductMetadata])
  }
}
