package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{Buttons, TextField, TextFieldProps}
import scommons.client.util.ActionsData

case class InputPopupProps(show: Boolean,
                           message: String,
                           onOk: String => Unit,
                           onCancel: () => Unit,
                           placeholder: Option[String] = None,
                           initialValue: String = "")

object InputPopup {

  private case class InputPopupState(value: String,
                                     actionCommands: Set[String],
                                     opened: Boolean = false)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[InputPopupProps, InputPopupState](
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

      def onCommand(command: String): Unit = command match {
        case Buttons.OK.command => props.onOk(self.state.value)
        case _ => props.onCancel()
      }

      <(Modal())(^.wrapped := ModalProps(props.show,
        None,
        List(Buttons.OK, Buttons.CANCEL),
        ActionsData(self.state.actionCommands, onCommand),
        props.onCancel,
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
              onEnter = { () =>
                onCommand(Buttons.OK.command)
              }
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
