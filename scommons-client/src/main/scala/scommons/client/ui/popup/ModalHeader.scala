package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent

case class ModalHeaderProps(header: String,
                            onClose: () => Unit,
                            closable: Boolean = true)

object ModalHeader {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ModalHeaderProps, Unit] { self =>
    val props = self.props.wrapped

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
