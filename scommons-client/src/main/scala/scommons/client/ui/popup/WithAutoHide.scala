package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

case class WithAutoHideProps(onHide: () => Unit)

object WithAutoHide {

  private case class WithAutoHideState(setAutoHideDiv: HTMLElement => Unit,
                                       getAutoHideDiv: () => HTMLElement,
                                       autoHideHandle: Option[js.Function1[Event, Unit]] = None)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[WithAutoHideProps, WithAutoHideState](
    getInitialState = { _ =>
      var autoHideDiv: HTMLElement = null

      WithAutoHideState({ ref =>
        autoHideDiv = ref
      }, { () =>
        autoHideDiv
      })
    },
    componentDidMount = { self =>
      val autoHideHandle: js.Function1[Event, Unit] = onAutoHide(
        self.state.getAutoHideDiv(),
        self.props.wrapped.onHide
      )
      dom.document.addEventListener("mouseup", autoHideHandle)
      dom.document.addEventListener("keyup", autoHideHandle)

      self.setState(_.copy(autoHideHandle = Some(autoHideHandle)))
    },
    componentWillUnmount = { self =>
      self.state.autoHideHandle match {
        case None =>
        case Some(autoHideHandle) =>
          dom.document.removeEventListener("mouseup", autoHideHandle)
          dom.document.removeEventListener("keyup", autoHideHandle)
          self.setState(_.copy(autoHideHandle = None))
      }
    },
    render = { self =>
      <.div(^.ref := { ref: HTMLElement =>
        self.state.setAutoHideDiv(ref)
      })(
        self.props.children
      )
    }
  )

  private[popup] def onAutoHide(autoHideDiv: HTMLElement, onHide: () => Unit): Event => Unit = { e =>
    require(autoHideDiv != null, "autoHideDiv is not null")

    val target = e.target.asInstanceOf[HTMLElement]
    if (!autoHideDiv.contains(target)) {
      onHide()
    }
  }
}
