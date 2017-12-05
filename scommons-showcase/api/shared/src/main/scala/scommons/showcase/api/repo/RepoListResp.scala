package scommons.showcase.api.repo

import play.api.libs.json._
import scommons.api.{ApiStatus, DataListResponse}

case class RepoListResp private(status: ApiStatus,
                                dataList: Option[List[RepoData]]
                               ) extends DataListResponse[RepoData]

object RepoListResp {

  implicit val jsonFormat: Format[RepoListResp] =
    Json.format[RepoListResp]

  def apply(status: ApiStatus): RepoListResp =
    RepoListResp(status, None)

  def apply(dataList: List[RepoData]): RepoListResp =
    RepoListResp(ApiStatus.Ok, Some(dataList))
}
