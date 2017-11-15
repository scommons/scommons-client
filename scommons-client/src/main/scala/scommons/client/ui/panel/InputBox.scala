package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.SyntheticEvent
import org.scalajs.dom.raw.HTMLInputElement
import scommons.client.ui.Buttons
import scommons.client.util.ActionsData

case class InputBoxProps(show: Boolean,
                         title: String,
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

  private type InputBoxSelf = Self[InputBoxProps, InputBoxState]

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
    render = { self =>
      val props = self.props.wrapped

      val placeholderAttr = props.placeholder.map(p => ^.placeholder := p)

      <(Modal())(^.wrapped := ModalProps(props.show,
        props.title,
        List(Buttons.OK, Buttons.CANCEL),
        ActionsData(self.state.actionCommands, {
          case Buttons.OK.command => props.onOk(self.state.value)
          case _ => props.onCancel()
        }),
        props.onCancel,
        onOpen = { () =>
          val inputRef = self.state.getInputRef()
          val value = self.state.value
          if (value.nonEmpty) {
            inputRef.setAttribute("value", value)
            inputRef.setSelectionRange(0, value.length)
          }

          self.state.getInputRef().focus()
        }
      ))(
        <.div(^.className := "row-fluid")(
          <.p()(props.message),
          <.div(^.className := "control-group")(
            <.input(
              ^.`type` := "text",
              ^.className := "span12",
              //^.value := self.state.value,
              placeholderAttr,
              ^.ref := { ref: HTMLInputElement =>
                self.state.setInputRef(ref)
              },
              ^.onKeyUp := onChangeEvent(self)
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

  private def onChangeEvent[T <: SyntheticEvent](self: InputBoxSelf): T => Unit = { _: T =>
    updateState(self.state.getInputRef, self.setState)
  }

  private def updateState(getInputRef: () => HTMLInputElement,
                          setState: (InputBoxState => InputBoxState) => Unit): Unit = {

    val value = getInputRef().value

    setState(_.copy(value = value, actionCommands = getActionCommands(value)))
  }
}
