package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.ui.{ButtonData, ButtonsPanel, ButtonsPanelProps, UiComponent}
import scommons.client.util.ActionsData

case class ModalFooterProps(buttons: List[ButtonData],
                            actions: ActionsData,
                            dispatch: Dispatch = _ => ())

object ModalFooter extends UiComponent[ModalFooterProps] {

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
      props.buttons,
      props.actions,
      props.dispatch,
      className = Some("modal-footer")
    ))()
  }
}
