package api.shoppingcart.entities

//The shopping cart available for checkout
case class ShoppingCart(items: List[Products], totalPrice: Double, currency: String)

//Collects info about each item and it's price.
case class Products(itemInfo: ItemInfo, itemName: String, price: Double, currency: String)

//Item selected to be added to the cart
case class ItemInfo(itemCode: String, quantity: Int)

