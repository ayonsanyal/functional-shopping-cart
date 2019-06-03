package api.users.routes

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ MediaTypes, _ }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import api.common.entities.{ ApiResponseSuccess, Error }
import api.users.UsersTestData
import api.users.domain.service.UserCreationService
import api.users.entity.User
import cats.data.EitherT
import cats.implicits._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec }

class UserRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest
  with MockFactory with BeforeAndAfterAll
  with UsersTestData {

  import FutureData._
  import Input._
  import Output._

  implicit val as = ActorSystem("route-user")
  implicit val ec = as.dispatcher
  implicit val mat = ActorMaterializer()
  val service = stub[UserCreationService]
  val routes = new UserRoutes(service)

  "UserRoutes" should {

    "be able to add users (POST /users) if user is not already registered" in {
      (service.createUser(_)).when(*).returns(
        EitherT(
          createUserResponse))

      val userEntity = HttpEntity(MediaTypes.`application/json`, ByteString(routeRequestUser))
      Post("/api/v1/users", userEntity) ~> routes.userRoutes ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = ApiResponseSuccess[User](Created.intValue, createUser.some)
        responseAs[ApiResponseSuccess[User]] shouldEqual result
      }
    }

    "Not be able to add users add users if user is  already registered" in {
      (service.createUser(_)).when(*).returns(
        EitherT(
          createUserError))

      val userEntity = HttpEntity(MediaTypes.`application/json`, ByteString(routeRequestUser))
      Post("/api/v1/users", userEntity) ~> routes.userRoutes ~> check {
        contentType should ===(ContentTypes.`application/json`)
        val result = Error(Conflict.intValue, alreadyExists.message.some)
        responseAs[Error] shouldEqual result
      }
    }

  }

}

