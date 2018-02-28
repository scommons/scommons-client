package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{Buttons, UiComponent}
import scommons.client.util.ActionsData

case class OkPopupProps(show: Boolean,
                        message: String,
                        onClose: () => Unit,
                        image: Option[String] = None)

object OkPopup extends UiComponent[OkPopupProps] {

  private case class OkPopupState(opened: Boolean = false)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, OkPopupState](
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
        ActionsData(Set(Buttons.OK.command),
          {
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
