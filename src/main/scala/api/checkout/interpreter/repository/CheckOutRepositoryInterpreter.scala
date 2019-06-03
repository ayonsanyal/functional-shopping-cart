package api.checkout.interpreter.repository

import api.checkout.domain.repository.CheckOutRepository
import api.checkout.entities.{ CheckOutDetails, OrderId }
import api.common.entities.ApiErrors.{ AlreadyExists, ResultNotFound, ServiceError }
import api.common.entities.{ ApiErrors, ErrorMessages }
import cats.implicits._

import scala.collection.mutable
import scala.concurrent.{ ExecutionContext, Future }

class CheckOutRepositoryInterpreter(implicit ec: ExecutionContext) extends CheckOutRepository
  with ErrorMessages {

  /**
   * This is the place where all checkOut details are stored.
   */
  private val checkOutItems: mutable.TreeMap[String, CheckOutDetails] = mutable.TreeMap.empty

  override def checkOut(checkOutDetails: CheckOutDetails): Future[Either[ServiceError, OrderId]] = Future {
    val orderIdRandom = scala.util.Random
    lazy val orderIdRndm = orderIdRandom.nextInt(10000)
    val orederId = s"ORD${orderIdRndm}"
    if (checkOutItems.contains(orederId)) AlreadyExists(ALREADY_EXISTS).asLeft[OrderId]
    else {
      checkOutItems += (orederId -> checkOutDetails)
      OrderId(orederId).asRight[ServiceError]
    }
  }

  override def findCheckOutDetails(orderId: OrderId): Future[Either[ApiErrors.ServiceError, CheckOutDetails]] = Future {
    checkOutItems.get(orderId.orderId) match {
      case Some(item) => item.asRight[ServiceError]
      case None => ResultNotFound(RESULT_NOT_FOUND).asLeft[CheckOutDetails]
    }
  }
}
