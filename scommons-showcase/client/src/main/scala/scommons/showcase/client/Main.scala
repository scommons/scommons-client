package scommons.showcase.client

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.redux.ReactRedux._
import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.redux.{ReactRedux, Redux}
import io.github.shogowada.scalajs.reactjs.router.RouterProps
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalajs.dom
import scommons.client.app._
import scommons.client.ui.Buttons
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeData, BrowseTreeItemData, BrowseTreeNodeData}
import scommons.client.util.ActionsData

import scala.scalajs.js.JSApp

object Main extends JSApp {

  def main(): Unit = {
    val mountNode = dom.document.getElementById("root")

    dom.document.title = "scommons-showcase"

    val store = Redux.createStore(Reducer.reduce)

    val appMainPanelProps = AppMainPanelProps(
      name = "scommons-showcase",
      user = "me",
      copyright = "© scommons-showcase",
      version = "(version: 0.1.0-SNAPSHOT)"
    )

    val routes = (Reducer.defaultRoutes.values.map(_.path).toList :+ "/").map { path =>
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

      (state: ReduxState, _: Props[Unit]) => {
        AppBrowseControllerProps(
          state.roots,
          state.routes,
          List(Buttons.REFRESH, Buttons.ADD, Buttons.REMOVE, Buttons.EDIT)
        )
      }
    }
  )(AppBrowseController())
}

case class ReduxState(roots: List[BrowseTreeData],
                      routes: Map[BrowseTreeDataKey, AppBrowseData])

object Reducer {

  private val repoItem = BrowseTreeItemData("Repo", Some(Buttons.REMOVE.image))
  private val reposItem = BrowseTreeNodeData("Repos", Some(Buttons.ADD.image), List(repoItem))
  private val buttonsItem = BrowseTreeItemData("Buttons", Some(Buttons.CANCEL.image))
  private val widgetsNode = BrowseTreeNodeData("Widgets", List(buttonsItem, reposItem))

  private val defaultRoots = List(widgetsNode)

  val defaultRoutes = Map(
    widgetsNode.key -> AppBrowseData("/widgets", ActionsData.empty, None),
    buttonsItem.key -> AppBrowseData("/buttons", ActionsData.empty, Some(ButtonsDemo())),
    reposItem.key -> AppBrowseData("/repos",
      ActionsData(Set(Buttons.REFRESH.command, Buttons.ADD.command), _ => ()), Some(Repos())
    )
  )

  val reduce: (Option[ReduxState], Any) => ReduxState = (maybeState, action) =>
    ReduxState(
      roots = rootsReducer(maybeState.map(_.roots), action),
      routes = routesReducer(maybeState.map(_.routes), action)
    )

  private def rootsReducer(roots: Option[List[BrowseTreeData]],
                           action: Any): List[BrowseTreeData] = action match {

    case _ => roots.getOrElse(defaultRoots)
  }

  private def routesReducer(routes: Option[Map[BrowseTreeDataKey, AppBrowseData]],
                            action: Any): Map[BrowseTreeDataKey, AppBrowseData] = action match {

    case _ => routes.getOrElse(defaultRoutes)
  }
}

object Repos extends RouterProps {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { self =>
    <.div()(
      "Repos",
      <.Route(
        ^.path := s"${self.props.`match`.path}/:id",
        ^.component := Repo()
      )()
    )
  }
}

object Repo extends RouterProps {

  // Params has type of js.Dictionary[String]
  private def id(self: Self[_, _]): String = self.props.`match`.params("id")

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { self =>
    <.div(^.id := s"repo-${id(self)}")(s"Repo ${id(self)}")
  }
}
