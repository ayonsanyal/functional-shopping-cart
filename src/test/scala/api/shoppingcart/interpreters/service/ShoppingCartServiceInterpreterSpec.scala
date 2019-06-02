package api.shoppingcart.interpreters.service

import api.UnitSpec
import api.common.entities.{ Pure, ResultError }
import api.products.domain.service.ProductService
import api.products.entities.ProductMetadata
import api.shoppingcart.ShoppingCartTestData
import api.shoppingcart.domain.repository.ShoppingCartRepository
import api.shoppingcart.entities.Products
import api.shoppingcart.interpreter.service.ShoppingCartServiceInterpreter
import cats.data.EitherT
import cats.implicits._
import org.scalamock.scalatest.MockFactory

import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

class ShoppingCartServiceInterpreterSpec extends UnitSpec
  with ShoppingCartTestData with MockFactory {

  import Input._
  import Output._
  val productSvc = stub[ProductService]
  val shoppingCartRepo = stub[ShoppingCartRepository]
  val shoppingCartSvc = new ShoppingCartServiceInterpreter(shoppingCartRepo, productSvc)

  behavior of "addToCart"

  it should "return the added Item to Cart when if the product code is found and is added successfully in the cart" in {
    val addProduct = ProductMetadata("item1", "samsung", 400, "EUR")
    (productSvc.findItem(_)).when(itemInfo.itemCode).returns(EitherT {
      Future.successful(Pure(addProduct).asRight[ResultError])
    })
    (shoppingCartRepo.addToCart(_, _, _)).when(itemInfo, *, *).returns(
      Future.successful(Products(itemInfo, "samsung", 400.0, "EUR")))
    val result = Await.result(shoppingCartSvc.addToCart(itemInfo).value, 20 seconds)
    result shouldBe Pure(Products(itemInfo, "samsung", 400.0, "EUR")).asRight[ResultError]
  }

  it should "return the not found when product is not found and hence cannot be added in the cart" in {
    val addProduct = ProductMetadata("item1", "samsung", 400, "EUR")
    (productSvc.findItem(_)).when(itemInfo.itemCode).returns(EitherT {
      Future.successful(resultNotFound)
    })
    val result = Await.result(shoppingCartSvc.addToCart(itemInfo).value, 20 seconds)
    result shouldBe resultErrorNotFound.asLeft[Pure[Products]]
  }
}