package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{Buttons, SimpleButtonData, UiComponent}
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.util.ActionsData

case class YesNoCancelPopupProps(show: Boolean,
                                 message: String,
                                 onSelect: YesNoCancelOption => Unit,
                                 selected: YesNoCancelOption = Yes,
                                 image: Option[String] = None)

object YesNoCancelPopup extends UiComponent[YesNoCancelPopupProps] {

  private case class YesNoCancelPopupState(opened: Boolean = false)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, YesNoCancelPopupState](
    getInitialState = { _ =>
      YesNoCancelPopupState()
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
          SimpleButtonData(No.command, "No", props.selected == No),
          Buttons.CANCEL.copy(command = Cancel.command, primary = props.selected == Cancel)
        ),
        ActionsData(Set(Yes.command, No.command, Cancel.command),
          {
            case (Yes.command, _) => props.onSelect(Yes)
            case (No.command, _) => props.onSelect(No)
            case _ => props.onSelect(Cancel)
          },
          if (self.state.opened) Some(props.selected.command)
          else None
        ),
        onClose = () => props.onSelect(Cancel),
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
