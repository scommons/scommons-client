package scommons.client.showcase

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.scalajs.dom
import scommons.client.app._
import scommons.client.ui.popup.raw.NativeReactModal
import scommons.react._

object ShowcaseMain {

  def main(args: Array[String]): Unit = {
    val mountNode = dom.document.getElementById("root")

    NativeReactModal.setAppElement(mountNode)

    dom.document.title = "scommons-client-showcase"

    val store = Redux.createStore(ShowcaseReducer.reduce)

    val appMainPanelProps = AppMainPanelProps(
      name = "scommons-client-showcase",
      user = "me",
      copyright = "© scommons-client-showcase",
      version = "(version: 0.1.0-SNAPSHOT)"
    )

    ReactDOM.render(
      <.Provider(^.store := store)(
        <.HashRouter()(
          <(WithRouter(AppMainPanel()))(^.wrapped := appMainPanelProps)(
            <(ShowcaseRouteController()).empty,
            <(ShowcaseTaskController()).empty
          )
        )
      ),
      mountNode
    )
  }
}
