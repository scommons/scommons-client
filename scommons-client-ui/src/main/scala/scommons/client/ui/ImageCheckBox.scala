package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.scalajs.dom.raw.HTMLInputElement
import scommons.react.UiComponent

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
object ImageCheckBox extends UiComponent[ImageCheckBoxProps] {

  private case class ImageCheckBoxState(setInputRef: HTMLInputElement => Unit,
                                        getInputRef: () => HTMLInputElement)

  protected def create(): ReactClass = React.createClass[PropsType, ImageCheckBoxState](
    getInitialState = { _ =>
      var inputRef: HTMLInputElement = null

      ImageCheckBoxState({ ref =>
        inputRef = ref
      }, { () =>
        inputRef
      })
    },
    componentDidMount = { self =>
      val inputRef = self.state.getInputRef()
      inputRef.indeterminate = self.props.wrapped.value == TriState.Indeterminate
    },
    componentDidUpdate = { (self, prevProps, _) =>
      val inputRef = self.state.getInputRef()

      if (self.props.wrapped.value != prevProps.wrapped.value) {
        inputRef.indeterminate = self.props.wrapped.value == TriState.Indeterminate
      }
      
      if (self.props.wrapped.requestFocus
        && self.props.wrapped.requestFocus != prevProps.wrapped.requestFocus) {
        
        inputRef.focus()
      }
    },
    render = { self =>
      val props = self.props.wrapped

      <.label()(
        <.input(
          ^.`type` := "checkbox",
          ^.checked := TriState.isSelected(props.value),
          ^.ref := { ref: HTMLInputElement =>
            self.state.setInputRef(ref)
          },
          ^.onChange := onChange(props.readOnly) { () =>
            props.onChange(props.value.next)
          }
        )(),
        ImageLabelWrapper(props.image, Some(props.text))
      )
    }
  )
  
  private[ui] def onChange(readOnly: Boolean)
                          (f: () => Unit): FormSyntheticEvent[HTMLInputElement] => Unit = { event =>

    if (readOnly) {
      event.stopPropagation()
      event.preventDefault()
    }
    else f()
  }
}
