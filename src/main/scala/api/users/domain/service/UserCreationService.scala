package api.users.domain.service

import api.common.ServiceResult
import api.users.entity.User

trait UserCreationService {

  def createUser(user: User): ServiceResult[User]

  def findUser(email: String): ServiceResult[User]
}
