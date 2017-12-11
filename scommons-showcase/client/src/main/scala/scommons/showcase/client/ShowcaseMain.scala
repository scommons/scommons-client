package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.{ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.scalajs.dom
import scommons.client.app._
import scommons.client.ui.Buttons

import scala.scalajs.js.JSApp

object ShowcaseMain extends JSApp {

  def main(): Unit = {
    val mountNode = dom.document.getElementById("root")

    dom.document.title = "scommons-showcase"

    val store = Redux.createStore(ShowcaseReducer.reduce)

    val appMainPanelProps = AppMainPanelProps(
      name = "scommons-showcase",
      user = "me",
      copyright = "© scommons-showcase",
      version = "(version: 0.1.0-SNAPSHOT)"
    )

    val routes = (ShowcaseReducer.defaultRoutes.values.map(_.path).toList :+ "/").map { path =>
      <.Route(^.path := path, ^.component := RouteController())()
    }

    ReactDOM.render(
      <.Provider(^.store := store)(
        <.HashRouter()(
          <(AppMainPanel())(^.wrapped := appMainPanelProps)(
            <.Switch()(
              routes
            )
          )
        )
      ),
      mountNode
    )
  }
}

object RouteController {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (_: Dispatch) => {

      (state: ShowcaseState, _: Props[Unit]) => {
        AppBrowseControllerProps(
          state.roots,
          state.routes,
          List(Buttons.REFRESH, Buttons.ADD, Buttons.REMOVE, Buttons.EDIT)
        )
      }
    }
  )(AppBrowseController())
}
