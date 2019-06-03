package api.users

import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound }
import api.common.entities.{ ErrorMessages, Pure, ResultError }
import api.users.entity.{ Address, User, UserErrorMessages }
import cats.implicits._

import scala.concurrent.Future

trait UsersTestData extends UserErrorMessages {

  object Input {
    val createUser = User("ayon", "ayon.sanyal@mail.com", Address("Street1", 70, 22087, "Germany"))
  }

  object Output {
    import Input._
    val userCreated = Pure(createUser).asRight[ResultError]
    val userFound = createUser.asRight[ResultError]
    val alreadyExists = AlreadyExists(ALREADY_EXISTS)
    val notFound = ResultNotFound(RESULT_NOT_FOUND)

    val resultErrorNotFound = ResultError(notFound)
    val resultErrorInUse = ResultError(alreadyExists)

    val alreadyExistsLeft = Left(alreadyExists)
    val notFoundLeft = Left(notFound)

    val resultNotFound = resultErrorNotFound.asLeft[Pure[User]]
    val resultInUse = resultErrorInUse.asLeft[Pure[User]]

    val routeRequestUser =
      """
        |{
        |"name" : "ayon",
        |"email" : "ayon.sanyal@mail.com",
        |"address" : {
        |"street" : "Street1",
        |"houseNumber" : 70,
        |"postalCode" : 22807,
        |"country" : "Germany"
        |}
        |}
        |
        """.stripMargin
  }

  object FutureData {

    import Input._
    import Output._

    val createUserResponse: Future[Either[ResultError, Pure[User]]] =
      Future.successful(Pure(createUser).asRight[ResultError])
    val createUserError: Future[Either[ResultError, Pure[User]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[User]])

    val findUserRight: Future[Either[ResultError, Pure[User]]] =
      Future.successful(Pure(createUser).asRight[ResultError])
    val findUserLeft: Future[Either[ResultError, Pure[User]]] =
      Future.successful(resultNotFound)
  }

}
