package api

import org.scalatest.{ FlatSpec, Matchers }

import scala.concurrent.ExecutionContext

trait UnitSpec extends FlatSpec with Matchers {
  implicit val ec = ExecutionContext.global
}
