package services

import domain.Repo
import domain.dao.RepoDao

import scala.concurrent.Future

/**
  * Service implementation operates on entities level and knows anything about API data.
  *
  * This example service is a simple wrapper around DAO.
  *
  * In a real-word application it can contain more business logic though, for example:
  *   - creating transactions around DAO calls
  *   - sending notifications
  *   - accessing and collecting data from several DAOs
  */
class RepoService(repoDao: RepoDao) {

  def getRepos: Future[List[Repo]] = {
    repoDao.getRepos
  }

  def getRepoById(id: Int): Future[Option[Repo]] = {
    repoDao.getRepoById(id)
  }

  def getRepoByName(name: String): Future[Option[Repo]] = {
    repoDao.getRepoByName(name)
  }

  def createRepo(data: Repo): Future[Repo] = {
    repoDao.createRepo(data)
  }

  def updateRepo(data: Repo): Future[Repo] = {
    repoDao.updateRepo(data)
  }

  def deleteRepo(id: Int): Future[Boolean] = {
    repoDao.deleteRepo(id)
  }
}
