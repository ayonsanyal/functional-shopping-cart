package api.users.routes

//#user-routes-spec
//#test-top
import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.{ MediaTypes, _ }
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.ActorMaterializer
import akka.util.ByteString
import api.common.entities.{ ApiResponseSuccess, Pure, ResultError }
import api.users.domain.service.UserCreationService
import api.users.entity.{ Address, User }
import api.users.routes.UserRoutes
import cats.data.EitherT
import cats.implicits._
import io.circe.generic.auto._
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpec }
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import scala.concurrent.Future
//#set-up
class UserRoutesSpec extends WordSpec with Matchers with ScalatestRouteTest
  with MockFactory with BeforeAndAfterAll {
  implicit val as = ActorSystem("Test")
  implicit val ec = as.dispatcher
  implicit val mat = ActorMaterializer()
  val service = stub[UserCreationService]
  val routes = new UserRoutes(service)

  //#set-up

  //#actual-test
  "UserRoutes" should {

    //#actual-test

    //#testing-post
    "be able to add users (POST /users) if user is not alredy registered" in {
      val request2 =
        """
          |{
          |"name" : "ayon",
          |"email" : "ayon.sanyal@mail.com",
          |"address" : {
          |"street" : "Street1",
          |"houseNumber" : 70,
          |"postalCode" : 22807,
          |"country" : "Germany"
          |}
          |}
          |
        """.stripMargin
      val user = User("ayon", "ayon.sanyal@mail.com", Address("Street1", 70, 22087, "Germany"))
      val userEntity = HttpEntity(MediaTypes.`application/json`, ByteString(request2)) // futureValue is from ScalaFutures
      (service.createUser(_)).when(*).returns(
        EitherT(
          Future.successful(Pure(user).asRight[ResultError])))
      Post("/api/v1/users", userEntity) ~> routes.userRoutes ~> check {
        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)
        val result = ApiResponseSuccess[User](Created.intValue, user.some)
        // and we know what message we're expecting back:
        responseAs[ApiResponseSuccess[User]] shouldEqual result
      }
    }

  }

}

