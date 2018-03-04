package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalajs.dom.raw.HTMLButtonElement

case class SimpleButtonProps(data: SimpleButtonData,
                             onClick: () => Unit,
                             disabled: Boolean = false,
                             requestFocus: Boolean = false)

object SimpleButton extends UiComponent[SimpleButtonProps] {

  private case class SimpleButtonState(setButtonRef: HTMLButtonElement => Unit,
                                       getButtonRef: () => HTMLButtonElement)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, SimpleButtonState](
    getInitialState = { _ =>
      var buttonRef: HTMLButtonElement = null

      SimpleButtonState({ ref =>
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

      val primaryClass = if (data.primary) "btn-primary" else ""

      <.button(
        ^.`type` := "button",
        ^.className := s"btn $primaryClass".trim,
        ^.disabled := props.disabled,
        ^.ref := { ref: HTMLButtonElement =>
          self.state.setButtonRef(ref)
        },
        ^.onClick := { _: MouseSyntheticEvent =>
          props.onClick()
        }
      )(
        data.text
      )
    }
  )
}
