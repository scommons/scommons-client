package scommons.showcase.client.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import org.scalajs.dom
import play.api.libs.json.Json
import scommons.api.StatusResponse
import scommons.api.http.js.JsApiHttpClient
import scommons.client.task.FutureTask
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup._
import scommons.client.util.ActionsData
import scommons.showcase.api.ShowcaseApiClient
import scommons.showcase.api.repo.{RepoData, RepoListResp, RepoResp}
import scommons.showcase.client.ShowcaseState
import scommons.showcase.client.action.{CreateRepo, FailingApiAction, FetchRepos}

import scala.concurrent.ExecutionContext.Implicits.global

object ApiDemoController {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (dispatch: Dispatch) => {
      val onFetchRepos = (task: FutureTask[RepoListResp]) => dispatch(FetchRepos(task))
      val onCreateRepo = (task: FutureTask[RepoResp]) => dispatch(CreateRepo(task))
      val onFailingTask = (task: FutureTask[StatusResponse]) => dispatch(FailingApiAction(task))

      (_: ShowcaseState, _: Props[Unit]) => {
        ApiDemoProps(
          onFetchRepos = onFetchRepos,
          onCreateRepo = onCreateRepo,
          onFailingTask = onFailingTask
        )
      }
    }
  )(ApiDemo())
}

case class ApiDemoProps(onFetchRepos: FutureTask[RepoListResp] => _,
                        onCreateRepo: FutureTask[RepoResp] => _,
                        onFailingTask: FutureTask[StatusResponse] => _)

object ApiDemo {

  private case class ApiDemoState(showInput: Boolean = false,
                                  showOk: Boolean = false,
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
              SimpleButtonData("get", "GET", primary = true),
              SimpleButtonData("post", "POST", primary = true),
              SimpleButtonData("timedout", "Timedout", primary = true),
              SimpleButtonData("failed", "Failed", primary = true)
            ),
            ActionsData(Set("get", "post", "timedout", "failed"), {
              case "get" => callGetRepos(self)
              case "post" => self.setState(_.copy(showInput = true))
              case "timedout" => callTimedout(self)
              case "failed" => callFailed(self)
            })
          ))(),

          <(InputPopup())(^.wrapped := InputPopupProps(
            self.state.showInput,
            "Enter repo name:",
            onOk = { text =>
              self.setState(_.copy(showInput = false))
              createRepo(self, text)
            },
            onCancel = { () =>
              self.setState(_.copy(showInput = false))
            },
            initialValue = "Test Repo"
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

  private val baseUrl = {
    val loc = dom.window.location
    s"${loc.protocol}//${loc.host}/scommons-showcase"
  }

  private val client = new ShowcaseApiClient(new JsApiHttpClient(baseUrl))

  private def callGetRepos(self: Self[ApiDemoProps, ApiDemoState]): Unit = {
    val task = FutureTask("Fetching Repos", client.getRepos)
    task.future.map { resp =>
      val json = Json.prettyPrint(Json.toJson(resp))
      val msg = s"Received successful response:\n\n$json"

      self.setState(_.copy(okMessage = msg, showOk = true))
    }

    self.props.wrapped.onFetchRepos(task)
  }

  private def createRepo(self: Self[ApiDemoProps, ApiDemoState], name: String): Unit = {
    val task = FutureTask("Creating Repo", client.createRepo(RepoData(None, name)))
    task.future.foreach { resp =>
      if (resp.status.successful) {
        val json = Json.prettyPrint(Json.toJson(resp))
        val msg = s"Received successful response:\n\n$json"

        self.setState(_.copy(okMessage = msg, showOk = true))
      }
    }

    self.props.wrapped.onCreateRepo(task)
  }

  private def callTimedout(self: Self[ApiDemoProps, ApiDemoState]): Unit = {
    val task = FutureTask("Calling timedout endpoint", client.timedoutExample())

    self.props.wrapped.onFailingTask(task)
  }

  private def callFailed(self: Self[ApiDemoProps, ApiDemoState]): Unit = {
    val task = FutureTask("Calling failed endpoint", client.failedExample())

    self.props.wrapped.onFailingTask(task)
  }
}
