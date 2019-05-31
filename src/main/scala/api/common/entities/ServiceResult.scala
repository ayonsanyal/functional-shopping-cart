package api.common.entities

import api.common.entities.ApiErrors.ServiceError

case class Pure[T](result: T)
case class ResultError(reason: ServiceError)
