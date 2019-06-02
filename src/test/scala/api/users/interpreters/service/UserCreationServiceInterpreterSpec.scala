package api.users.interpreters.service

import api.UnitSpec
import api.users.UsersTestData
import api.users.domain.repository.UserRepository
import api.users.entity.User
import api.users.interpreter.service.UserCreationServiceInterpreter
import org.scalamock.scalatest.MockFactory

import scala.concurrent.duration._
import scala.concurrent.{ Await, Future }

class UserCreationServiceInterpreterTest
  extends UnitSpec
  with MockFactory
  with UsersTestData {

  import Input._
  import Output._

  val userRepo = stub[UserRepository[User]]

  behavior of "createUser"

  it should "return the ServiceResult with Pure value of User when success" in {
    val userService = new UserCreationServiceInterpreter(userRepo)
    (userRepo.createUser(_)).when(createUser).returns(Future.successful(Right(createUser)))
    val result = Await.result(userService.createUser(createUser).value, 20 seconds)
    result shouldBe userCreated
  }

  it should "return the error message if the User already exists" in {
    val userService = new UserCreationServiceInterpreter(userRepo)
    (userRepo.createUser(_)).when(createUser).returns(Future.successful(alreadyExistsLeft))
    val result = Await.result(userService.createUser(createUser).value, 20 seconds)
    result shouldBe resultInUse
  }

  behavior of "findUser"

  it should "find the user for email if it exists" in {
    val userService = new UserCreationServiceInterpreter(userRepo)
    (userRepo.findUser(_)).when(createUser.email).returns(Future.successful(Right(createUser)))
    val result = Await.result(userService.findUser(createUser.email).value, 20 seconds)
    result shouldBe userCreated
  }

  it should "return error message if the user with email does not exist" in {
    val userService = new UserCreationServiceInterpreter(userRepo)
    (userRepo.findUser(_)).when(createUser.email).returns(Future.successful(notFoundLeft))
    val result = Await.result(userService.findUser(createUser.email).value, 20 seconds)
    result shouldBe resultNotFound
  }

}
