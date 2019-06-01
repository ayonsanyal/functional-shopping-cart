package api.products.domain.service

import api.common.ServiceResult
import api.products.entities.ProductMetadata

trait ProductService {

  def addItem(item: ProductMetadata): ServiceResult[ProductMetadata]

  def findItem(itemCategory: String): ServiceResult[ProductMetadata]
}
