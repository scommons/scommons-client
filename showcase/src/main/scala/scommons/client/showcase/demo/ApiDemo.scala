package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import play.api.libs.json.Json
import scommons.client.showcase.ShowcaseState
import scommons.client.showcase.action.api.ApiActions
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup._
import scommons.client.util.ActionsData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

object ApiDemoController {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      (_: ShowcaseState, _: Props[Unit]) => {
        ApiDemoProps(dispatch)
      }
    }
  )(ApiDemo())
}

case class ApiDemoProps(dispatch: Dispatch)

object ApiDemo {

  private case class ApiDemoState(showOk: Boolean = false,
                                  okMessage: String = "")

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[ApiDemoProps, ApiDemoState](
    getInitialState = { _ => ApiDemoState() },
    render = { self =>
      <.div()(
        <.h2()("API Calls"),
        <.hr()(),
        <.p()(
          <(ButtonsPanel())(^.wrapped := ButtonsPanelProps(
            List(
              SimpleButtonData("successful", "Successful", primary = true),
              SimpleButtonData("timedout", "Timedout", primary = true),
              SimpleButtonData("failed", "Failed", primary = true)
            ),
            ActionsData(Set("successful", "timedout", "failed"), _ => {
              case "successful" => callSuccessful(self)
              case "timedout" => callTimedout(self)
              case "failed" => callFailed(self)
            }),
            self.props.wrapped.dispatch
          ))()
        ),

        <(OkPopup())(^.wrapped := OkPopupProps(
          self.state.showOk,
          self.state.okMessage,
          image = Some(IconCss.dialogInformation),
          onClose = { () =>
            self.setState(_.copy(showOk = false))
          }
        ))()
      )
    }
  )

  private def callSuccessful(self: Self[ApiDemoProps, ApiDemoState]): Unit = {
    val action = ApiActions.successfulAction(self.props.wrapped.dispatch)
    action.task.future.andThen {
      case Success(resp) =>
        val json = Json.prettyPrint(Json.toJson(resp))
        val msg = s"Received successful response:\n\n$json"

        self.setState(_.copy(okMessage = msg, showOk = true))
    }

    self.props.wrapped.dispatch(action)
  }

  private def callTimedout(self: Self[ApiDemoProps, ApiDemoState]): Unit = {
    val action = ApiActions.timedoutAction()

    self.props.wrapped.dispatch(action)
  }

  private def callFailed(self: Self[ApiDemoProps, ApiDemoState]): Unit = {
    val action = ApiActions.failedAction()

    self.props.wrapped.dispatch(action)
  }
}
