package api.users.routes

//#user-routes-spec
//#test-top
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{MediaTypes, _}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import api.common.entities.ApiResponseSuccess
import api.users.UsersTestData
import api.users.domain.service.UserCreationService
import api.users.entity.User
import cats.data.EitherT
import cats.implicits._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}
//#set-up
class UserRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest
  with MockFactory with BeforeAndAfterAll
 with UsersTestData{

  import FutureData._
  import Input._
  import Output._

  implicit val as = ActorSystem("Test")
  implicit val ec = as.dispatcher
  implicit val mat = ActorMaterializer()
  val service = stub[UserCreationService]
  val routes = new UserRoutes(service)

  "UserRoutes" should {

    "be able to add users (POST /users) if user is not already registered" in {
      val userEntity = HttpEntity(MediaTypes.`application/json`, ByteString(routeRequestUser)) // futureValue is from ScalaFutures
      (service.createUser(_)).when(createUser).returns(
        EitherT(
          createUserResponse))
      Post("/api/v1/users", userEntity) ~> routes.userRoutes ~> check {
        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)
        val result = ApiResponseSuccess[User](Created.intValue, createUser.some)
        // and we know what message we're expecting back:
        responseAs[ApiResponseSuccess[User]] shouldEqual result
      }
    }

  }

}

