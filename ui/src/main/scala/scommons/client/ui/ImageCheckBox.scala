package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.scalajs.dom.raw.HTMLInputElement
import scommons.react._
import scommons.react.dom._
import scommons.react.hooks._

import scala.scalajs.js

case class ImageCheckBoxProps(value: TriState,
                              image: String,
                              text: String,
                              onChange: (TriState) => Unit,
                              requestFocus: Boolean = false,
                              readOnly: Boolean = false)

/**
  * CheckBox that can have additional image element along with the text.
  *
  * <p>Supports indeterminate state.
  * 
  * @see http://css-tricks.com/indeterminate-checkboxes/
  * @see https://github.com/facebook/react/issues/1798#issuecomment-333414857
  */
object ImageCheckBox extends FunctionComponent[ImageCheckBoxProps] {

  protected def render(compProps: Props): ReactElement = {
    val inputRef = useRef[HTMLInputElement](null)
    val props = compProps.wrapped
    
    useLayoutEffect({ () =>
      val input = inputRef.current
      if (input != null) {
        input.indeterminate = props.value == TriState.Indeterminate
      }
    }, List(props.value.asInstanceOf[js.Any]))

    useLayoutEffect({ () =>
      val input = inputRef.current
      if (input != null) {
        if (props.requestFocus) {
          input.focus()
        }
      }
    }, List(props.requestFocus))

    <.label()(
      <.input(
        ^.`type` := "checkbox",
        ^.checked := TriState.isSelected(props.value),
        ^.reactRef := inputRef,
        ^.onChange := onChange(props.readOnly) { () =>
          props.onChange(props.value.next)
        }
      )(),
      ImageLabelWrapper(props.image, Some(props.text))
    )
  }
  
  private def onChange(readOnly: Boolean)
                      (f: () => Unit): FormSyntheticEvent[HTMLInputElement] => Unit = { event =>

    if (readOnly) {
      event.stopPropagation()
      event.preventDefault()
    }
    else f()
  }
}
