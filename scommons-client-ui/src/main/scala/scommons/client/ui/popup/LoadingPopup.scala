package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent

case class LoadingPopupProps(show: Boolean)

object LoadingPopup extends UiComponent[LoadingPopupProps] {

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    <(Popup())(^.wrapped := PopupProps(
      props.show,
      onClose = () => (),
      closable = false,
      overlayClass = s"${PopupCss.loadingOverlay} scommons-modal-top",
      popupClass = s"${PopupCss.loadingContent} scommons-modal-top"
    ))(
      <.img(^.className := PopupCss.loadingImg, ^.src := "")()
    )
  }
}
