package api

import api.common.entities.{ Pure, ResultError }
import cats.data.EitherT

import scala.concurrent.Future

package object common {
  /**
    * A type which defines contract of result every service will have.It includes Pure which wraps
    * pure result and ResultError which wraps any error type.This composition is wrapped in EitherT monad .
    * @tparam A
    */
  type ServiceResult[A] = EitherT[Future, ResultError, Pure[A]]
}
