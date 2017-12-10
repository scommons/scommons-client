package scommons.client.task

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.popup._

case class TaskManagerUiProps(showLoading: Boolean,
                              statusMessage: String,
                              showError: Boolean,
                              error: String,
                              errorDetails: Option[String],
                              onCloseErrorPopup: () => Unit)

object TaskManagerUi {

  private case class TaskManagerUiState(showStatus: Boolean)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[TaskManagerUiProps, TaskManagerUiState](
    getInitialState = { self =>
      TaskManagerUiState(self.props.wrapped.showLoading)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props
        && props.showLoading
        && props.showLoading != self.props.wrapped.showLoading) {

        self.setState(_.copy(showStatus = true))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      <.div()(
        <(LoadingPopup())(^.wrapped := LoadingPopupProps(props.showLoading))(),
        <(StatusPopup())(^.wrapped := StatusPopupProps(props.statusMessage, self.state.showStatus, { () =>
          self.setState(_.copy(showStatus = false))
        }))(),
        <(ErrorPopup())(^.wrapped := ErrorPopupProps(
          props.showError,
          props.error,
          details = props.errorDetails,
          onClose = props.onCloseErrorPopup
        ))()
      )
    }
  )
}
