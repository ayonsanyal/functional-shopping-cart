package api.products.interpreters.service

import api.UnitSpec
import api.products.ProductsTestData
import api.products.domain.repository.ProductRepository
import api.products.entities.ProductMetadata
import api.products.interpreter.service.ProductServiceInterpreter
import org.scalamock.scalatest.MockFactory

import scala.concurrent.{ Await, Future }
import concurrent.duration._

class ProductServiceInterpreterSpec extends UnitSpec
  with ProductsTestData with MockFactory {

  import Input._
  import Output._

  val productRepo = stub[ProductRepository[ProductMetadata]]


  behavior of "addProduct"

  it should "return the ServiceResult with Pure value of Item when success" in {
    val productSvc = new ProductServiceInterpreter(productRepo)
    (productRepo.addProduct(_)).when(addProduct).returns(Future.successful(productAdded))
    val result = Await.result(productSvc.addItem(addProduct).value, 20 seconds)
    result shouldBe productCreated
  }

  it should "return the error message if the User already exists" in {
    val productSvc = new ProductServiceInterpreter(productRepo)
    (productRepo.addProduct(_)).when(addProduct).returns(Future.successful(alreadyExistsLeft))
    val result = Await.result(productSvc.addItem(addProduct).value, 20 seconds)
    result shouldBe resultInUse
  }

  behavior of "findProduct"

  it should "find the product for category if it exists" in {
    val productSvc = new ProductServiceInterpreter(productRepo)
    (productRepo.findProduct(_)).when(addProduct.productCode).returns(Future.successful(productAdded))
    val result = Await.result(productSvc.findItem(addProduct.productCode).value, 20 seconds)
    result shouldBe productCreated
  }

  it should "return error message if the user with email does not exist" in {
    val productSvc = new ProductServiceInterpreter(productRepo)
    (productRepo.findProduct(_)).when(addProduct.productCode).returns(Future.successful(notFoundLeft))
    val result = Await.result(productSvc.findItem(addProduct.productCode).value, 20 seconds)
    result shouldBe resultNotFound
  }

}