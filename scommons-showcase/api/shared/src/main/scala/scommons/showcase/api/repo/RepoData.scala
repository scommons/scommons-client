package scommons.showcase.api.repo

import play.api.libs.json._

case class RepoData(id: Option[Int],
                    name: String)

object RepoData {

  implicit val jsonFormat: Format[RepoData] = Json.format[RepoData]
}
