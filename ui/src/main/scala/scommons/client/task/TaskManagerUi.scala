package scommons.client.task

import scommons.client.ui.popup._
import scommons.react._

case class TaskManagerUiProps(showLoading: Boolean,
                              status: Option[String],
                              onHideStatus: () => Unit,
                              error: Option[String],
                              errorDetails: Option[String],
                              onCloseErrorPopup: () => Unit)

object TaskManagerUi extends FunctionComponent[TaskManagerUiProps] {

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
