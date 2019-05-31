package api.users.routes

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.post
import api.common.ApiResponseMapperService
import api.users.domain.service.UserCreationService
import api.users.entity.User
import io.circe.generic.auto._
import scala.concurrent.ExecutionContext

//#user-routes-class
class UserRoutes(userService: UserCreationService)(implicit executionContext: ExecutionContext, asystem: ActorSystem)
  extends ApiResponseMapperService {
  //#user-routes-class
  lazy val log = Logging(asystem, classOf[UserRoutes])

  lazy val userRoutes: Route =
    pathPrefix("api") {
      pathPrefix("v1") {
        pathPrefix("users") {
          //#users-get
          pathEnd {
            post {
              entity(as[User]) { user =>
                mapToApiResponse(userService.createUser(user), "POST")
              }
            }
          }
        }
      }
    }

}