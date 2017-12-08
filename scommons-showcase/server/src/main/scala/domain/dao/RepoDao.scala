package domain.dao

import java.util.concurrent.atomic.AtomicInteger

import domain.Repo

import scala.collection.concurrent.TrieMap
import scala.concurrent.Future

/**
  * The data access object (DAO) implementation usually operates with single entity.
  *
  * Normally it should not open any transactions internally, this responsibility is of the upper service.
  *
  * This example uses in-memory storage.
  */
class RepoDao {

  private val nextId = new AtomicInteger(0)

  private val indexByName = TrieMap.empty[String, Int]

  private val storage = TrieMap.empty[Int, Repo]

  def getRepos: Future[List[Repo]] = {
    val list = storage.values.toList.sortBy(_.name)

    Future.successful(list)
  }

  def getRepoById(id: Int): Future[Option[Repo]] = {
    Future.successful(storage.get(id))
  }

  def getRepoByName(name: String): Future[Option[Repo]] = {
    val result = indexByName.get(name).flatMap { id =>
      storage.get(id)
    }

    Future.successful(result)
  }

  def createRepo(data: Repo): Future[Repo] = {
    val id = nextId.incrementAndGet()

    indexByName.get(data.name) match {
      case Some(existingId) =>
        Future.failed(new IllegalStateException(
          s"Repo with name '${data.name}' already exists, existing repoId: $existingId"
        ))
      case None =>
        val createdRepo = data.copy(id = id)

        storage.putIfAbsent(id, createdRepo) match {
          case Some(_) =>
            Future.failed(new IllegalStateException(s"Repo with id '$id' already exists"))
          case None =>
            indexByName.put(data.name, id)
            Future.successful(createdRepo)
        }
    }
  }

  def updateRepo(data: Repo): Future[Repo] = {
    storage.put(data.id, data) match {
      case Some(existing) =>
        indexByName.remove(existing.name)
        indexByName.put(data.name, data.id)
        Future.successful(data)
      case None =>
        Future.failed(new IllegalStateException(s"Repo with id '${data.id}' not found"))
    }
  }

  def deleteRepo(id: Int): Future[Boolean] = {
    val result = storage.remove(id) match {
      case None => false
      case Some(_) => true
    }

    Future.successful(result)
  }
}
