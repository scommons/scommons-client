package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.modal.ReactModal._
import scommons.react.modal.{ReactModalStyle, ReactModalStyleContent, ReactModalStyleOverlay}

case class LoadingPopupProps(show: Boolean)

object LoadingPopup {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[LoadingPopupProps, Unit] { self =>
    val props = self.props.wrapped

    <.ReactModal(
      ^.isOpen := props.show,
      ^.shouldCloseOnOverlayClick := false,
      ^.modalStyle := new ReactModalStyle {
        override val overlay = new ReactModalStyleOverlay {
          override val backgroundColor = "rgba(0,0,0, 0)"
          override val cursor = "wait"
        }
        override val content = new ReactModalStyleContent {
          override val position = "fixed"
          override val marginTop = "-16px"
          override val marginLeft = "-16px"
          override val top = "50%"
          override val left = "50%"
          override val right = ""
          override val bottom = ""
          override val background = "rgba(0,0,0, 0)"
          override val border = ""
          override val borderRadius = ""
          override val padding = ""
        }
      }
    )(
      <.img(^.className := PopupPanelCss.loading, ^.src := "")()
    )
  }
}
