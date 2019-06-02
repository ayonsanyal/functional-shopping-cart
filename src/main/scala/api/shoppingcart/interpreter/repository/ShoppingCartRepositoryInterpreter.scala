package api.shoppingcart.interpreter.repository

import api.common.entities.ApiErrors.{ ResultNotFound, ServiceError }
import api.common.entities.{ ApiErrors, ErrorMessages }
import api.products.domain.service.ProductService
import api.products.entities.ProductMetadata
import api.shoppingcart.domain.repository.ShoppingCartRepository
import api.shoppingcart.entities.{ ItemInfo, Products, ShoppingCart }
import cats.implicits._

import scala.collection.mutable
import scala.concurrent.{ ExecutionContext, Future }

class ShoppingCartRepositoryInterpreter(currency: String)(implicit ec: ExecutionContext)
  extends ShoppingCartRepository
  with ErrorMessages {

  /**
   * This is the place where all products are added in shopping cart.
   */
  private val itemsInCart: mutable.TreeMap[String, Products] = mutable.TreeMap.empty

  override def addToCart(product: ItemInfo, unitPrice: Double,
    itemName: String): Future[Products] =
    Future {
      if (itemsInCart.contains(product.itemCode)) {
        //Divided the update operation in a step by step process
        val prodInfoOpt: Option[Products] = itemsInCart.get(product.itemCode)
        val prodInfo = prodInfoOpt.get
        val updatedItemInfo: ItemInfo = prodInfo.itemInfo.copy(quantity = product.quantity)
        val totalPrice = unitPrice * product.quantity
        val updatedProduct: Products = prodInfo.copy(itemInfo = updatedItemInfo, price = totalPrice)
        itemsInCart.update(product.itemCode, updatedProduct)
        updatedProduct
      } else {
        val productMeta: Products = Products(product, itemName, unitPrice * product.quantity, currency)
        itemsInCart += (product.itemCode -> productMeta)
        productMeta
      }
    }

  override def showCart(): Future[Either[ServiceError, ShoppingCart]] = Future {
    val shoppingCart = itemsInCart.values.toList
    val calculatePrice: Double = shoppingCart.foldLeft(0.0)(_ + _.price)
    if (itemsInCart.isEmpty) {
      ResultNotFound(RESULT_NOT_FOUND).asLeft[ShoppingCart]
    } else ShoppingCart(shoppingCart, calculatePrice, currency).asRight[ServiceError]
  }
}
