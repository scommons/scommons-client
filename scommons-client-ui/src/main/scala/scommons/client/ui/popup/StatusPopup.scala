package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent

case class StatusPopupProps(text: String,
                            show: Boolean,
                            onHide: () => Unit)

object StatusPopup extends UiComponent[StatusPopupProps] {

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    <(Popup())(^.wrapped := PopupProps(
      props.show,
      onClose = () => (),
      overlayClass = "scommons-modal-no-overlay scommons-modal-top",
      popupClass = s"${PopupCss.statusContent} scommons-modal-top"
    ))(
      <(WithAutoHide())(^.wrapped := WithAutoHideProps(props.onHide))(props.text)
    )
  }
}
