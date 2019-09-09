package scommons.client.ui.popup

import scommons.client.ui.icon.IconCss
import scommons.client.ui.{HTML, HTMLProps, SimpleButtonData}
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class ErrorPopupProps(error: String,
                           onClose: () => Unit,
                           details: Option[String] = None)

object ErrorPopup extends FunctionComponent[ErrorPopupProps] {

  private case class ErrorPopupState(showDetails: Boolean = false,
                                     opened: Boolean = false)

  protected def render(compProps: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => ErrorPopupState())
      
    val props = compProps.wrapped
    val detailsBtn = SimpleButtonData("details",
      if (state.showDetails) "Details <<"
      else "Details >>"
    )
    val closeBtn = SimpleButtonData("close", "Close", primary = true)

    <(Modal())(^.wrapped := ModalProps(
      header = None,
      buttons = if (props.details.isDefined) List(detailsBtn, closeBtn) else List(closeBtn),
      actions = ActionsData(Set(detailsBtn.command, closeBtn.command), _ => {
        case detailsBtn.command => setState(s => s.copy(showDetails = !s.showDetails))
        case _ => props.onClose()
      },
        if (state.opened) Some(closeBtn.command)
        else None
      ),
      onClose = props.onClose,
      onOpen = { () =>
        setState(_.copy(opened = true))
      }
    ))(
      <.div(^.className := "row-fluid")(
        <.img(^.className := IconCss.dialogError, ^.src := "")(),
        <(HTML())(^.wrapped := HTMLProps(
          if (state.showDetails) getFullText(props)
          else props.error
          ,
          wordWrap = false
        ))()
      )
    )
  }

  private def getFullText(props: ErrorPopupProps): String = {
    HTML.makeHtmlText(s"${props.error}\n\n${props.details.getOrElse("")}")
  }
}
