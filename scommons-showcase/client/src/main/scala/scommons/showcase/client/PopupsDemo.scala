package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import scommons.client.ui._
import scommons.client.ui.popup._
import scommons.client.util.ActionsData

case class ModalState(showModal: Boolean = false,
                      showInputPopup: Boolean = false,
                      showOkPopup: Boolean = false,
                      showLoading: Boolean = false,
                      showStatus: Boolean = false)

object PopupsDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, ModalState](
    getInitialState = { _ => ModalState() },
    render = { self =>
      <.div()(
        <.h2()("Modal"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Modal", primary = true), { () =>
            self.setState(_.copy(showModal = true))
          }))(),
          <(Modal())(^.wrapped := ModalProps(
            self.state.showModal,
            Some("Modal header"),
            List(Buttons.OK, Buttons.CANCEL),
            ActionsData(Set(Buttons.OK.command, Buttons.CANCEL.command), {
              case Buttons.CANCEL.command => self.setState(_.copy(showModal = false))
              case _ =>
            }),
            onClose = { () =>
              self.setState(_.copy(showModal = false))
            }
          ))(
            <.p()("One fine body...")
          )
        ),

        <.h2()("InputPopup"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "InputPopup", primary = true), { () =>
            self.setState(_.copy(showInputPopup = true))
          }))(),
          <(InputPopup())(^.wrapped := InputPopupProps(
            self.state.showInputPopup,
            "Please, enter a value",
            onOk = { _ =>
              self.setState(_.copy(showInputPopup = false))
            },
            onCancel = { () =>
              self.setState(_.copy(showInputPopup = false))
            },
            initialValue = "initial value"
          ))()
        ),

        <.h2()("Ok/Yes/No/Cancel Popups"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "OkPopup", primary = true), { () =>
            self.setState(_.copy(showOkPopup = true))
          }))(),
          <(OkPopup())(^.wrapped := OkPopupProps(
            self.state.showOkPopup,
            "Hello World!",
            onClose = { () =>
              self.setState(_.copy(showOkPopup = false))
            }
          ))()
        ),

        <.h2()("Other Popups"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Loading and Status", primary = true), { () =>
            self.setState(_.copy(showLoading = true, showStatus = true))

            var timerId = 0
            timerId = dom.window.setInterval({ () =>
              self.setState(_.copy(showLoading = false))
              dom.window.clearInterval(timerId)
            }, 3000)
          }))(),
          <(LoadingPopup())(^.wrapped := LoadingPopupProps(show = self.state.showLoading))(),
          <(StatusPopup())(^.wrapped := StatusPopupProps("Fetching data...", show = self.state.showStatus, { () =>
            if (!self.state.showLoading) {
              self.setState(_.copy(showStatus = false))
            }
          }))()
        )
      )
    }
  )
}
