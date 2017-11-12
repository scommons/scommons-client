package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.modal.ReactModal._
import scommons.react.modal.{ReactModalStyle, ReactModalStyleContent}

case class StatusPopupProps(text: String,
                            show: Boolean,
                            onHide: () => Unit)

object StatusPopup {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[StatusPopupProps, Unit] { self =>
    val props = self.props.wrapped

    <.ReactModal(
      ^.isOpen := props.show,
      ^.overlayClassName := "no-overlay",
      ^.modalStyle := new ReactModalStyle {
        override val content = new ReactModalStyleContent {
          override val position = "fixed"
          override val marginTop = "-20px"
          override val top = "100%"
          override val left = ""
          override val right = ""
          override val bottom = ""
          override val border = ""
          override val padding = ""
        }
      }
    )(
      <(WithAutoHide())(^.wrapped := WithAutoHideProps(props.onHide))(props.text)
    )
  }
}
