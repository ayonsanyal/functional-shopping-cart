package api.checkout.entities

import api.shoppingcart.entities.ShoppingCart
import api.users.entity.User

case class CheckOutDetails(cart: ShoppingCart, userDetails: User)
case class OrderId(orderId: String)
case class UserEmail(email: String)
