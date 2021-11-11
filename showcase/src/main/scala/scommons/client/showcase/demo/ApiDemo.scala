package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React.Props
import play.api.libs.json.Json
import scommons.client.showcase.ShowcaseState
import scommons.client.showcase.action.api.ApiActions
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup._
import scommons.client.util.ActionsData
import scommons.react._
import scommons.react.hooks._
import scommons.react.redux._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

object ApiDemoController extends BaseStateController[ShowcaseState, ApiDemoProps] {

  lazy val uiComponent: UiComponent[ApiDemoProps] = ApiDemo

  def mapStateToProps(dispatch: Dispatch, state: ShowcaseState, props: Props[Unit]): ApiDemoProps = {
    ApiDemoProps(dispatch)
  }
}

case class ApiDemoProps(dispatch: Dispatch)

object ApiDemo extends FunctionComponent[ApiDemoProps] {

  private case class ApiDemoState(showOk: Boolean = false,
                                  okMessage: String = "")

  protected def render(compProps: Props): ReactElement = {
    val (state, setState) = useStateUpdater(() => ApiDemoState())
    
    val props = compProps.wrapped

    def callSuccessful(): Unit = {
      val action = ApiActions.successfulAction(props.dispatch)
      action.task.future.andThen {
        case Success(resp) =>
          val json = Json.prettyPrint(Json.toJson(resp))
          val msg = s"Received successful response:\n\n$json"

          setState(_.copy(okMessage = msg, showOk = true))
      }

      props.dispatch(action)
    }

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
            case "successful" => callSuccessful()
            case "timedout" => callTimedout(props)
            case "failed" => callFailed(props)
          }),
          props.dispatch
        ))()
      ),

      if (state.showOk) Some(
        <(OkPopup())(^.wrapped := OkPopupProps(
          message = state.okMessage,
          image = Some(IconCss.dialogInformation),
          onClose = { () =>
            setState(_.copy(showOk = false))
          }
        ))()
      ) else None
    )
  }

  private def callTimedout(props: ApiDemoProps): Unit = {
    val action = ApiActions.timedoutAction()

    props.dispatch(action)
  }

  private def callFailed(props: ApiDemoProps): Unit = {
    val action = ApiActions.failedAction()

    props.dispatch(action)
  }
}
