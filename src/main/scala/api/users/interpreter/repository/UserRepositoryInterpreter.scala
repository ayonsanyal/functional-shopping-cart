package api.users.interpreter.repository

import api.common.entities.ApiErrors
import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound, ServiceError }
import api.users.domain.repository.UserRepository
import api.users.entity.{ User, UserErrorMessages }
import cats.implicits._

import scala.collection.mutable
import scala.concurrent.{ ExecutionContext, Future }
class UserRepositoryInterpreter(implicit executionContext: ExecutionContext)
  extends UserRepository[User]
  with UserErrorMessages {

  /**
   * This is the place where all user details are stored.
   */
  private val users: mutable.TreeMap[String, User] = mutable.TreeMap.empty

  override def createUser(user: User): Future[Either[ApiErrors.ServiceError, User]] =
    Future {
      if (users.contains(user.email)) {
        AlreadyExists(ALREADY_EXISTS).asLeft[User]
      } else {
        users += (user.email -> user)
        user.asRight[ServiceError]
      }
    }

  override def findUser(email: String): Future[Either[ServiceError, User]] =
    Future {
      users.get(email) match {
        case Some(value) => value.asRight[ServiceError]
        case None => ResultNotFound(RESULT_NOT_FOUND).asLeft[User]
      }
    }
}

