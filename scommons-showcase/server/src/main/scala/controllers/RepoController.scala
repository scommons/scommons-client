package controllers

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
class RepoController(repoApi: RepoApi)
                    (implicit components: ControllerComponents, ec: ExecutionContext)
  extends BaseApiController(components) {

  def getRepos: Action[AnyContent] = {
    apiNoBodyAction[RepoListResp] {
      repoApi.getRepos
    }
  }

  def getRepo(id: Int): Action[AnyContent] = {
    apiNoBodyAction[RepoResp] {
      repoApi.getRepo(id)
    }
  }

  def createRepo(): Action[JsValue] = {
    apiAction[RepoData, RepoResp] { data =>
      repoApi.createRepo(data)
    }
  }

  def updateRepo(): Action[JsValue] = {
    apiAction[RepoData, RepoResp] { data =>
      repoApi.updateRepo(data)
    }
  }

  def deleteRepo(id: Int): Action[AnyContent] = {
    apiNoBodyAction[StatusResponse] {
      repoApi.deleteRepo(id)
    }
  }
}
