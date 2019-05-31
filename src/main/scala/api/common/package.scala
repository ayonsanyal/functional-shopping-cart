package api

import api.common.entities.{Pure, ResultError}
import cats.data.EitherT

import scala.concurrent.Future

package object common {
  type ServiceResult[A] = EitherT[Future,ResultError,Pure[A]]
}
