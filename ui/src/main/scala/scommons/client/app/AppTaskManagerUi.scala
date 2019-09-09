package scommons.client.app

import scommons.api.ApiResponse
import scommons.client.ui.popup._
import scommons.react._
import scommons.react.redux.task.TaskManagerUiProps

import scala.util.{Success, Try}

/**
  * Displays status of running tasks.
  */
object AppTaskManagerUi extends FunctionComponent[TaskManagerUiProps] {

  var errorHandler: PartialFunction[Try[_], (Option[String], Option[String])] = {
    case Success(result) => result match {
      case res: ApiResponse if res.status.nonSuccessful =>
        (Some(res.status.error.getOrElse("Non-successful response")), res.status.details)
      case _ =>
        (None, None)
    }
  }

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    val showStatus = props.status.isDefined
    val statusMessage = props.status.getOrElse("")
    val showError = props.error.isDefined
    val errorMessage = props.error.getOrElse("")

    <.>()(
      if (showStatus) Some(
        <(StatusPopup())(^.wrapped := StatusPopupProps(
          statusMessage,
          onHide = props.onHideStatus
        ))()
      ) else None,

      if (props.showLoading) Some(
        <(LoadingPopup())()()
      ) else None,

      if (showError) Some(
        <(ErrorPopup())(^.wrapped := ErrorPopupProps(
          errorMessage,
          details = props.errorDetails,
          onClose = props.onCloseErrorPopup
        ))()
      ) else None
    )
  }
}
