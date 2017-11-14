package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalajs.dom
import scommons.client.ui.panel.{LoadingPopup, LoadingPopupProps, StatusPopup, StatusPopupProps}
import scommons.client.ui._
import scommons.client.util.ActionsData
import scommons.react.modal.ReactModal._

case class ModalState(showModal: Boolean = false,
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
          <(ImageButton())(^.wrapped := ImageButtonProps(Buttons.OK.copy(text = "Modal"), { () =>
            self.setState(_.copy(showModal = true))
          }))(),
          <.ReactModal(
            ^.isOpen := self.state.showModal,
            ^.onRequestClose := { () =>
              self.setState(_.copy(showModal = false))
            },
            ^.modalClassName := "scommons-modal",
            ^.overlayClassName := "scommons-modal-overlay"
          )(
            <.div(^.className := "modal-header")(
              <.button(^.`type` := "button", ^.className := "close", ^.onClick := { _: MouseSyntheticEvent =>
                self.setState(_.copy(showModal = false))
              })("×"),
              <.h3()("Modal header")
            ),
            <.div(^.className := "modal-body")(
              <.p()("One fine body...")
            ),
            <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
              List(Buttons.OK, Buttons.CANCEL),
              ActionsData(Set(Buttons.OK.command, Buttons.CANCEL.command), _ => ()),
              group = false,
              className = Some("modal-footer")
            ))()
          )
        ),

        <.h2()("Popups"),
        <.hr()(),
        <.p()(
          <(ImageButton())(^.wrapped := ImageButtonProps(Buttons.OK.copy(text = "Show Loading and Status"), { () =>
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
