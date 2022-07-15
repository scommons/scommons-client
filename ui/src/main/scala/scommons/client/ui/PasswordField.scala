package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.events.{FormSyntheticEvent, KeyboardSyntheticEvent}
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.ext.KeyCode
import scommons.react._
import scommons.react.dom._
import scommons.react.hooks._

case class PasswordFieldProps(password: String,
                              onChange: (String) => Unit,
                              requestFocus: Boolean = false,
                              requestSelect: Boolean = false,
                              className: Option[String] = None,
                              placeholder: Option[String] = None,
                              onEnter: () => Unit = () => (),
                              readOnly: Boolean = false)

object PasswordField extends FunctionComponent[PasswordFieldProps] {

  protected def render(compProps: Props): ReactElement = {
    val inputRef = useRef[HTMLInputElement](null)
    val props = compProps.wrapped
    
    useLayoutEffect({ () =>
      val input = inputRef.current
      if (input != null) {
        val value = input.value
        if (props.requestSelect && value.nonEmpty) {
          input.setSelectionRange(0, value.length)
        }
      }
    }, List(props.requestSelect))

    useLayoutEffect({ () =>
      val input = inputRef.current
      if (input != null) {
        if (props.requestFocus) {
          input.focus()
        }
      }
    }, List(props.requestFocus))

    <.input(
      ^("readOnly") := props.readOnly,
      ^.`type` := "password",
      props.className.map { className =>
        ^.className := className
      },
      ^.value := props.password,
      props.placeholder.map { placeholder =>
        ^.placeholder := placeholder
      },
      ^.reactRef := inputRef,
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
}
