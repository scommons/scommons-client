package controllers

import io.swagger.annotations._
import play.api.libs.json.JsValue
import play.api.mvc._
import scommons.api.StatusResponse
import scommons.play.controllers.BaseApiController
import scommons.showcase.api.repo._

import scala.concurrent.ExecutionContext

/**
  * API controller implementation should not contain any logic.
  *
  * It should redirect all the calls to the appropriate API implementation.
  *
  * It's main responsibility is conversion between raw HTTP data and API data.
  */
@Api(tags = Array("repos"))
class RepoController(repoApi: RepoApi)
                    (implicit components: ControllerComponents, ec: ExecutionContext)
  extends BaseApiController(components) {

  @ApiOperation(value = "Returns repos list",
    response = classOf[RepoListResp])
  def getRepos: Action[AnyContent] = {
    apiNoBodyAction[RepoListResp] {
      repoApi.getRepos
    }
  }

  @ApiOperation(value = "Returns repo by id",
    response = classOf[RepoResp])
  def getRepo(@ApiParam("repo id") id: Int): Action[AnyContent] = {
    apiNoBodyAction[RepoResp] {
      repoApi.getRepo(id)
    }
  }

  @ApiOperation(value = "Creates repo",
    response = classOf[RepoResp])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", paramType = "body", required = true,
      dataType = "scommons.showcase.api.repo.RepoData")
  ))
  def createRepo(): Action[JsValue] = {
    apiAction[RepoData, RepoResp] { data =>
      repoApi.createRepo(data)
    }
  }

  @ApiOperation(value = "Updates repo",
    response = classOf[RepoResp])
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "body", paramType = "body", required = true,
      dataType = "scommons.showcase.api.repo.RepoData")
  ))
  def updateRepo(): Action[JsValue] = {
    apiAction[RepoData, RepoResp] { data =>
      repoApi.updateRepo(data)
    }
  }

  @ApiOperation(value = "Deletes repo by id",
    response = classOf[StatusResponse])
  def deleteRepo(@ApiParam("repo id") id: Int): Action[AnyContent] = {
    apiNoBodyAction[StatusResponse] {
      repoApi.deleteRepo(id)
    }
  }
}
