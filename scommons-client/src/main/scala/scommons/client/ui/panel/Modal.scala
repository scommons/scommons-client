package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.ButtonData
import scommons.client.util.ActionsData

case class ModalProps(show: Boolean,
                      header: Option[String],
                      buttons: List[ButtonData],
                      actions: ActionsData,
                      onClose: () => Unit,
                      closable: Boolean = true,
                      onOpen: () => Unit = () => ())

object Modal {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ModalProps, Unit] { self =>
    val props = self.props.wrapped

    <(Popup())(^.wrapped := PopupProps(
      props.show,
      props.onClose,
      closable = props.closable,
      props.onOpen
    ))(
      props.header.map { header =>
        <(ModalHeader())(^.wrapped := ModalHeaderProps(header, props.onClose, closable = props.closable))()
      },
      <(ModalBody())()(
        self.props.children
      ),
      <(ModalFooter())(^.wrapped := ModalFooterProps(props.buttons, props.actions))()
    )
  }
}
