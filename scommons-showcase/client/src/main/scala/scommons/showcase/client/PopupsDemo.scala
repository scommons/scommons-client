package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import scommons.client.ui.panel.{LoadingPopup, LoadingPopupProps, StatusPopup, StatusPopupProps}
import scommons.client.ui.{Buttons, ImageButton, ImageButtonProps}
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
            ^.overlayClassName := "scommons-PopupPanelGlass"
          )(
            <.p()("Modal text!")
          )
        ),

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
        ),

        <.a(^.href := "#myModal", ^.role := "button", ^.className := "btn", ^("data-toggle") := "modal")("Launch demo modal"),

        <.div(^.id := "myModal", ^.className := "modal hide fade", ^.tabindex := -1, ^.role := "dialog",
          ^("aria-labelledby") := "myModalLabel", ^("aria-hidden") := "true"
        )(
          <.div(^.className := "modal-header")(
            <.button(^.`type` := "button", ^.className := "close", ^("data-dismiss") := "modal", ^("aria-hidden") := "true")("×"),
            <.h3(^.id := "myModalLabel")("Modal header")
          ),
          <.div(^.className := "modal-body")(
            <.p()("One fine body...")
          ),
          <.div(^.className := "modal-footer")(
            <.button(^.className := "btn", ^("data-dismiss") := "modal", ^("aria-hidden") := "true")("Close"),
            <.button(^.className := "btn btn-primary")("Save changes")
          )
        )
      )
    }
  )
}
