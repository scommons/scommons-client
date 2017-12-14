package scommons.showcase.api.repo

import scommons.api.StatusResponse

import scala.concurrent.Future

trait RepoApi {

  def getRepos: Future[RepoListResp]

  def getRepo(id: Int): Future[RepoResp]

  def createRepo(data: RepoData): Future[RepoResp]

  def updateRepo(data: RepoData): Future[RepoResp]

  def deleteRepo(id: Int): Future[StatusResponse]
}
