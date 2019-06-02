package api.shoppingcart.domain.repository

import api.common.entities.ApiErrors.ServiceError
import api.shoppingcart.entities.{ ItemInfo, Products, ShoppingCart }

import scala.concurrent.Future

trait ShoppingCartRepository {
  //Add Items in the cart
  def addToCart(items: ItemInfo, unitPrice: Double,
    itemName: String): Future[Products]

  // Shows all the items in the cart
  def showCart(): Future[Either[ServiceError, ShoppingCart]]
}
