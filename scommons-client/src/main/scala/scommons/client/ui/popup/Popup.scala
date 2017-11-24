package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.UiComponent
import scommons.react.modal.ReactModal._

case class PopupProps(show: Boolean,
                      onClose: () => Unit,
                      closable: Boolean = true,
                      onOpen: () => Unit = () => (),
                      overlayClass: String = "scommons-modal-overlay",
                      popupClass: String = "scommons-modal")

object Popup extends UiComponent[PopupProps] {

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    <.ReactModal(
      ^.isOpen := props.show,
      ^.shouldCloseOnOverlayClick := props.closable,
      ^.onAfterOpen := props.onOpen,
      ^.onRequestClose := props.onClose,
      ^.overlayClassName := props.overlayClass,
      ^.modalClassName := props.popupClass
    )(
      self.props.children
    )
  }
}
