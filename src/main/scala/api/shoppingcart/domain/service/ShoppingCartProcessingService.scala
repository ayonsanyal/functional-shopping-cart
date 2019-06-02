package api.shoppingcart.domain.service

import api.common.ServiceResult
import api.shoppingcart.entities.{ ItemInfo, Products, ShoppingCart }

trait ShoppingCartService {

  def addToCart(item: ItemInfo): ServiceResult[Products]

  def findAllItems(): ServiceResult[ShoppingCart]
}
