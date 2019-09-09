package scommons.client.showcase.demo

import org.scalajs.dom
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.popup._
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

object PopupsDemo extends FunctionComponent[Unit] {

  private case class ModalState(showModal: Boolean = false,
                                showInput: Boolean = false,
                                inputValue: String = "initial value",
                                showOk: Boolean = false,
                                okMessage: String = "",
                                showYesNoCancel: Boolean = false,
                                showYesNo: Boolean = false,
                                showError: Boolean = false,
                                showLoading: Boolean = false,
                                showStatus: Boolean = false)

  protected def render(props: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => ModalState())
    
    <.div()(
      <.h2()("Modal"),
      <.hr()(),
      <.p()(
        <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Modal", primary = true), { () =>
          setState(_.copy(showModal = true))
        }))(),
        
        if (state.showModal) Some(
          <(Modal())(^.wrapped := ModalProps(
            Some("Modal header"),
            List(Buttons.OK, Buttons.CANCEL),
            ActionsData(Set(Buttons.OK.command, Buttons.CANCEL.command), _ => {
              case Buttons.CANCEL.command => setState(_.copy(showModal = false))
              case _ =>
            }),
            onClose = { () =>
              setState(_.copy(showModal = false))
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
          setState(_.copy(showInput = true))
        }))(),
        
        if (state.showInput) Some(
          <(InputPopup())(^.wrapped := InputPopupProps(
            message = "Please, enter a value",
            onOk = { inputValue =>
              setState(_.copy(
                showInput = false,
                inputValue = inputValue,
                showOk = true,
                okMessage = s"You've entered:\n\n$inputValue"
              ))
            },
            onCancel = { () =>
              setState(_.copy(showInput = false))
            },
            initialValue = state.inputValue
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
          case "ok" => setState(_.copy(showOk = true, okMessage = "Hello World!"))
          case "yes-no-cancel" => setState(_.copy(showYesNoCancel = true))
          case "yes-no" => setState(_.copy(showYesNo = true))
        })))(),

        if (state.showOk) Some(
          <(OkPopup())(^.wrapped := OkPopupProps(
            message = state.okMessage,
            image = Some(IconCss.dialogInformation),
            onClose = { () =>
              setState(_.copy(showOk = false))
            }
          ))()
        ) else None,
        
        if (state.showYesNoCancel) Some(
          <(YesNoCancelPopup())(^.wrapped := YesNoCancelPopupProps(
            message = "Do you like Scala.js ?",
            image = Some(IconCss.dialogQuestion),
            onSelect = {
              case Yes => setState(_.copy(showYesNoCancel = false, showOk = true, okMessage = "You selected: YES"))
              case No => setState(_.copy(showYesNoCancel = false, showOk = true, okMessage = "You selected: NO"))
              case _ => setState(_.copy(showYesNoCancel = false))
            }
          ))()
        ) else None,
        
        if (state.showYesNo) Some(
          <(YesNoPopup())(^.wrapped := YesNoPopupProps(
            message = "Do you like Scala.js and React.js ?",
            image = Some(IconCss.dialogQuestion),
            onSelect = {
              case Yes => setState(_.copy(showYesNo = false, showOk = true, okMessage = "You selected: YES"))
              case _ => setState(_.copy(showYesNo = false, showOk = true, okMessage = "You selected: NO"))
            }
          ))()
        ) else None
      ),

      <.h2()("Error Popup"),
      <.hr()(),
      <.p()(
        <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Error", primary = true), { () =>
          setState(_.copy(showError = true))
        }))(),
        
        if (state.showError) Some(
          <(ErrorPopup())(^.wrapped := ErrorPopupProps(
            error = "Some error occurred",
            details = Some("Some\n\terror\n\t\tdetails"),
            onClose = { () =>
              setState(_.copy(showError = false))
            }
          ))()
        ) else None
      ),

      <.h2()("Other Popups"),
      <.hr()(),
      <.p()(
        <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Loading/Status", primary = true), { () =>
          setState(_.copy(showLoading = true, showStatus = true))

          var timerId = 0
          timerId = dom.window.setInterval({ () =>
            setState(_.copy(showLoading = false))
            dom.window.clearInterval(timerId)
          }, 3000)
        }))(),
        
        if (state.showLoading) Some(
          <(LoadingPopup())()()
        ) else None,
        
        if (state.showStatus) Some(
          <(StatusPopup())(^.wrapped := StatusPopupProps("Fetching data...", { () =>
            if (!state.showLoading) {
              setState(_.copy(showStatus = false))
            }
          }))()
        ) else None
      )
    )
  }
}
