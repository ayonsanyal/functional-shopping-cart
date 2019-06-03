package api.checkout.interpreters.service

import api.UnitSpec
import api.checkout.CheckOutTestData
import api.checkout.domain.repository.CheckOutRepository
import api.checkout.interpreter.service.CheckOutServiceInterpreter
import api.common.entities.ApiErrors.ResultNotFound
import api.common.entities.ResultError
import api.shoppingcart.domain.service.ShoppingCartService
import api.users.domain.service.UserCreationService
import cats.data.EitherT
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Await
import scala.concurrent.duration._

class CheckOutServiceInterpreterSpec extends UnitSpec
  with CheckOutTestData with MockFactory {

  import FutureData._
  import Input._
  import Output._

  val userSvc = stub[UserCreationService]
  val shoppingCartSvc = stub[ShoppingCartService]
  val checkOutRepo = stub[CheckOutRepository]

  val checkOutSvc = new CheckOutServiceInterpreter(userSvc, shoppingCartSvc, checkOutRepo)

  behavior of "checkOut"

  it should "CheckOut the items in shopping cart if the user exists, and the shopping cart is not empty" in {
    (userSvc.findUser(_)).when(*).returns(
      EitherT(userFound))
    (() => shoppingCartSvc.findAllItems()).when().returns(EitherT(
      shoppingCartFound))

    (checkOutRepo.checkOut(_)).when(*).returns(checkOutDetailsFuture)

    val result = Await.result(checkOutSvc.checkOut(userDetails.email).value, 20 seconds)
    result shouldBe orderIdCreated
  }

  it should "Not CheckOut the items in shopping cart if the user does not exists" in {
    (userSvc.findUser(_)).when(*).returns(
      EitherT(userNotFound))
    val result = Await.result(checkOutSvc.checkOut("invalidemail@mail.com").value, 20 seconds)
    result shouldBe Left(ResultError(ResultNotFound("User Not Found")))
  }

  it should "Not checkOut the items in shopping cart if the user is found but shopping cart is empty " in {
    (userSvc.findUser(_)).when(*).returns(
      EitherT(userFound))
    (() => shoppingCartSvc.findAllItems()).when().returns(EitherT(
      shoppingCartNotFound))
    val result = Await.result(checkOutSvc.checkOut(userDetails.email).value, 20 seconds)
    result shouldBe Left(ResultError(ResultNotFound("Shopping cart is empty,so cannot checkout")))
  }
}