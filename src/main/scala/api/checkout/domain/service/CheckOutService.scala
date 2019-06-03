package api.checkout.domain.service

import api.checkout.entities.{ CheckOutDetails, OrderId }
import api.common.ServiceResult

trait CheckOutService {

  def checkOut(email: String): ServiceResult[OrderId]

  def findCheckOutDetails(orderId: OrderId): ServiceResult[CheckOutDetails]
}
