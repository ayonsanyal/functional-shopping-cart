package api.common

import api.common.entities.ApiErrors.ServiceError
import api.common.entities.{Pure, ResultError}
import cats.implicits._
trait ResultToResponseService {

  def transformResult[A](input: Either[ServiceError, A]): Either[ResultError, Pure[A]] = {
    input match  {
      case Right(value) => Pure(result = value).asRight[ResultError]

      case Left(value) => ResultError(reason = value).asLeft[Pure[A]]
    }
  }
}
