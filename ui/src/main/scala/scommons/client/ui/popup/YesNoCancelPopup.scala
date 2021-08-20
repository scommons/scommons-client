package scommons.client.ui.popup

import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.{Buttons, SimpleButtonData, UiSettings}
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class YesNoCancelPopupProps(message: String,
                                 onSelect: YesNoCancelOption => Unit,
                                 selected: YesNoCancelOption = Yes,
                                 image: Option[String] = None)

object YesNoCancelPopup extends FunctionComponent[YesNoCancelPopupProps] {

  private[popup] var modalComp: UiComponent[ModalProps] = Modal

  protected def render(compProps: Props): ReactElement = {
    val (opened, setOpened) = useState(false)
    
    val props = compProps.wrapped

    <(modalComp())(^.wrapped := ModalProps(
      header = None,
      buttons = List(
        SimpleButtonData(Yes.command, "Yes", props.selected == Yes),
        SimpleButtonData(No.command, "No", props.selected == No),
        Buttons.CANCEL.copy(command = Cancel.command, primary = props.selected == Cancel)
      ),
      actions = ActionsData(Set(Yes.command, No.command, Cancel.command), _ => {
        case Yes.command => props.onSelect(Yes)
        case No.command => props.onSelect(No)
        case _ => props.onSelect(Cancel)
      },
        if (opened) Some(props.selected.command)
        else None
      ),
      onClose = () => props.onSelect(Cancel),
      onOpen = { () =>
        setOpened(true)
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
