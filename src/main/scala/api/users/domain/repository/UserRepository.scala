package api.users.domain.repository

import api.common.entities.ApiErrors.ServiceError

import scala.concurrent.Future

trait UserRepository[User] {

  def createUser(user: User): Future[Either[ServiceError, User]]

  def findUser(email: String): Future[Either[ServiceError, User]]
}
