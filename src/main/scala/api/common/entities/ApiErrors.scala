package api.common.entities

object ApiErrors {

  sealed trait ServiceError

  sealed trait ServerSideFailure extends ServiceError

  case class InternalServerError(message: String) extends ServerSideFailure

  sealed trait ValidationError extends ServiceError

  case class AlreadyExists(message: String) extends ValidationError

  case class ResultNotFound(message: String) extends ValidationError

}
