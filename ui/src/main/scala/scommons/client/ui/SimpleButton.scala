package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalajs.dom.HTMLButtonElement
import scommons.react._
import scommons.react.dom._
import scommons.react.hooks._

case class SimpleButtonProps(data: SimpleButtonData,
                             onClick: () => Unit,
                             disabled: Boolean = false,
                             requestFocus: Boolean = false)

object SimpleButton extends FunctionComponent[SimpleButtonProps] {

  protected def render(compProps: Props): ReactElement = {
    val buttonRef = useRef[HTMLButtonElement](null)
    val props = compProps.wrapped

    useLayoutEffect({ () =>
      val button = buttonRef.current
      if (button != null) {
        if (props.requestFocus) {
          button.focus()
        }
      }
    }, List(props.requestFocus))

    val data = props.data
    val primaryClass = if (data.primary) "btn-primary" else ""

    <.button(
      ^.`type` := "button",
      ^.className := s"btn $primaryClass".trim,
      ^.disabled := props.disabled,
      ^.reactRef := buttonRef,
      ^.onClick := { _: MouseSyntheticEvent =>
        props.onClick()
      }
    )(
      data.text
    )
  }
}
