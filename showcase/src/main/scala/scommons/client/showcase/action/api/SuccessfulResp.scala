package scommons.client.showcase.action.api

import play.api.libs.json._
import scommons.api.{ApiStatus, DataResponse}

case class SuccessfulResp private(status: ApiStatus,
                                  data: Option[String]
                                 ) extends DataResponse[String]

object SuccessfulResp {

  implicit val jsonFormat: Format[SuccessfulResp] = Json.format[SuccessfulResp]

  def apply(data: String): SuccessfulResp =
    SuccessfulResp(ApiStatus.Ok, Some(data))
}
