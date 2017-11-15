package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, KeyboardSyntheticEvent}
import org.scalajs.dom.raw.HTMLInputElement
import scommons.client.ui.Buttons
import scommons.client.util.{ActionsData, KeyCodes}

case class InputBoxProps(show: Boolean,
                         message: String,
                         onOk: String => Unit,
                         onCancel: () => Unit,
                         placeholder: Option[String] = None,
                         initialValue: Option[String] = None)

object InputBox {

  private case class InputBoxState(value: String,
                                   actionCommands: Set[String],
                                   setInputRef: HTMLInputElement => Unit,
                                   getInputRef: () => HTMLInputElement)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[InputBoxProps, InputBoxState](
    getInitialState = { self =>
      val value = self.props.wrapped.initialValue.getOrElse("")
      var inputRef: HTMLInputElement = null

      InputBoxState(value, getActionCommands(value), { ref =>
        inputRef = ref
      }, { () =>
        inputRef
      })
    },
    componentWillReceiveProps = { (self, props) =>
      val value = props.wrapped.initialValue.getOrElse("")
      self.setState(_.copy(value = value, actionCommands = getActionCommands(value)))
    },
    render = { self =>
      val props = self.props.wrapped

      val placeholderAttr = props.placeholder.map(p => ^.placeholder := p)

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
          val inputRef = self.state.getInputRef()
          val value = self.state.value
          if (value.nonEmpty) {
            inputRef.setSelectionRange(0, value.length)
          }

          inputRef.focus()
        }
      ))(
        <.div(^.className := "row-fluid")(
          <.p()(props.message),
          <.div(^.className := "control-group")(
            <.input(
              ^.`type` := "text",
              ^.className := "span12",
              ^.value := self.state.value,
              placeholderAttr,
              ^.ref := { ref: HTMLInputElement =>
                self.state.setInputRef(ref)
              },
              ^.onChange := { e: FormSyntheticEvent[HTMLInputElement] =>
                val value = e.target.value
                self.setState(_.copy(value = value, actionCommands = getActionCommands(value)))
              },
              ^.onKeyDown := { e: KeyboardSyntheticEvent =>
                if (e.keyCode == KeyCodes.KEY_ENTER) {
                  onCommand(Buttons.OK.command)
                }
              }
            )()
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
