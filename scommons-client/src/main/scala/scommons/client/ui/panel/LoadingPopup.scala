package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass

case class LoadingPopupProps(show: Boolean)

object LoadingPopup {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[LoadingPopupProps, Unit] { self =>
    val props = self.props.wrapped

    <(Popup())(^.wrapped := PopupProps(
      props.show,
      onClose = () => (),
      closable = false,
      overlayClass = PopupPanelCss.loadingOverlay,
      popupClass = PopupPanelCss.loadingContent
    ))(
      <.img(^.className := PopupPanelCss.loadingImg, ^.src := "")()
    )
  }
}
