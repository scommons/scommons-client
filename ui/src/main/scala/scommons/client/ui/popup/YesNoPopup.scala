package scommons.client.ui.popup

import scommons.client.ui.SimpleButtonData
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class YesNoPopupProps(show: Boolean,
                           message: String,
                           onSelect: YesNoCancelOption => Unit,
                           selected: YesNoCancelOption = Yes,
                           image: Option[String] = None)

object YesNoPopup extends FunctionComponent[YesNoPopupProps] {

  protected def render(compProps: Props): ReactElement = {
    val (opened, setOpened) = useState(false)
    
    val props = compProps.wrapped

    <(Modal())(^.wrapped := ModalProps(
      show = props.show,
      header = None,
      buttons = List(
        SimpleButtonData(Yes.command, "Yes", props.selected == Yes),
        SimpleButtonData(No.command, "No", props.selected == No)
      ),
      actions = ActionsData(Set(Yes.command, No.command), _ => {
        case Yes.command => props.onSelect(Yes)
        case No.command => props.onSelect(No)
      },
        if (opened) Some(props.selected.command)
        else None
      ),
      onClose = () => (),
      closable = false,
      onOpen = { () =>
        setOpened(true)
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
