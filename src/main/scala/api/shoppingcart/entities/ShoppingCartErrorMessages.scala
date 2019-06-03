package api.shoppingcart.entities

import api.common.entities.ErrorMessages

trait ShoppingCartErrorMessages extends ErrorMessages {
  override val RESULT_NOT_FOUND: String = "Shopping cart is empty,so cannot checkout"
}
