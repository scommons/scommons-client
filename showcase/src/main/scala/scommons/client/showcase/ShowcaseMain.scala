package scommons.client.showcase

import io.github.shogowada.scalajs.reactjs.React.Props
import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.{ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.router.WithRouter
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import org.scalajs.dom
import scommons.client.app._
import scommons.client.task.{TaskManager, TaskManagerProps}
import scommons.client.ui.Buttons
import scommons.client.ui.popup.raw.NativeReactModal

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
            <(RouteController()).empty,
            <(TaskController()).empty
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
    (dispatch: Dispatch) => {

      (state: ShowcaseState, _: Props[Unit]) => {
        AppBrowseControllerProps(
          List(Buttons.REFRESH, Buttons.ADD, Buttons.REMOVE, Buttons.EDIT),
          ShowcaseReducer.getTreeRoots(state),
          dispatch,
          Set(ShowcaseReducer.widgetsNode.path)
        )
      }
    }
  )(AppBrowseController())
}

object TaskController {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (_: Dispatch) => {

      (state: ShowcaseState, _: Props[Unit]) => {
        TaskManagerProps(state.currentTask)
      }
    }
  )(TaskManager())
}
