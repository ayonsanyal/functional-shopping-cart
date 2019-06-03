package api.checkout.interpreters.repository

import api.UnitSpec
import api.checkout.CheckOutTestData
import api.checkout.entities.OrderId
import api.checkout.interpreter.repository.CheckOutRepositoryInterpreter

import scala.concurrent.Await
import scala.concurrent.duration._

class CheckOutRepositoryInterpreterSpec extends UnitSpec
  with CheckOutTestData {

  import Output._

  behavior of "checkOut"

  it should "allow checkout the shopping cart items and store the orderId if it does not exist already" in {
    val checkOutRepo = new CheckOutRepositoryInterpreter()
    val result = Await.result(checkOutRepo.checkOut(checkOutDetails), 20 seconds)
    result.isRight shouldBe true
  }

  behavior of "findCheckOutDetails"

  it should "Display all the items in a cart if  the cart is not empty" in {
    val checkOutRepo = new CheckOutRepositoryInterpreter()
    val order = Await.result(checkOutRepo.checkOut(checkOutDetails), 20 seconds)
    val result = Await.result(checkOutRepo.findCheckOutDetails(order.right.get), 20 seconds)
    result shouldEqual checkOutDetilsRight
  }

  it should "Not Display any items in a cart if cart is empty" in {
    val checkOutRepo = new CheckOutRepositoryInterpreter()
    val result = Await.result(checkOutRepo.findCheckOutDetails(OrderId("fakeOrder")), 20 seconds)
    result shouldBe notFoundLeft
  }
}
