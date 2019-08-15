package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.popup._
import scommons.client.util.ActionsData

case class ModalState(showModal: Boolean = false,
                      showInput: Boolean = false,
                      inputValue: String = "initial value",
                      showOk: Boolean = false,
                      okMessage: String = "",
                      showYesNoCancel: Boolean = false,
                      showYesNo: Boolean = false,
                      showError: Boolean = false,
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
          
          if (self.state.showModal) Some(
            <(Modal())(^.wrapped := ModalProps(
              Some("Modal header"),
              List(Buttons.OK, Buttons.CANCEL),
              ActionsData(Set(Buttons.OK.command, Buttons.CANCEL.command), _ => {
                case Buttons.CANCEL.command => self.setState(_.copy(showModal = false))
                case _ =>
              }),
              onClose = { () =>
                self.setState(_.copy(showModal = false))
              }
            ))(
              <.p()("One fine body...")
            )
          ) else None
        ),

        <.h2()("Input Popup"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Input", primary = true), { () =>
            self.setState(_.copy(showInput = true))
          }))(),
          
          if (self.state.showInput) Some(
            <(InputPopup())(^.wrapped := InputPopupProps(
              message = "Please, enter a value",
              onOk = { inputValue =>
                self.setState(_.copy(
                  showInput = false,
                  inputValue = inputValue,
                  showOk = true,
                  okMessage = s"You've entered:\n\n$inputValue"
                ))
              },
              onCancel = { () =>
                self.setState(_.copy(showInput = false))
              },
              initialValue = self.state.inputValue
            ))()
          ) else None
        ),

        <.h2()("OK/Yes/No/Cancel Popups"),
        <.hr()(),
        <.p()(
          <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(List(
            SimpleButtonData("ok", "OK", primary = true),
            SimpleButtonData("yes-no-cancel", "Yes/No/Cancel", primary = true),
            SimpleButtonData("yes-no", "Yes/No", primary = true)
          ),
          ActionsData(Set("ok", "yes-no-cancel", "yes-no"), _ => {
            case "ok" => self.setState(_.copy(showOk = true, okMessage = "Hello World!"))
            case "yes-no-cancel" => self.setState(_.copy(showYesNoCancel = true))
            case "yes-no" => self.setState(_.copy(showYesNo = true))
          })))(),

          if (self.state.showOk) Some(
            <(OkPopup())(^.wrapped := OkPopupProps(
              message = self.state.okMessage,
              image = Some(IconCss.dialogInformation),
              onClose = { () =>
                self.setState(_.copy(showOk = false))
              }
            ))()
          ) else None,
          
          if (self.state.showYesNoCancel) Some(
            <(YesNoCancelPopup())(^.wrapped := YesNoCancelPopupProps(
              message = "Do you like Scala.js ?",
              image = Some(IconCss.dialogQuestion),
              onSelect = {
                case Yes => self.setState(_.copy(showYesNoCancel = false, showOk = true, okMessage = "You selected: YES"))
                case No => self.setState(_.copy(showYesNoCancel = false, showOk = true, okMessage = "You selected: NO"))
                case _ => self.setState(_.copy(showYesNoCancel = false))
              }
            ))()
          ) else None,
          
          if (self.state.showYesNo) Some(
            <(YesNoPopup())(^.wrapped := YesNoPopupProps(
              message = "Do you like Scala.js and React.js ?",
              image = Some(IconCss.dialogQuestion),
              onSelect = {
                case Yes => self.setState(_.copy(showYesNo = false, showOk = true, okMessage = "You selected: YES"))
                case _ => self.setState(_.copy(showYesNo = false, showOk = true, okMessage = "You selected: NO"))
              }
            ))()
          ) else None
        ),

        <.h2()("Error Popup"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Error", primary = true), { () =>
            self.setState(_.copy(showError = true))
          }))(),
          
          if (self.state.showError) Some(
            <(ErrorPopup())(^.wrapped := ErrorPopupProps(
              error = "Some error occurred",
              exception = new Exception(),
              onClose = { () =>
                self.setState(_.copy(showError = false))
              }
            ))()
          ) else None
        ),

        <.h2()("Other Popups"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Loading/Status", primary = true), { () =>
            self.setState(_.copy(showLoading = true, showStatus = true))

            var timerId = 0
            timerId = dom.window.setInterval({ () =>
              self.setState(_.copy(showLoading = false))
              dom.window.clearInterval(timerId)
            }, 3000)
          }))(),
          
          if (self.state.showLoading) Some(
            <(LoadingPopup())()()
          ) else None,
          
          if (self.state.showStatus) Some(
            <(StatusPopup())(^.wrapped := StatusPopupProps("Fetching data...", { () =>
              if (!self.state.showLoading) {
                self.setState(_.copy(showStatus = false))
              }
            }))()
          ) else None
        )
      )
    }
  )
}
