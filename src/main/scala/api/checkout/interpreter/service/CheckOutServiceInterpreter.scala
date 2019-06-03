package api.checkout.interpreter.service

import api.checkout.domain.repository.CheckOutRepository
import api.checkout.domain.service.CheckOutService
import api.checkout.entities.{ CheckOutDetails, OrderId }
import api.common.entities.{ Pure, ResultError }
import api.common.{ ResultToResponseService, ServiceResult }
import api.shoppingcart.domain.service.ShoppingCartService
import api.users.domain.service.UserCreationService
import cats.data.EitherT
import cats.implicits._

import scala.concurrent.{ ExecutionContext, Future }

class CheckOutServiceInterpreter(
  userSvc: UserCreationService,
  shoppingCartService: ShoppingCartService,
  repo: CheckOutRepository)(implicit ec: ExecutionContext)
  extends CheckOutService
  with ResultToResponseService {

  override def checkOut(email: String): ServiceResult[OrderId] = EitherT {
    userSvc.findUser(email).value flatMap {
      case Left(userNotFound) => Future.successful(userNotFound.asLeft[Pure[OrderId]])
      case Right(user) => shoppingCartService.findAllItems().value flatMap {
        case Left(cartError) => Future.successful(cartError.asLeft[Pure[OrderId]])
        case Right(cart) => repo.checkOut(CheckOutDetails(cart.result, user.result)) map {
          case Left(error) => ResultError(error).asLeft[Pure[OrderId]]
          case Right(orderId) => Pure(orderId).asRight[ResultError]
        }
      }
    }
  }

  override def findCheckOutDetails(orderId: OrderId): ServiceResult[CheckOutDetails] = EitherT {
    repo.findCheckOutDetails(orderId).map(transformResult[CheckOutDetails])
  }
}
