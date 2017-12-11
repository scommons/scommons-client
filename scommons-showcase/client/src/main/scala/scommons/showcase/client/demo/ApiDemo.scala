package scommons.showcase.client.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import play.api.libs.json.Json
import scommons.client.task.AbstractTask.AbstractTaskKey
import scommons.client.task.{FutureTask, TaskManager, TaskManagerProps}
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup._
import scommons.client.util.ActionsData
import scommons.showcase.api.ShowcaseApiJsClient
import scommons.showcase.api.repo.RepoData

import scala.concurrent.ExecutionContext.Implicits.global

object ApiDemo {

  private case class ApiDemoState(showInput: Boolean = false,
                                  showOk: Boolean = false,
                                  okMessage: String = "",
                                  startTask: Option[AbstractTaskKey] = None)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, ApiDemoState](
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
        ))(),

        <(TaskManager())(^.wrapped := TaskManagerProps(
          self.state.startTask
        ))()
      )
    }
  )

  private val baseUrl = {
    val loc = dom.window.location
    s"${loc.protocol}//${loc.host}/scommons-showcase"
  }

  private val client = new ShowcaseApiJsClient(baseUrl)

  private def callGetRepos(self: Self[Unit, ApiDemoState]): Unit = {
    val task = FutureTask("Fetching Repos", client.getRepos.map { resp =>
      val json = Json.prettyPrint(Json.toJson(resp))
      val msg = s"Received successful response:\n\n$json"
      self.setState(_.copy(startTask = None, okMessage = msg, showOk = true))
    })

    self.setState(_.copy(startTask = Some(task.key)))
  }

  private def createRepo(self: Self[Unit, ApiDemoState], name: String): Unit = {
    val task = FutureTask("Creating Repo", client.createRepo(RepoData(None, name)).map { resp =>
      val json = Json.prettyPrint(Json.toJson(resp))
      val msg = s"Received successful response:\n\n$json"
      self.setState(_.copy(startTask = None, okMessage = msg, showOk = true))
    })

    self.setState(_.copy(startTask = Some(task.key)))
  }

  private def callTimedout(self: Self[Unit, ApiDemoState]): Unit = {
    val task = FutureTask("Calling timedout endpoint", client.timedoutExample())

    self.setState(_.copy(startTask = Some(task.key)))
  }

  private def callFailed(self: Self[Unit, ApiDemoState]): Unit = {
    val task = FutureTask("Calling failed endpoint", client.failedExample())

    self.setState(_.copy(startTask = Some(task.key)))
  }
}
