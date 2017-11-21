package scommons.client.ui.panel

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

  private[panel] case class InputBoxState(value: String,
                                          actionCommands: Set[String],
                                          requestFocus: Boolean = false,
                                          requestSelect: Boolean = false)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[InputPopupProps, InputBoxState](
    getInitialState = { self =>
      val value = self.props.wrapped.initialValue

      InputBoxState(value, getActionCommands(value))
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val value = nextProps.wrapped.initialValue
      self.setState(_.copy(
        value = value,
        actionCommands = getActionCommands(value),
        requestFocus = false,
        requestSelect = false
      ))
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
          self.setState(_.copy(requestFocus = true, requestSelect = true))
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
              requestFocus = self.state.requestFocus,
              requestSelect = self.state.requestSelect,
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
