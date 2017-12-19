package controllers

import io.swagger.annotations.{Api, ApiOperation}
import play.api.mvc._
import scommons.api.StatusResponse
import scommons.play.controllers.BaseApiController
import scommons.showcase.api.failing.FailingApi

import scala.concurrent.ExecutionContext

/**
  * API controller implementation should not contain any logic.
  *
  * It should redirect all the calls to the appropriate API implementation.
  *
  * It's main responsibility is conversion between raw HTTP data and API data.
  */
@Api(tags = Array("failing"))
class FailingController(failingApi: FailingApi)
                       (implicit components: ControllerComponents, ec: ExecutionContext)
  extends BaseApiController(components) {

  @ApiOperation(value = "Example timed-out endpoint",
    response = classOf[StatusResponse])
  def timedoutExample(): Action[AnyContent] = {
    apiNoBodyAction[StatusResponse] {
      failingApi.timedoutExample()
    }
  }

  @ApiOperation(value = "Example failed endpoint",
    response = classOf[StatusResponse])
  def failedExample(): Action[AnyContent] = {
    apiNoBodyAction[StatusResponse] {
      failingApi.failedExample()
    }
  }
}
