package api.users.interpreters.repository

import api.UnitSpec
import api.users.UsersTestData
import api.users.interpreter.repository.UserRepositoryInterpreter

import scala.concurrent.Await
import scala.concurrent.duration._

class UserRepositoryInterpreterSpec
  extends UnitSpec
  with UsersTestData {

  import Input._
  import Output._

  behavior of "createUser"

  it should "store the post if it is not already stored" in {
    val userRepository = new UserRepositoryInterpreter()
    val result = Await.result(userRepository.createUser(createUser), 20 seconds)
    result shouldBe Right(createUser)
  }

  it should "not store the post if it is already stored" in {
    val userRepository = new UserRepositoryInterpreter()
    Await.result(userRepository.createUser(createUser), 20 seconds)
    val result = Await.result(userRepository.createUser(createUser), 20 seconds)
    result shouldBe alreadyExistsLeft
  }

  behavior of "findItem"

  it should "return user details if it is found " in {
    val userRepository = new UserRepositoryInterpreter()
    Await.result(userRepository.createUser(createUser), 20 seconds)
    val result = Await.result(userRepository.findUser(createUser.email), 20 seconds)
    result shouldBe userFound
  }

  it should "return not found if user is not found with email" in {
    val userRepository = new UserRepositoryInterpreter()
    val result = Await.result(userRepository.findUser(createUser.email), 20 seconds)
    result shouldBe notFoundLeft
  }

}
