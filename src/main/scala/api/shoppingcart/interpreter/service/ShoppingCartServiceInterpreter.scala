package api.shoppingcart.interpreter.service

import api.common.entities.{ Pure, ResultError }
import api.common.{ ResultToResponseService, ServiceResult }
import api.products.domain.repository.ProductRepository
import api.products.domain.service.ProductService
import api.products.entities.ProductMetadata
import api.shoppingcart.domain.repository.ShoppingCartRepository
import api.shoppingcart.domain.service.ShoppingCartService
import api.shoppingcart.entities.{ ItemInfo, Products, ShoppingCart }
import cats.data.EitherT
import cats.implicits._

import scala.concurrent.{ ExecutionContext, Future }

class ShoppingCartServiceInterpreter(repo: ShoppingCartRepository, prodSvc: ProductService)(implicit ec: ExecutionContext)
  extends ShoppingCartService
  with ResultToResponseService {

  override def addToCart(item: ItemInfo): ServiceResult[Products] = EitherT {
    prodSvc.findItem(item.itemCode).value.flatMap {
      case Left(value) => Future.successful(value.asLeft[Pure[Products]])
      case Right(productMeta) => {
        val productInfo: ProductMetadata = productMeta.result
        repo.addToCart(item, productInfo.unitPrice, productInfo.brand) map { product =>
          Pure(product).asRight[ResultError]
        }
      }
    }
  }

  override def findAllItems(): ServiceResult[ShoppingCart] = EitherT {
    repo.showCart().map(transformResult[ShoppingCart])
  }
}
