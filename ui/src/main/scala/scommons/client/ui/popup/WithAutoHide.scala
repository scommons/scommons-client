package scommons.client.ui.popup

import org.scalajs.dom
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLElement
import scommons.react._

import scala.scalajs.js

case class WithAutoHideProps(onHide: () => Unit)

object WithAutoHide extends ClassComponent[WithAutoHideProps] {

  private case class WithAutoHideState(setAutoHideDiv: HTMLElement => Unit,
                                       getAutoHideDiv: () => HTMLElement,
                                       autoHideHandle: Option[js.Function1[Event, Unit]] = None)

  private[popup] var addDomListener: (String, js.Function1[Event, Unit]) => Unit = { (`type`, listener) =>
    dom.document.addEventListener(`type`, listener)
  }

  private[popup] var removeDomListener: (String, js.Function1[Event, Unit]) => Unit = { (`type`, listener) =>
    dom.document.removeEventListener(`type`, listener)
  }

  protected def create(): ReactClass = createClass[WithAutoHideState](
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
      addDomListener("mouseup", autoHideHandle)
      addDomListener("keydown", autoHideHandle)

      self.setState(_.copy(autoHideHandle = Some(autoHideHandle)))
    },
    componentWillUnmount = { self =>
      self.state.autoHideHandle match {
        case None =>
        case Some(autoHideHandle) =>
          removeDomListener("mouseup", autoHideHandle)
          removeDomListener("keydown", autoHideHandle)
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
    require(autoHideDiv != null, "autoHideDiv should not be null")

    val target = e.target.asInstanceOf[HTMLElement]
    if (!autoHideDiv.contains(target)) {
      onHide()
    }
  }
}
