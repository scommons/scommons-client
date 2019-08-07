package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.react._

case class ModalHeaderProps(header: String,
                            onClose: () => Unit,
                            closable: Boolean = true)

object ModalHeader extends FunctionComponent[ModalHeaderProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

    val closeButton =
      if (props.closable) {
        Some(<.button(
          ^.`type` := "button",
          ^.className := "close",
          ^.onClick := { _: MouseSyntheticEvent =>
            props.onClose()
          }
        )("×"))
      }
      else None

    <.div(^.className := "modal-header")(
      closeButton,
      <.h3()(props.header)
    )
  }
}
