package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, KeyboardSyntheticEvent}
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLInputElement

case class PasswordFieldProps(password: String,
                              onChange: (String) => Unit,
                              requestFocus: Boolean = false,
                              requestSelect: Boolean = false,
                              className: Option[String] = None,
                              placeholder: Option[String] = None,
                              onEnter: () => Unit = () => ())

object PasswordField extends UiComponent[PasswordFieldProps] {

  private case class PasswordFieldState(setInputRef: HTMLInputElement => Unit,
                                        getInputRef: () => HTMLInputElement)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, PasswordFieldState](
    getInitialState = { _ =>
      var inputRef: HTMLInputElement = null

      PasswordFieldState({ ref =>
        inputRef = ref
      }, { () =>
        inputRef
      })
    },
    componentDidUpdate = { (self, prevProps, _) =>
      val inputRef = self.state.getInputRef()
      val value = inputRef.value
      if (self.props.wrapped.requestSelect
        && self.props.wrapped.requestSelect != prevProps.wrapped.requestSelect
        && value.nonEmpty) {

        inputRef.setSelectionRange(0, value.length)
      }

      if (self.props.wrapped.requestFocus
        && self.props.wrapped.requestFocus != prevProps.wrapped.requestFocus) {

        inputRef.focus()
      }
    },
    render = { self =>
      val props = self.props.wrapped

      <.input(
        ^.`type` := "password",
        props.className.map { className =>
          ^.className := className
        },
        ^.value := props.password,
        props.placeholder.map { placeholder =>
          ^.placeholder := placeholder
        },
        ^.ref := { ref: HTMLInputElement =>
          self.state.setInputRef(ref)
        },
        ^.onChange := { e: FormSyntheticEvent[HTMLInputElement] =>
          val value = e.target.value
          props.onChange(value)
        },
        ^.onKeyDown := { e: KeyboardSyntheticEvent =>
          if (e.keyCode == KeyCode.Enter) {
            props.onEnter()
          }
        }
      )()
    }
  )
}
