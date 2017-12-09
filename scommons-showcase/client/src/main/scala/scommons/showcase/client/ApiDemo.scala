package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import org.scalajs.dom
import play.api.libs.json.Json
import scommons.client.ui._
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup._
import scommons.showcase.api.ShowcaseApiJsClient
import scommons.showcase.api.repo.RepoData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object ApiDemo {

  private case class ApiDemoState(showInput: Boolean = false,
                                  showOk: Boolean = false,
                                  okMessage: String = "",
                                  error: String = "",
                                  errorDetails: Option[String] = None,
                                  showError: Boolean = false)

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, ApiDemoState](
    getInitialState = { _ => ApiDemoState() },
    render = { self =>
      <.div()(
        <.h2()("API"),
        <.hr()(),
        <.p()(
          <(SimpleButton())(^.wrapped := SimpleButtonProps(SimpleButtonData("", "Create Repo", primary = true), { () =>
            self.setState(_.copy(showInput = true))
          }))(),
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

        <(ErrorPopup())(^.wrapped := ErrorPopupProps(
          self.state.showError,
          self.state.error,
          details = self.state.errorDetails,
          onClose = { () =>
            self.setState(_.copy(showError = false))
          }
        ))()
      )
    }
  )

  private def createRepo(self: Self[Unit, ApiDemoState], name: String): Unit = {
    val baseUrl = {
      val loc = dom.window.location
      s"${loc.protocol}//${loc.host}/scommons-showcase"
    }

    val client = new ShowcaseApiJsClient(baseUrl)
    client.createRepo(RepoData(None, name)).onComplete {
      case Failure(e) =>
        val details = Some(ErrorPopup.printStackTrace(e))
        self.setState(_.copy(error = "Failed to create repo", errorDetails = details, showError = true))
      case Success(data) =>
        val json = Json.prettyPrint(Json.toJson(data))
        val msg = s"Received successful response:\n\n$json"
        self.setState(_.copy(okMessage = msg, showOk = true))
    }
  }
}
