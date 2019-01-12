package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.react.UiComponent

object ModalBody extends UiComponent[Unit] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit] { self =>
    <.div(^.className := "modal-body")(
      self.props.children
    )
  }
}
