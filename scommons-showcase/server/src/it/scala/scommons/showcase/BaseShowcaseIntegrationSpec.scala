package scommons.showcase

import java.util.UUID

import org.scalatest._
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatestplus.play.ConfiguredServer
import play.api.libs.ws.WSClient
import scommons.api.{ApiStatus, StatusResponse}
import scommons.showcase.api.ShowcaseApiClient
import scommons.showcase.api.repo._

import scala.concurrent.Future
import scala.reflect.ClassTag

trait BaseShowcaseIntegrationSpec extends FlatSpec
  with Matchers
  with ConfiguredServer
  with ScalaFutures
  with Inside
  with Eventually
  with BeforeAndAfterEach {

  implicit val defaultPatience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(100, Millis))

  private def inject[T: ClassTag]: T = app.injector.instanceOf[T]

  implicit lazy val wsClient: WSClient = inject[WSClient]

  private lazy val apiClient = inject[ShowcaseApiClient]

  ////////////////////////////////////////////////////////////////////////////////////////
  // repos

  def createRandomRepo(): RepoData = {
    callRepoCreate(RepoData(None,
      s"${UUID.randomUUID()} random name"
    ))
  }

  def callRepoGetById(id: Int): RepoData = {
    callRepoGetById(id, ApiStatus.Ok).get
  }

  def callRepoGetById(id: Int, expectedStatus: ApiStatus): Option[RepoData] = {
    val resp = apiClient.getRepo(id).futureValue
    resp.status shouldBe expectedStatus
    resp.data
  }

  def callRepoList(): List[RepoData] = {
    val resp = apiClient.getRepos.futureValue
    resp.status shouldBe ApiStatus.Ok
    resp.dataList.get
  }

  def callRepoCreate(data: RepoData): RepoData = {
    callRepoCreate(data, ApiStatus.Ok).get
  }

  def callRepoCreate(data: RepoData, expectedStatus: ApiStatus): Option[RepoData] = {
    val resp = apiClient.createRepo(data).futureValue
    resp.status shouldBe expectedStatus
    resp.data
  }

  def callRepoUpdate(data: RepoData): RepoData = {
    callRepoUpdate(data, ApiStatus.Ok).get
  }

  def callRepoUpdate(data: RepoData, expectedStatus: ApiStatus): Option[RepoData] = {
    val resp = apiClient.updateRepo(data).futureValue
    resp.status shouldBe expectedStatus
    resp.data
  }

  def callRepoDelete(id: Int): ApiStatus = {
    val resp = apiClient.deleteRepo(id).futureValue
    resp.status
  }

  ////////////////////////////////////////////////////////////////////////////////////////
  // failing

  def callFailingTimedout(): Future[StatusResponse] = {
    apiClient.timedoutExample()
  }

  def callFailingFailed(): Future[StatusResponse] = {
    apiClient.failedExample()
  }
}
