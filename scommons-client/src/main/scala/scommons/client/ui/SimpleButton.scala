package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent

case class SimpleButtonProps(data: SimpleButtonData,
                             onClick: () => Unit,
                             disabled: Boolean = false)

object SimpleButton {

  type SimpleButtonSelf = Self[SimpleButtonProps, Unit]

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[SimpleButtonProps, Unit] { self =>
    val props = self.props.wrapped
    val data = props.data

    val primaryClass = if (data.primary) "btn-primary" else ""

    <.button(
      ^.`type` := "button",
      ^.className := s"btn $primaryClass".trim,
      ^.disabled := props.disabled,
      ^.onClick := { _: MouseSyntheticEvent =>
        props.onClick()
      }
    )(
      data.text
    )
  }
}
