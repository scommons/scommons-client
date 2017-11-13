package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.modal.ReactModal._

case class LoadingPopupProps(show: Boolean)

object LoadingPopup {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[LoadingPopupProps, Unit] { self =>
    val props = self.props.wrapped

    <.ReactModal(
      ^.isOpen := props.show,
      ^.shouldCloseOnOverlayClick := false,
      ^.overlayClassName := PopupPanelCss.loadingOverlay,
      ^.modalClassName := PopupPanelCss.loadingContent
    )(
      <.img(^.className := PopupPanelCss.loadingImg, ^.src := "")()
    )
  }
}
