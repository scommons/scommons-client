package scommons.client.ui.popup

import scommons.client.ui.{Buttons, UiSettings}
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class OkPopupProps(message: String,
                        onClose: () => Unit,
                        image: Option[String] = None)

object OkPopup extends FunctionComponent[OkPopupProps] {

  private case class OkPopupState(opened: Boolean = false)

  private[popup] var modalComp: UiComponent[ModalProps] = Modal

  protected def render(compProps: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => OkPopupState())
    
    val props = compProps.wrapped

    <(modalComp())(^.wrapped := ModalProps(
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
          <.img(^.className := image, ^.src := UiSettings.imgClearCacheUrl)()
        },
        <.p()(props.message)
      )
    )
  }
}
