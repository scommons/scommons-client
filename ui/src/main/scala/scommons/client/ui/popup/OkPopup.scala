package scommons.client.ui.popup

import scommons.client.ui.Buttons
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class OkPopupProps(message: String,
                        onClose: () => Unit,
                        image: Option[String] = None)

object OkPopup extends FunctionComponent[OkPopupProps] {

  private case class OkPopupState(opened: Boolean = false)

  protected def render(compProps: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => OkPopupState())
    
    val props = compProps.wrapped

    <(Modal())(^.wrapped := ModalProps(
      header = None,
      buttons = List(Buttons.OK),
      actions = ActionsData(Set(Buttons.OK.command), _ => {
        case _ => props.onClose()
      },
        if (state.opened) Some(Buttons.OK.command)
        else None
      ),
      onClose = props.onClose,
      onOpen = { () =>
        setState(_.copy(opened = true))
      }
    ))(
      <.div(^.className := "row-fluid")(
        props.image.map { image =>
          <.img(^.className := image, ^.src := "")()
        },
        <.p()(props.message)
      )
    )
  }
}
