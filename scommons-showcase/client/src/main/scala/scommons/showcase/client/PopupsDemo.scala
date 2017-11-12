package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.panel.{StatusPopup, StatusPopupProps}
import scommons.client.ui.{Buttons, ImageButton, ImageButtonProps}
import scommons.react.modal.ReactModal._
import scommons.react.modal.{ReactModalStyle, ReactModalStyleContent, ReactModalStyleOverlay}

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
          <(ImageButton())(^.wrapped := ImageButtonProps(Buttons.OK.copy(text = "Loading"), { () =>
            self.setState(_.copy(showLoading = true))
          }))(),
          <.ReactModal(
            ^.isOpen := self.state.showLoading,
            ^.onRequestClose := { () =>
              self.setState(_.copy(showLoading = false))
            },
            ^.modalStyle := new ReactModalStyle {
              override val overlay = new ReactModalStyleOverlay {
                override val backgroundColor = "rgba(0,0,0, 0)"
              }
              override val content = new ReactModalStyleContent {
                override val position = "fixed"
                override val top = "50%"
                override val left = "50%"
                override val right = ""
                override val bottom = ""
                override val border = ""
                override val borderRadius = ""
                override val padding = ""
              }
            }
          )(
            <.p()("Loading...")
          )
        ),

        <.p()(
          <(ImageButton())(^.wrapped := ImageButtonProps(Buttons.OK.copy(text = "Show Status"), { () =>
            self.setState(_.copy(showStatus = true))
          }))(),
          <(StatusPopup())(^.wrapped := StatusPopupProps("Fetching data...", show = self.state.showStatus, { () =>
            self.setState(_.copy(showStatus = false))
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
