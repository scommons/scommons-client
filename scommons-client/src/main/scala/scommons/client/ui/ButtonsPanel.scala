package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.util.ActionsData

case class ButtonsPanelProps(buttons: List[ButtonData],
                             actions: ActionsData,
                             group: Boolean)

object ButtonsPanel {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ButtonsPanelProps, Unit] { self =>
    val props = self.props.wrapped

    val panelClass = if (props.group) "btn-group" else "btn-toolbar"

    <.div(^.className := panelClass)(props.buttons.map { buttonData =>
      val disabled = !props.actions.actionCommands.contains(buttonData.command)

      buttonData match {
        case data: ImageButtonData => <(ImageButton())(^.wrapped := ImageButtonProps(
          data, () => props.actions.onCommand(data.command), showTextAsTitle = props.group, disabled = disabled
        ))()
      }
    })
  }
}
