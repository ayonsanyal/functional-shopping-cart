package api.checkout.entities

import api.common.entities.ErrorMessages

trait CheckOutErrorMessages extends ErrorMessages {
  override val RESULT_NOT_FOUND: String = "Could not find the order details"
}
