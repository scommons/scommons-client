package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalajs.dom.raw.HTMLButtonElement
import scommons.react._
import scommons.react.hooks._

case class ImageButtonProps(data: ImageButtonData,
                            onClick: () => Unit,
                            disabled: Boolean = false,
                            showTextAsTitle: Boolean = false,
                            requestFocus: Boolean = false)

object ImageButton extends FunctionComponent[ImageButtonProps] {

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
    val image = if (props.disabled) data.disabledImage else data.image
    val primaryClass = if (data.primary) "btn-primary" else ""

    val attributes = (
      if (props.showTextAsTitle) Some(^.title := data.text)
      else None
    ) :: List(
      ^.`type` := "button",
      ^.className := s"btn $primaryClass",
      ^.disabled := props.disabled,
      ^.reactRef := buttonRef,
      ^.onClick := { _: MouseSyntheticEvent =>
        props.onClick()
      }
    )

    <.button(attributes)(
      if (props.showTextAsTitle) ImageLabelWrapper(image, None)
      else ImageLabelWrapper(image, Some(data.text))
    )
  }
}
