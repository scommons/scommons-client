package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalajs.dom.raw.HTMLButtonElement

case class ImageButtonProps(data: ImageButtonData,
                            onClick: () => Unit,
                            disabled: Boolean = false,
                            showTextAsTitle: Boolean = false,
                            requestFocus: Boolean = false)

object ImageButton {

  private case class ImageButtonState(setButtonRef: HTMLButtonElement => Unit,
                                      getButtonRef: () => HTMLButtonElement)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ImageButtonProps, ImageButtonState](
    getInitialState = { _ =>
      var buttonRef: HTMLButtonElement = null

      ImageButtonState({ ref =>
        buttonRef = ref
      }, { () =>
        buttonRef
      })
    },
    componentDidUpdate = { (self, prevProps, _) =>
      val buttonRef = self.state.getButtonRef()
      if (self.props.wrapped.requestFocus
        && self.props.wrapped.requestFocus != prevProps.wrapped.requestFocus) {

        buttonRef.focus()
      }
    },
    render = { self =>
      val props = self.props.wrapped
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
        ^.ref := { ref: HTMLButtonElement =>
          self.state.setButtonRef(ref)
        },
        ^.onClick := { _: MouseSyntheticEvent =>
          self.props.wrapped.onClick()
        }
      )

      <.button(attributes)(
        if (props.showTextAsTitle) ImageLabelWrapper(image, None)
        else ImageLabelWrapper(image, Some(data.text))
      )
    }
  )
}
