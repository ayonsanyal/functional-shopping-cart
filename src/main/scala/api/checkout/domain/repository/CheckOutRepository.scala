package api.checkout.domain.repository

import api.checkout.entities.{ CheckOutDetails, OrderId }
import api.common.entities.ApiErrors.ServiceError

import scala.concurrent.Future
trait CheckOutRepository {

  def checkOut(checkOut: CheckOutDetails): Future[Either[ServiceError, OrderId]]

  def findCheckOutDetails(orderDetails: OrderId): Future[Either[ServiceError, CheckOutDetails]]
}
