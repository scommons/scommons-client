package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

case class StatusPopupProps(text: String,
                            show: Boolean,
                            onHide: () => Unit)

object StatusPopup {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[StatusPopupProps, Unit] { self =>
    val props = self.props.wrapped

    <(Popup())(^.wrapped := PopupProps(
      props.show,
      onClose = () => (),
      overlayClass = "scommons-status-no-overlay",
      popupClass = PopupPanelCss.statusContent
    ))(
      <(WithAutoHide())(^.wrapped := WithAutoHideProps(props.onHide))(props.text)
    )
  }
}
