package scommons.showcase

import java.util.UUID

import apis.ApiStatuses._
import org.scalatest.DoNotDiscover
import scommons.api.ApiStatus
import scommons.showcase.api.repo.RepoData

@DoNotDiscover
class RepoApiIntegrationSpec extends BaseShowcaseIntegrationSpec {

  private def removeAllRepos(): Unit = {
    callRepoList().foreach { repo =>
      callRepoDelete(repo.id.get)
    }
  }

  "getRepo" should "fail if no such client" in {
    //when & then
    callRepoGetById(12345, RepoNotFound) shouldBe None
  }

  it should "return list of repos ordered by name" in {
    //given
    removeAllRepos()

    val repo1 = createRandomRepo()
    val repo2 = createRandomRepo()
    val repo3 = createRandomRepo()

    //when & then
    callRepoList() shouldBe List(repo1, repo2, repo3).sortBy(_.name)
  }

  "createRepo" should "fail if repo with such name already exists" in {
    //given
    val existing = createRandomRepo()
    val data = RepoData(
      None,
      existing.name
    )

    //when & then
    callRepoCreate(data, RepoAlreadyExists) shouldBe None
  }

  it should "create new repo" in {
    //given
    val data = RepoData(
      None,
      s"  ${UUID.randomUUID()}  "
    )

    //when
    val result = callRepoCreate(data)

    //then
    result.id should not be None
    result.name shouldBe data.name.trim

    assertRepo(result, callRepoGetById(result.id.get))
  }

  "updateRepo" should "fail if repo not exists" in {
    //given
    val data = RepoData(
      Some(12345),
      s"${UUID.randomUUID()}"
    )

    //when & then
    callRepoUpdate(data, RepoNotFound) shouldBe None
  }

  it should "fail if repo with such name already exists" in {
    //given
    val existing = createRandomRepo()
    val data = RepoData(
      createRandomRepo().id,
      existing.name
    )

    //when & then
    callRepoUpdate(data, RepoAlreadyExists) shouldBe None
  }

  it should "update existing repo" in {
    //given
    val existing = createRandomRepo()
    val data = RepoData(
      existing.id,
      s"  ${UUID.randomUUID()}  "
    )

    //when
    val result = callRepoUpdate(data)

    //then
    result.id shouldBe existing.id
    result.name shouldBe data.name.trim

    assertRepo(result, callRepoGetById(result.id.get))
  }

  "deleteRepo" should "fail if repo not exists" in {
    //given
    val data = RepoData(
      Some(12345),
      s"${UUID.randomUUID()}"
    )

    //when & then
    callRepoDelete(data.id.get) shouldBe RepoNotFound
  }

  it should "delete existing repo" in {
    //given
    val existing = createRandomRepo()

    //when
    val result = callRepoDelete(existing.id.get)

    //then
    result shouldBe ApiStatus.Ok

    callRepoGetById(existing.id.get, RepoNotFound) shouldBe None
  }

  private def assertRepo(repo: RepoData, expected: RepoData): Unit = {
    inside (repo) {
      case RepoData(id, name) =>
        id shouldBe expected.id
        name shouldBe expected.name
    }
  }
}
