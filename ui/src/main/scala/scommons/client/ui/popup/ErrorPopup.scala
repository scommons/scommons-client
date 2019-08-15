package scommons.client.ui.popup

import scommons.client.ui.icon.IconCss
import scommons.client.ui.{HTML, HTMLProps, SimpleButtonData}
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._

case class ErrorPopupProps(error: String,
                           onClose: () => Unit,
                           details: Option[String] = None)

object ErrorPopupProps {

  def apply(error: String,
            exception: Throwable,
            onClose: () => Unit): ErrorPopupProps = {

    ErrorPopupProps(error, onClose, Some(ErrorPopup.printStackTrace(exception)))
  }
}

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

  def printStackTrace(x: Throwable): String = {
    val sb = new StringBuilder(x.toString)
    val trace = x.getStackTrace
    for (t <- trace) {
      sb.append("\n\tat&nbsp").append(t)
    }

    val cause = x.getCause
    if (cause != null) {
      printStackTraceAsCause(sb, cause, trace)
    }

    sb.toString
  }

  /**
    * Print stack trace as a cause for the specified stack trace.
    */
  private def printStackTraceAsCause(sb: StringBuilder,
                                     cause: Throwable,
                                     causedTrace: Array[StackTraceElement]): Unit = {

    // Compute number of frames in common between this and caused
    val trace = cause.getStackTrace
    var m = trace.length - 1
    var n = causedTrace.length - 1
    while (m >= 0 && n >= 0 && trace(m) == causedTrace(n)) {
      m -= 1
      n -= 1
    }

    val framesInCommon = trace.length - 1 - m
    sb.append("\nCaused by: " + cause)

    for (i <- 0 to m) {
      sb.append("\n\tat&nbsp").append(trace(i))
    }

    if (framesInCommon != 0) {
      sb.append("\n\t...&nbsp").append(framesInCommon).append("&nbspmore")
    }

    // Recurse if we have a cause
    val ourCause = cause.getCause
    if (ourCause != null) {
      printStackTraceAsCause(sb, ourCause, trace)
    }
  }
}
