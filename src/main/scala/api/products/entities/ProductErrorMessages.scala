package api.products.entities

import api.common.entities.ErrorMessages

trait ProductErrorMessages extends ErrorMessages {
  override val RESULT_NOT_FOUND: String = "Product not found for this code"
}
