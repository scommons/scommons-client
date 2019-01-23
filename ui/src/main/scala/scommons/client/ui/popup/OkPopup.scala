package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.Buttons
import scommons.client.util.ActionsData
import scommons.react.UiComponent

case class OkPopupProps(show: Boolean,
                        message: String,
                        onClose: () => Unit,
                        image: Option[String] = None)

object OkPopup extends UiComponent[OkPopupProps] {

  private case class OkPopupState(opened: Boolean = false)

  protected def create(): ReactClass = React.createClass[PropsType, OkPopupState](
    getInitialState = { _ =>
      OkPopupState()
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
        List(Buttons.OK),
        ActionsData(Set(Buttons.OK.command), _ => {
            case _ => props.onClose()
          },
          if (self.state.opened) Some(Buttons.OK.command)
          else None
        ),
        onClose = props.onClose,
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
