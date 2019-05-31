package api.common.entities

case class ApiResponseSuccess[T](status: Int, data: Option[T])
case class Error(status: Int, message: Option[String] = None)
