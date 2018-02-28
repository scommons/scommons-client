package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{Buttons, TextField, TextFieldProps, UiComponent}
import scommons.client.util.ActionsData

case class InputPopupProps(show: Boolean,
                           message: String,
                           onOk: String => Unit,
                           onCancel: () => Unit,
                           placeholder: Option[String] = None,
                           initialValue: String = "")

object InputPopup extends UiComponent[InputPopupProps] {

  private case class InputPopupState(value: String,
                                     actionCommands: Set[String],
                                     opened: Boolean = false)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, InputPopupState](
    getInitialState = { self =>
      val props = self.props.wrapped

      InputPopupState(props.initialValue, getActionCommands(props.initialValue))
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        self.setState(_.copy(
          value = props.initialValue,
          actionCommands = getActionCommands(props.initialValue),
          opened = false
        ))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      val onOk = () => props.onOk(self.state.value)

      <(Modal())(^.wrapped := ModalProps(props.show,
        None,
        List(Buttons.OK, Buttons.CANCEL),
        ActionsData(self.state.actionCommands, {
          case (Buttons.OK.command, _) => onOk()
          case _ => props.onCancel()
        }),
        onClose = props.onCancel,
        onOpen = { () =>
          self.setState(_.copy(opened = true))
        }
      ))(
        <.div(^.className := "row-fluid")(
          <.p()(props.message),
          <.div(^.className := "control-group")(
            <(TextField())(^.wrapped := TextFieldProps(
              self.state.value,
              onChange = { value =>
                self.setState(_.copy(value = value, actionCommands = getActionCommands(value)))
              },
              requestFocus = self.state.opened,
              requestSelect = self.state.opened,
              className = Some("span12"),
              placeholder = props.placeholder,
              onEnter = onOk
            ))()
          )
        )
      )
    }
  )

  private val enabledOkActions = Set(Buttons.OK.command, Buttons.CANCEL.command)
  private val disabledOkActions = Set(Buttons.CANCEL.command)

  private def getActionCommands(value: String): Set[String] = {
    if (value.nonEmpty) enabledOkActions else disabledOkActions
  }
}
