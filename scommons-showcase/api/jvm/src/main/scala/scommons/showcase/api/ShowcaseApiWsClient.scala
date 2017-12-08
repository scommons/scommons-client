package scommons.showcase.api

import akka.actor.ActorSystem
import scommons.api.StatusResponse
import scommons.api.client.JsonWsClient
import scommons.showcase.api.repo._

import scala.concurrent.Future

class ShowcaseApiWsClient(baseUrl: String)(implicit system: ActorSystem)
  extends JsonWsClient(baseUrl)
    with RepoApi {

  ////////////////////////////////////////////////////////////////////////////////////////
  // Repos

  def getRepos: Future[RepoListResp] = {
    execGet[RepoListResp]("/repos")
  }

  def getRepo(id: Int): Future[RepoResp] = {
    execGet[RepoResp](s"/repos/$id")
  }

  def createRepo(data: RepoData): Future[RepoResp] = {
    execPost[RepoData, RepoResp]("/repos", data)
  }

  def updateRepo(data: RepoData): Future[RepoResp] = {
    execPut[RepoData, RepoResp]("/repos", data)
  }

  def deleteRepo(id: Int): Future[StatusResponse] = {
    execDelete[String, StatusResponse](s"/repos/$id")
  }
}
