package api.common

import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import api.common.entities._
import cats.implicits._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Encoder
import io.circe.generic.auto._
import io.circe.syntax._

import scala.concurrent.ExecutionContext

trait ApiResponseMapperService extends FailFastCirceSupport with ErrorMessages {

  /**
    * Maps the result from service to api json response based on nature of result.
    * @param serviceResult
    * @param methodType
    * @param ec
    * @param encoder
    * @tparam A
    * @return
    */
  def mapToApiResponse[A](serviceResult: ServiceResult[A], methodType: String)(implicit ec: ExecutionContext, encoder: Encoder[A]): Route =
    onComplete(serviceResult.value) {
      case util.Success(Right(pure)) if methodType == "POST" => complete(ApiResponseSuccess[A](Created.intValue, pure.result.some).asJson)
      case util.Success(Right(pure)) => complete(ApiResponseSuccess[A](OK.intValue, pure.result.some).asJson)

      case util.Success(Left(error: ResultError)) => error.reason match {
        case error: ApiErrors.ValidationError => error match {
          case ApiErrors.AlreadyExists(message) => complete(Error(Conflict.intValue, message.some).asJson)
          case ApiErrors.ResultNotFound(message) => complete(Error(NotFound.intValue, message.some).asJson)
        }
      }

      case util.Failure(exception) => complete(Error(InternalServerError.intValue, SERVER_SIDE_ERROR.some).asJson)
    }

}
