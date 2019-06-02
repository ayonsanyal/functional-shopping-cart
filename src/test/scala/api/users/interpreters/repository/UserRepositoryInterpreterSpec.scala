package api.users.interpreters.repository

import api.UnitSpec
import api.users.UsersTestData
import api.users.interpreter.repository.UserRepositoryInterpreter
import org.scalamock.scalatest.MockFactory

import scala.concurrent.Await
import concurrent.duration._

class UserRepositoryInterpreterSpec
  extends UnitSpec
  with UsersTestData {

  import Input._
  import Output._

  val userRepository = new UserRepositoryInterpreter()
  behavior of "createUser"

  it should "store the post if it is not already stored" in {
    val result = Await.result(userRepository.createUser(createUser), 20 seconds)
    result shouldBe Right(createUser)
  }

  it should "not store the post if it is already stored" in {
    val result = Await.result(userRepository.createUser(createUser), 20 seconds)
    result shouldBe alreadyExistsLeft
  }
}
