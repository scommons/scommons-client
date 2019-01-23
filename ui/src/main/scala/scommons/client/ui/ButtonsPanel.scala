package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import scommons.client.util.ActionsData
import scommons.react.UiComponent

case class ButtonsPanelProps(buttons: List[ButtonData],
                             actions: ActionsData,
                             dispatch: Dispatch = _ => (),
                             group: Boolean = false,
                             className: Option[String] = None)

object ButtonsPanel extends UiComponent[ButtonsPanelProps] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    val props = self.props.wrapped

    val panelClass = props.className match {
      case None => if (props.group) "btn-group" else "btn-toolbar"
      case Some(className) => className
    }

    def onCommand(command: String): () => Unit = { () =>
      val onCommandP = props.actions.onCommand(props.dispatch)
      if (onCommandP.isDefinedAt(command)) {
        onCommandP(command)
      }
    }

    <.div(^.className := panelClass)(props.buttons.map { buttonData =>
      val disabled = !props.actions.enabledCommands.contains(buttonData.command)
      val focused = props.actions.focusedCommand.contains(buttonData.command)

      buttonData match {
        case data: SimpleButtonData => <(SimpleButton())(^.wrapped := SimpleButtonProps(
          data, onCommand(data.command), disabled = disabled, requestFocus = focused
        ))()
        case data: ImageButtonData => <(ImageButton())(^.wrapped := ImageButtonProps(
          data, onCommand(data.command), showTextAsTitle = props.group, disabled = disabled, requestFocus = focused
        ))()
      }
    })
  }
}
