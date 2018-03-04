package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{SimpleButtonData, UiComponent}
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.util.ActionsData

case class YesNoPopupProps(show: Boolean,
                           message: String,
                           onSelect: YesNoCancelOption => Unit,
                           selected: YesNoCancelOption = Yes,
                           image: Option[String] = None)

object YesNoPopup extends UiComponent[YesNoPopupProps] {

  private case class YesNoPopupState(opened: Boolean = false)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, YesNoPopupState](
    getInitialState = { _ =>
      YesNoPopupState()
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        self.setState(_.copy(opened = false))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      <(Modal())(^.wrapped := ModalProps(props.show,
        None,
        List(
          SimpleButtonData(Yes.command, "Yes", props.selected == Yes),
          SimpleButtonData(No.command, "No", props.selected == No)
        ),
        ActionsData(Set(Yes.command, No.command), _ => {
            case Yes.command => props.onSelect(Yes)
            case No.command => props.onSelect(No)
          },
          if (self.state.opened) Some(props.selected.command)
          else None
        ),
        onClose = () => (),
        closable = false,
        onOpen = { () =>
          self.setState(_.copy(opened = true))
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
  )
}
