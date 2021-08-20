package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.ui.ButtonData
import scommons.client.util.ActionsData
import scommons.react._

case class ModalProps(header: Option[String],
                      buttons: List[ButtonData],
                      actions: ActionsData,
                      dispatch: Dispatch = _ => (),
                      onClose: () => Unit,
                      closable: Boolean = true,
                      onOpen: () => Unit = () => ())

object Modal extends FunctionComponent[ModalProps] {

  private[popup] var popupComp: UiComponent[PopupProps] = Popup
  private[popup] var modalHeaderComp: UiComponent[ModalHeaderProps] = ModalHeader
  private[popup] var modalBodyComp: UiComponent[Unit] = ModalBody
  private[popup] var modalFooterComp: UiComponent[ModalFooterProps] = ModalFooter

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <(popupComp())(^.wrapped := PopupProps(
      onClose = props.onClose,
      closable = props.closable,
      onOpen = props.onOpen
    ))(
      props.header.map { header =>
        <(modalHeaderComp())(^.wrapped := ModalHeaderProps(header, props.onClose, closable = props.closable))()
      },
      <(modalBodyComp())()(
        compProps.children
      ),
      <(modalFooterComp())(^.wrapped := ModalFooterProps(props.buttons, props.actions, props.dispatch))()
    )
  }
}
