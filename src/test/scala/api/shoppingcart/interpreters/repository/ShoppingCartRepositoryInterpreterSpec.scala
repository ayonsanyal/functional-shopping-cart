package api.shoppingcart.interpreters.repository

import api.UnitSpec
import api.shoppingcart.ShoppingCartTestData
import api.shoppingcart.entities.{ ItemInfo, Products, ShoppingCart }
import api.shoppingcart.interpreter.repository.ShoppingCartRepositoryInterpreter

import scala.concurrent.Await
import scala.concurrent.duration._

class ShoppingCartRepositoryInterpreterSpec extends UnitSpec
  with ShoppingCartTestData {

  import Input._
  import Output._

  behavior of "addToCart"

  it should "allow addition of products in shopping cart if it does not exist already" in {
    val shoppingCartRepo = new ShoppingCartRepositoryInterpreter("EUR")
    val result = Await.result(shoppingCartRepo.addToCart(itemInfo, 200, "Item Dummy"), 20 seconds)
    result shouldBe Products(itemInfo, "Item Dummy", 400.0, "EUR")
  }

  it should "allow to update of products in shopping cart if it does  exist already" in {
    val shoppingCartRepo = new ShoppingCartRepositoryInterpreter("EUR")
    Await.result(shoppingCartRepo.addToCart(itemInfo, 200, "Item Dummy"), 20 seconds)
    val result2 = Await.result(shoppingCartRepo.addToCart(ItemInfo("item1", 4), 200, "Item Dummy"), 20 seconds)
    result2 shouldBe Products(ItemInfo("item1", 4), "Item Dummy", 800.0, "EUR")
  }

  behavior of "showCart"

  it should "Display all the items in a cart if  the cart is not empty" in {
    val shoppingCartRepo = new ShoppingCartRepositoryInterpreter("EUR")
    Await.result(shoppingCartRepo.addToCart(ItemInfo("item1", 4), 100, "Item Dummy1"), 20 seconds)
    Await.result(shoppingCartRepo.addToCart(ItemInfo("item2", 2), 100, "Item Dummy2"), 20 seconds)

    Await.result(shoppingCartRepo.showCart(), 20 seconds) shouldBe Right(shoppingCart)
  }

  it should "Not Display any items in a cart if cart is empty" in {
    val shoppingCartRepo = new ShoppingCartRepositoryInterpreter("EUR")
    Await.result(shoppingCartRepo.showCart(), 20 seconds) shouldBe notFoundLeft
  }

}
