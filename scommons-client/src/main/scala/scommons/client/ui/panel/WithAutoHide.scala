package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
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

  private type WithAutoHideSelf = Self[WithAutoHideProps, WithAutoHideState]

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
      val autoHideHandle: js.Function1[Event, Unit] = onAutoHide(self)
      dom.document.body.addEventListener("mouseup", autoHideHandle)
      dom.document.body.addEventListener("keyup", autoHideHandle)

      self.setState(_.copy(autoHideHandle = Some(autoHideHandle)))
    },
    componentWillUnmount = { self =>
      self.state.autoHideHandle match {
        case None =>
        case Some(autoHideHandle) =>
          dom.document.body.removeEventListener("mouseup", autoHideHandle)
          dom.document.body.removeEventListener("keyup", autoHideHandle)
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

  private def onAutoHide(self: WithAutoHideSelf): Event => Unit = { e =>
    val autoHideDiv = self.state.getAutoHideDiv()
    require(autoHideDiv != null, "autoHideDiv is not null")

    if (!autoHideDiv.contains(e.target.asInstanceOf[HTMLElement])) {
      self.props.wrapped.onHide()
    }
  }
}
