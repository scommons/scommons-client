package scommons.client.task

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.popup._
import scommons.react.UiComponent

case class TaskManagerUiProps(showLoading: Boolean,
                              status: Option[String],
                              onHideStatus: () => Unit,
                              error: Option[String],
                              errorDetails: Option[String],
                              onCloseErrorPopup: () => Unit)

object TaskManagerUi extends UiComponent[TaskManagerUiProps] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped
    val showStatus = props.status.isDefined
    val statusMessage = props.status.getOrElse("")
    val showError = props.error.isDefined
    val errorMessage = props.error.getOrElse("")

    <.div()(
      <(StatusPopup())(^.wrapped := StatusPopupProps(
        statusMessage,
        showStatus,
        onHide = props.onHideStatus
      ))(),

      if (props.showLoading) Some(
        <(LoadingPopup())(^.wrapped := LoadingPopupProps(
          props.showLoading
        ))()
      ) else None,

      if (showError) Some(
        <(ErrorPopup())(^.wrapped := ErrorPopupProps(
          showError,
          errorMessage,
          details = props.errorDetails,
          onClose = props.onCloseErrorPopup
        ))()
      ) else None
    )
  }
}
