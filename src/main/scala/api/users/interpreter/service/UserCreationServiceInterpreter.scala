package api.users.interpreter.service

import api.common.{ ResultToResponseService, ServiceResult }
import api.users.domain.repository.UserRepository
import api.users.domain.service.UserCreationService
import api.users.entity.User
import cats.data.EitherT

import scala.concurrent.ExecutionContext

class UserCreationServiceInterpreter(repo: UserRepository[User])(implicit ec: ExecutionContext)
  extends UserCreationService
  with ResultToResponseService {
  override def createUser(user: User): ServiceResult[User] = EitherT {
    repo.createUser(user).map(transformResult[User])
  }

  override def findUser(email: String): ServiceResult[User] = EitherT {
    repo.findUser(email).map(transformResult[User])
  }
}
