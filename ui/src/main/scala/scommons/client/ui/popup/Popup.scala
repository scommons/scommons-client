package scommons.client.ui.popup

import scommons.client.ui.popup.raw.NativeModal._
import scommons.react._

case class PopupProps(onClose: () => Unit,
                      closable: Boolean = true,
                      focusable: Boolean = true,
                      onOpen: () => Unit = () => (),
                      overlayClass: String = "scommons-modal-overlay",
                      popupClass: String = "scommons-modal")

object Popup extends FunctionComponent[PopupProps] {
  
  private[popup] var reactModal: ReactClass = <.ReactModal.reactClass

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <(reactModal)(
      ^.isOpen := true,
      ^.shouldCloseOnOverlayClick := props.closable,
      ^.shouldFocusAfterRender := props.focusable,
      ^.shouldReturnFocusAfterClose := props.focusable,
      ^.onAfterOpen := props.onOpen,
      ^.onRequestClose := props.onClose,
      ^.overlayClassName := props.overlayClass,
      ^.modalClassName := props.popupClass
    )(
      compProps.children
    )
  }
}
