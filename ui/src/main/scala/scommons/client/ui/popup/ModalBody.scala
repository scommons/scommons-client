package scommons.client.ui.popup

import scommons.react._

object ModalBody extends FunctionComponent[Unit] {

  protected def render(props: Props): ReactElement = {
    <.div(^.className := "modal-body")(
      props.children
    )
  }
}
