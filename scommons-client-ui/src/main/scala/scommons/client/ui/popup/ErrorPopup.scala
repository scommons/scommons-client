package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui.icon.IconCss
import scommons.client.ui.{HTML, HTMLProps, SimpleButtonData, UiComponent}
import scommons.client.util.ActionsData

case class ErrorPopupProps(show: Boolean,
                           error: String,
                           onClose: () => Unit,
                           details: Option[String] = None)

object ErrorPopupProps {

  def apply(show: Boolean,
            error: String,
            exception: Throwable,
            onClose: () => Unit): ErrorPopupProps = {

    ErrorPopupProps(show, error, onClose, Some(ErrorPopup.printStackTrace(exception)))
  }
}

object ErrorPopup extends UiComponent[ErrorPopupProps] {

  private case class ErrorPopupState(showDetails: Boolean = false,
                                     opened: Boolean = false)

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, ErrorPopupState](
    getInitialState = { _ =>
      ErrorPopupState()
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        self.setState(_.copy(showDetails = false, opened = false))
      }
    },
    render = { self =>
      val props = self.props.wrapped
      val detailsBtn = SimpleButtonData("details",
        if (self.state.showDetails) "Details <<"
        else "Details >>"
      )
      val closeBtn = SimpleButtonData("close", "Close", primary = true)

      <(Modal())(^.wrapped := ModalProps(props.show,
        None,
        if (props.details.isDefined) List(detailsBtn, closeBtn)
        else List(closeBtn),
        ActionsData(Set(detailsBtn.command, closeBtn.command), _ => {
          case detailsBtn.command => self.setState(s => s.copy(showDetails = !s.showDetails))
          case _ => props.onClose()
        },
          if (self.state.opened) Some(closeBtn.command)
          else None
        ),
        onClose = props.onClose,
        onOpen = { () =>
          self.setState(_.copy(opened = true))
        }
      ))(
        <.div(^.className := "row-fluid")(
          <.img(^.className := IconCss.dialogError, ^.src := "")(),
          <(HTML())(^.wrapped := HTMLProps(
            if (self.state.showDetails) getFullText(props)
            else props.error
            ,
            wordWrap = false
          ))()
        )
      )
    }
  )

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
