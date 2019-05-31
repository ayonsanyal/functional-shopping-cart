package api.users

import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound }
import api.common.entities.{ ErrorMessages, Pure, ResultError }
import api.users.entity.{ Address, User }
import cats.implicits._

import scala.concurrent.Future

trait UsersTestData extends ErrorMessages {

  object Input {
    val createUser = User("ayon", "ayon.sanyal@mail.com", Address("Street1", 70, 22087, "Germany"))
  }

  object Output {
    import Input._
    val userCreated = Pure(createUser).asRight[ResultError]
    val alreadyExists = AlreadyExists(ALREADY_EXISTS)
    val notFound = ResultNotFound(RESULT_NOT_FOUND)

    val resultErrorNotFound = ResultError(notFound)
    val resultErrorInUse = ResultError(alreadyExists)

    val alreadyExistsLeft = Left(alreadyExists)
    val notFoundLeft = Left(notFound)

    val resultNotFound = resultErrorNotFound.asLeft[Pure[User]]
    val resultInUse = resultErrorInUse.asLeft[Pure[User]]
  }

  object FutureData {

    import Input._
    import Output._

    val createPostResponse: Future[Either[ResultError, Pure[User]]] =
      Future.successful(Pure(createUser).asRight[ResultError])
    val createPostError: Future[Either[ResultError, Pure[User]]] =
      Future.successful(resultErrorInUse.asLeft[Pure[User]])

    val findPostRight: Future[Either[ResultError, Pure[User]]] =
      Future.successful(Pure(createUser).asRight[ResultError])
    val findPostLeft: Future[Either[ResultError, Pure[User]]] =
      Future.successful(resultNotFound)
  }

}
