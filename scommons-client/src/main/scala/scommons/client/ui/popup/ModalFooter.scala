package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{ButtonData, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.ActionsData

case class ModalFooterProps(buttons: List[ButtonData],
                            actions: ActionsData)

object ModalFooter {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ModalFooterProps, Unit] { self =>
    val props = self.props.wrapped

    <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
      props.buttons,
      props.actions,
      group = false,
      className = Some("modal-footer")
    ))()
  }
}
