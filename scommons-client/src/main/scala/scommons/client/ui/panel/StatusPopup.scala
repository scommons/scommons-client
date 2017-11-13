package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.modal.ReactModal._

case class StatusPopupProps(text: String,
                            show: Boolean,
                            onHide: () => Unit)

object StatusPopup {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[StatusPopupProps, Unit] { self =>
    val props = self.props.wrapped

    <.ReactModal(
      ^.isOpen := props.show,
      ^.overlayClassName := "scommons-status-no-overlay",
      ^.modalClassName := PopupPanelCss.statusContent
    )(
      <(WithAutoHide())(^.wrapped := WithAutoHideProps(props.onHide))(props.text)
    )
  }
}
