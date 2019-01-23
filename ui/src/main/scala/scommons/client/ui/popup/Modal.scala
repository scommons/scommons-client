package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.ui.ButtonData
import scommons.client.util.ActionsData
import scommons.react.UiComponent

case class ModalProps(show: Boolean,
                      header: Option[String],
                      buttons: List[ButtonData],
                      actions: ActionsData,
                      dispatch: Dispatch = _ => (),
                      onClose: () => Unit,
                      closable: Boolean = true,
                      onOpen: () => Unit = () => ())

object Modal extends UiComponent[ModalProps] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    <(Popup())(^.wrapped := PopupProps(
      show = props.show,
      onClose = props.onClose,
      closable = props.closable,
      onOpen = props.onOpen
    ))(
      props.header.map { header =>
        <(ModalHeader())(^.wrapped := ModalHeaderProps(header, props.onClose, closable = props.closable))()
      },
      <(ModalBody())()(
        self.props.children
      ),
      <(ModalFooter())(^.wrapped := ModalFooterProps(props.buttons, props.actions, props.dispatch))()
    )
  }
}
