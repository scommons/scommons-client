package apis

import scommons.api.StatusResponse
import scommons.showcase.api.failing.FailingApi

import scala.concurrent.{ExecutionContext, Future}

class FailingApiImpl()(implicit ec: ExecutionContext) extends FailingApi {

  def timedoutExample(): Future[StatusResponse] = {
    Future {
      Thread.sleep(30000)
      StatusResponse.Ok
    }
  }

  def failedExample(): Future[StatusResponse] = {
    Future.failed(new Exception("Example error"))
  }
}
