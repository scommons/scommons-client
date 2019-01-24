package scommons.client.showcase.action.api

import io.github.shogowada.scalajs.reactjs.redux.Action
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.api.{ApiStatus, StatusResponse}
import scommons.client.task.{FutureTask, TaskAction}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

object ApiActions {

  private val client: Api = new Api {
  }

  def successfulAction(dispatch: Dispatch): SuccessfulFetchAction = {
    val future = client.successExample().andThen {
      case Success(SuccessfulResp(ApiStatus.Ok, Some(data))) =>
        dispatch(SuccessfulFetchedAction(data))
    }
    SuccessfulFetchAction(FutureTask("Calling successful endpoint", future))
  }

  def timedoutAction(): FailingApiAction = {
    FailingApiAction(FutureTask("Calling timedout endpoint", client.timedoutExample()))
  }

  def failedAction(): FailingApiAction = {
    FailingApiAction(FutureTask("Calling failed endpoint", client.failedExample()))
  }

  case class SuccessfulFetchAction(task: FutureTask[SuccessfulResp]) extends TaskAction
  case class SuccessfulFetchedAction(data: String) extends Action
  
  case class FailingApiAction(task: FutureTask[StatusResponse]) extends TaskAction
}
