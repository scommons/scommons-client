package scommons.client.task

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent
import scommons.client.ui.popup._

case class TaskManagerUiProps(showLoading: Boolean,
                              status: Option[String],
                              onHideStatus: () => Unit,
                              error: Option[String],
                              errorDetails: Option[String],
                              onCloseErrorPopup: () => Unit)

object TaskManagerUi extends UiComponent[TaskManagerUiProps] {

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped
    val showStatus = props.status.isDefined
    val statusMessage = props.status.getOrElse("")
    val showError = props.error.isDefined
    val errorMessage = props.error.getOrElse("")

    <.div()(
      <(LoadingPopup())(^.wrapped := LoadingPopupProps(
        props.showLoading
      ))(),

      <(StatusPopup())(^.wrapped := StatusPopupProps(
        statusMessage,
        showStatus,
        onHide = props.onHideStatus
      ))(),

      <(ErrorPopup())(^.wrapped := ErrorPopupProps(
        showError,
        errorMessage,
        details = props.errorDetails,
        onClose = props.onCloseErrorPopup
      ))()
    )
  }
}
