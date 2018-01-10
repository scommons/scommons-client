package scommons.showcase.client.demo

import org.scalajs.dom
import scommons.api.{ApiStatus, StatusResponse}
import scommons.showcase.api.failing._
import scommons.showcase.api.repo._

import scala.collection.mutable
import scala.concurrent.duration._
import scala.concurrent.{Future, Promise}

class ApiStubClient
  extends RepoApi
  with FailingApi {

  private val repoById = new mutable.HashMap[Int, RepoData]
  private var nextId = 0

  ////////////////////////////////////////////////////////////////////////////////////////
  // Repos

  def getRepos: Future[RepoListResp] = {
    Future.successful(RepoListResp(repoById.values.toList))
  }

  def getRepo(id: Int): Future[RepoResp] = {
    repoById.get(id) match {
      case None => Future.successful(RepoResp(ApiStatus(404, s"Repo with id $id not found")))
      case Some(data) => Future.successful(RepoResp(data))
    }
  }

  def createRepo(data: RepoData): Future[RepoResp] = {
    repoById.values.find(_.name == data.name) match {
      case Some(_) => Future.successful(RepoResp(
        ApiStatus(409, s"Repo with name ${data.name} already exists")
      ))
      case _ =>
        nextId += 1
        val created = data.copy(id = Some(nextId))
        repoById.put(nextId, created)

        Future.successful(RepoResp(created))
    }
  }

  def updateRepo(data: RepoData): Future[RepoResp] = {
    repoById.values.find(_.name == data.name) match {
      case Some(existing) if existing.id != data.id => Future.successful(RepoResp(
        ApiStatus(409, s"Repo with name ${data.name} already exists")
      ))
      case _ => data.id match {
        case None => Future.successful(RepoResp(
          ApiStatus(400, "Repo id is not specified")
        ))
        case Some(id) =>
          repoById.get(id) match {
            case None => Future.successful(RepoResp(ApiStatus(404, s"Repo with id $id not found")))
            case Some(existing) =>
              val updated = existing.copy(name = data.name)
              repoById.put(id, updated)

              Future.successful(RepoResp(updated))
          }
      }
    }
  }

  def deleteRepo(id: Int): Future[StatusResponse] = {
    repoById.remove(id) match {
      case None => Future.successful(StatusResponse(ApiStatus(404, s"Repo with id $id not found")))
      case _ => Future.successful(StatusResponse.Ok)
    }
  }

  ////////////////////////////////////////////////////////////////////////////////////////
  // Failing

  def timedoutExample(): Future[StatusResponse] = {
    val promise = Promise[StatusResponse]()

    var handleId = 0
    handleId = dom.window.setTimeout({ () =>
      dom.window.clearTimeout(handleId)

      promise.failure(new Exception("Request timed out, unable to get timely response"))
    }, 2.seconds.toMillis.toDouble)

    promise.future
  }

  def failedExample(): Future[StatusResponse] = {
    Future.successful(StatusResponse(
      ApiStatus(500, "Internal Server Error", "Some error occurred, try again later)")
    ))
  }
}
