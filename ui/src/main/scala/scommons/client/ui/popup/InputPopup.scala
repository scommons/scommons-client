package scommons.client.ui.popup

import scommons.client.ui.{Buttons, TextField, TextFieldProps}
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class InputPopupProps(message: String,
                           onOk: String => Unit,
                           onCancel: () => Unit,
                           placeholder: Option[String] = None,
                           initialValue: String = "")

object InputPopup extends FunctionComponent[InputPopupProps] {

  private case class InputPopupState(value: String,
                                     actionCommands: Set[String],
                                     opened: Boolean = false)

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    val (state, setState) = useStateUpdater { () =>
      InputPopupState(props.initialValue, getActionCommands(props.initialValue))
    }

    val onOk = () => props.onOk(state.value)

    <(Modal())(^.wrapped := ModalProps(
      header = None,
      buttons = List(Buttons.OK, Buttons.CANCEL),
      actions = ActionsData(state.actionCommands, _ => {
        case Buttons.OK.command => onOk()
        case _ => props.onCancel()
      }),
      onClose = props.onCancel,
      onOpen = { () =>
        setState(_.copy(opened = true))
      }
    ))(
      <.div(^.className := "row-fluid")(
        <.p()(props.message),
        <.div(^.className := "control-group")(
          <(TextField())(^.wrapped := TextFieldProps(
            state.value,
            onChange = { value =>
              setState(_.copy(value = value, actionCommands = getActionCommands(value)))
            },
            requestFocus = state.opened,
            requestSelect = state.opened,
            className = Some("span12"),
            placeholder = props.placeholder,
            onEnter = onOk
          ))()
        )
      )
    )
  }

  private val enabledOkActions = Set(Buttons.OK.command, Buttons.CANCEL.command)
  private val disabledOkActions = Set(Buttons.CANCEL.command)

  private def getActionCommands(value: String): Set[String] = {
    if (value.nonEmpty) enabledOkActions else disabledOkActions
  }
}
