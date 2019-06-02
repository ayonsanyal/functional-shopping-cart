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



  behavior of "addProduct"

  it should "allow addition of products in inmemory collection if it does not exist already" in {
    val productRepo = new ProductRepositoryInterpreter()
    val result = Await.result(productRepo.addProduct(addProduct), 20 seconds)
    result shouldBe Right(addProduct)
  }

  it should "Not allow addition of products in in memory collection if it  exists already" in {
    val productRepo = new ProductRepositoryInterpreter()
    Await.result(productRepo.addProduct(addProduct), 20 seconds)
    val result = Await.result(productRepo.addProduct(addProduct), 20 seconds)
    result shouldBe alreadyExistsLeft
  }

  behavior of "findProduct"

  it should "find the product for the productId if it exists" in {
    val productRepo = new ProductRepositoryInterpreter()
    Await.result(productRepo.addProduct(addProduct), 20 seconds)
    val result = Await.result(productRepo.findProduct(addProduct.productCode), 20 seconds)
    result shouldEqual productAdded
  }

  it should "not find find the product for the productId is not found" in {
    val productRepo = new ProductRepositoryInterpreter()
    val result = Await.result(productRepo.findProduct(addProduct.productCode), 20 seconds)
    result shouldEqual notFoundLeft
  }

}
