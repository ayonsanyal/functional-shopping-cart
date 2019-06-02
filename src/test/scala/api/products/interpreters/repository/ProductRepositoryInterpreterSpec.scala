package api.products.interpreters.repository

import api.UnitSpec
import api.products.ProductsTestData
import api.products.interpreter.repository.ProductRepositoryInterpreter

import scala.concurrent.Await
import concurrent.duration._

class ProductRepositoryInterpreterSpec extends UnitSpec
  with ProductsTestData {

  import Input._
  import Output._

  val productRepo = new ProductRepositoryInterpreter()

  behavior of "addProduct"

  it should "allow addition of products in inmemory collection if it does not exist already" in {
    val result = Await.result(productRepo.addProduct(addProduct), 20 seconds)
    result shouldBe Right(addProduct)
  }

  it should "Not allow addition of products in in memory collection if it  exists already" in {
    val result = Await.result(productRepo.addProduct(addProduct), 20 seconds)
    result shouldBe alreadyExistsLeft
  }

}
