package scommons.showcase.api.failing

import scommons.api.StatusResponse

import scala.concurrent.Future

trait FailingApi {

  def timedoutExample(): Future[StatusResponse]

  def failedExample(): Future[StatusResponse]
}
