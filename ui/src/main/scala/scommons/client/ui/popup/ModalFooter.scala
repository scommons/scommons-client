package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.ui.{ButtonData, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.ActionsData
import scommons.react._

case class ModalFooterProps(buttons: List[ButtonData],
                            actions: ActionsData,
                            dispatch: Dispatch = _ => ())

object ModalFooter extends FunctionComponent[ModalFooterProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
      props.buttons,
      props.actions,
      props.dispatch,
      className = Some("modal-footer")
    ))()
  }
}
