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
import scommons.client.ui._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeData, BrowseTreeItemData, BrowseTreeNodeData}

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

    val routes = Reducer.routes.map { path =>
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

object RouteController extends RouterProps {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = ReactRedux.connectAdvanced(
    (_: Dispatch) => {
      //val onSelect = (data: BrowseTreeData) => dispatch(SelectBrowseItem(data))

      def getSelectedItem(state: ReduxState, path: String): Option[BrowseTreeDataKey] = {
        state.routes.find { case (_, panelData) =>
          panelData.path == path
        }.map(_._1)
      }

      (state: ReduxState, ownProps: Props[Unit]) => {
        val path = ownProps.`match`.path

        AppBrowsePanelProps(
          state.roots,
          state.routes,
          getSelectedItem(state, path)
        )
      }
    }
  )(AppBrowsePanel())
}

case class ReduxState(roots: List[BrowseTreeData],
                      routes: Map[BrowseTreeDataKey, BrowsePanelData])

object Reducer {

  val routes = List("/widgets", "/repos", "/")

  private val reposItem = BrowseTreeItemData("Repos")
  private val widgetsNode = BrowseTreeNodeData("Widgets", List(reposItem))

  private val rootsDefault = List(widgetsNode)
  private val routesDefault = Map(
    widgetsNode.key -> BrowsePanelData("/widgets", Widgets()),
    reposItem.key -> BrowsePanelData("/repos", Repos())
  )

  val reduce: (Option[ReduxState], Any) => ReduxState = (maybeState, action) =>
    ReduxState(
      roots = rootsReducer(maybeState.map(_.roots), action),
      routes = routesReducer(maybeState.map(_.routes), action)
    )

  private def rootsReducer(roots: Option[List[BrowseTreeData]],
                           action: Any): List[BrowseTreeData] = action match {

    case _ => roots.getOrElse(rootsDefault)
  }

  private def routesReducer(routes: Option[Map[BrowseTreeDataKey, BrowsePanelData]],
                            action: Any): Map[BrowseTreeDataKey, BrowsePanelData] = action match {

    case _ => routes.getOrElse(routesDefault)
  }
}

object Widgets {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, Unit] { _ =>
    <.div()(
      <.h1()("Buttons"),
      <.div()(
        <(ImageButton())(^.wrapped := ImageButtonProps(ButtonImagesCss.accept, Some("Add")))(),
        <(ImageButton())(^.wrapped := ImageButtonProps(ButtonImagesCss.accept, Some("Disabled"),
          Some(ButtonImagesCss.acceptDisabled), disabled = true))(),
        <(ImageButton())(^.wrapped := ImageButtonProps(ButtonImagesCss.accept, Some("Primary"), primary = true))()
      )
    )
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
