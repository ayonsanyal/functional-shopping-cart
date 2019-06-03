package api.users.entity

import api.common.entities.ErrorMessages

trait UserErrorMessages extends ErrorMessages {

  override val RESULT_NOT_FOUND: String = "User Not Found"
}
