package apis

import apis.ApiStatuses._
import apis.RepoApiImpl._
import domain.Repo
import scommons.api.StatusResponse
import scommons.play.apis.ex.NotFoundStatusException
import scommons.showcase.api.repo._
import services.RepoService

import scala.concurrent.{ExecutionContext, Future}

/**
  * API implementation class usually doesn't contain much business logic.
  *
  * It is a bridge between controller and service.
  *
  * It's main responsibilities are:
  *   - validation of request data
  *   - calling appropriate service methods
  *   - conversion between API data and domain entities
  */
class RepoApiImpl(service: RepoService)(implicit ec: ExecutionContext) extends RepoApi {

  def getRepos: Future[RepoListResp] = {
    service.getRepos.map { list =>
      RepoListResp(list.map(convertToRepoData))
    }
  }

  def getRepo(id: Int): Future[RepoResp] = {
    service.getRepoById(id).map {
      case None => throw new NotFoundStatusException(RepoNotFound)
      case Some(entity) => RepoResp(convertToRepoData(entity))
    }
  }

  def createRepo(data: RepoData): Future[RepoResp] = {
    validateRepoData(false, data, { entity =>
      service.createRepo(entity).map { repo =>
        RepoResp(convertToRepoData(repo))
      }
    })
  }

  def updateRepo(data: RepoData): Future[RepoResp] = {
    validateRepoData(true, data, { entity =>
      service.updateRepo(entity).map { repo =>
        RepoResp(convertToRepoData(repo))
      }
    })
  }

  def deleteRepo(id: Int): Future[StatusResponse] = {
    service.deleteRepo(id).map {
      case false => throw new NotFoundStatusException(RepoNotFound)
      case true => StatusResponse.Ok
    }
  }

  private def validateRepoData(update: Boolean,
                               data: RepoData,
                               onSuccess: Repo => Future[RepoResp]): Future[RepoResp] = {

    val entity = convertToRepo(data)
    if (entity.name.isEmpty) {
      throw new IllegalArgumentException("name is blank")
    }

    def getById(data: RepoData) = data.id match {
      case Some(id) if update => service.getRepoById(id)
      case _ => Future.successful(None)
    }

    def getByName(current: Option[Repo], entity: Repo): Future[Option[Repo]] = current match {
      case Some(curr) if curr.name == entity.name => Future.successful(None)
      case _ => service.getRepoByName(entity.name)
    }

    getById(data).flatMap { current =>
      if (current.isEmpty && update) Future.successful(RepoResp(RepoNotFound))
      else {
        Future.sequence(List(
          getByName(current, entity)
        )).flatMap {
          case List(Some(_)) => Future.successful(RepoResp(RepoAlreadyExists))
          case List(None) => onSuccess(entity)
        }
      }
    }
  }
}

object RepoApiImpl {

  def convertToRepoData(c: Repo): RepoData = RepoData(
    Some(c.id),
    c.name
  )

  def convertToRepo(data: RepoData): Repo = Repo(
    data.id.getOrElse(-1),
    data.name.trim
  )
}
