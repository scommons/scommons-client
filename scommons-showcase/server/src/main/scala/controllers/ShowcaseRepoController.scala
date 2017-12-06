package controllers

import play.api.libs.json.JsValue
import play.api.mvc._
import scommons.api.StatusResponse
import scommons.play.controllers.BaseApiController
import scommons.showcase.api.repo._

import scala.concurrent.ExecutionContext

class ShowcaseRepoController(repoApi: RepoApi)
                            (implicit components: ControllerComponents, ec: ExecutionContext)
  extends BaseApiController(components) {

  def getRepos: Action[AnyContent] = {
    apiNoBodyAction[RepoListResp] {
      repoApi.getRepos
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
