package scommons.showcase.api.repo

import play.api.libs.json._
import scommons.api.{ApiStatus, DataResponse}

case class RepoResp private(status: ApiStatus,
                            data: Option[RepoData]
                           ) extends DataResponse[RepoData]

object RepoResp {

  implicit val jsonFormat: Format[RepoResp] =
    Json.format[RepoResp]

  def apply(status: ApiStatus): RepoResp =
    RepoResp(status, None)

  def apply(data: RepoData): RepoResp =
    RepoResp(ApiStatus.Ok, Some(data))
}
