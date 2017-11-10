package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.{Buttons, ImageButton, ImageButtonProps}
import scommons.react.modal.ReactModal._

object PopupsDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Boolean](
    getInitialState = { _ =>
      false
    },
    render = { self =>
      <.div()(
        <.h2()("Modal"),
        <.hr()(),

        <.p()(
          <(ImageButton())(^.wrapped := ImageButtonProps(Buttons.OK.copy(text = "Show ReactModal"), { () =>
            self.setState(_ => true)
          }))(),
          <.ReactModal(
            ^.isOpen := self.state,
            ^.onRequestClose := { () =>
              self.setState(_ => false)
            },
            ^.modalClassName := "scommons-modal",
            ^.overlayClassName := "scommons-PopupPanelGlass"
//            ^.modalStyle := new ReactModalStyle {
//              override val overlay = new ReactModalStyleOverlay {
//                override val position = "fixed"
//                override val top = 0
//                override val left = 0
//                override val right = 0
//                override val bottom = 0
//              }
//            }
          )(
            <.p()("Modal text!")
          )
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
