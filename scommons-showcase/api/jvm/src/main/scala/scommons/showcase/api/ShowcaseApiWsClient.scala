package scommons.showcase.api

import akka.actor.ActorSystem
import scommons.api.StatusResponse
import scommons.api.client.JsonWsClient
import scommons.showcase.api.failing.FailingApi
import scommons.showcase.api.repo._

import scala.concurrent.Future
import scala.concurrent.duration._

class ShowcaseApiWsClient(baseUrl: String)(implicit system: ActorSystem)
  extends JsonWsClient(baseUrl)
    with RepoApi
    with FailingApi {

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

  ////////////////////////////////////////////////////////////////////////////////////////
  // Failing

  def timedoutExample(): Future[StatusResponse] = {
    execGet[StatusResponse]("/failing/timedout", timeout = 2.seconds)
  }

  def failedExample(): Future[StatusResponse] = {
    execGet[StatusResponse]("/failing/failed")
  }
}
