package scommons.showcase.api

import scommons.api.StatusResponse
import scommons.api.http.ApiHttpClient
import scommons.showcase.api.failing._
import scommons.showcase.api.repo._

import scala.concurrent.Future
import scala.concurrent.duration._

class ShowcaseApiClient(client: ApiHttpClient)
  extends RepoApi
    with FailingApi {

  ////////////////////////////////////////////////////////////////////////////////////////
  // Repos

  def getRepos: Future[RepoListResp] = {
    client.execGet[RepoListResp]("/repos")
  }

  def getRepo(id: Int): Future[RepoResp] = {
    client.execGet[RepoResp](s"/repos/$id")
  }

  def createRepo(data: RepoData): Future[RepoResp] = {
    client.execPost[RepoData, RepoResp]("/repos", data)
  }

  def updateRepo(data: RepoData): Future[RepoResp] = {
    client.execPut[RepoData, RepoResp]("/repos", data)
  }

  def deleteRepo(id: Int): Future[StatusResponse] = {
    client.execDelete[String, StatusResponse](s"/repos/$id")
  }

  ////////////////////////////////////////////////////////////////////////////////////////
  // Failing

  def timedoutExample(): Future[StatusResponse] = {
    client.execGet[StatusResponse]("/failing/timedout", timeout = 2.seconds)
  }

  def failedExample(): Future[StatusResponse] = {
    client.execGet[StatusResponse]("/failing/failed")
  }
}
