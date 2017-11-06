package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.ui._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeData, BrowseTreeProps}
import scommons.client.util.ActionsData

case class AppBrowseControllerProps(treeRoots: List[BrowseTreeData],
                                    routes: Map[BrowseTreeDataKey, AppBrowseData],
                                    buttons: List[ButtonData])

object AppBrowseController extends RouterProps {

  def apply(): ReactClass = WithRouter(reactClass)

  private[app] lazy val reactClass = React.createClass[AppBrowseControllerProps, Unit] { self =>
    val props = self.props.wrapped
    val path = self.props.`match`.path

    val selectedRoute = getSelectedRoute(props.routes, path)
    val selectedItem = selectedRoute.map(_._1)

    def onSelectItem(data: BrowseTreeData): Unit = {
      props.routes.get(data.key).foreach { panel =>
        self.props.history.push(panel.path)
      }
    }

    val actions = selectedRoute.map(_._2.actions).getOrElse(ActionsData.empty)

    val panelComp = selectedRoute.flatMap(_._2.reactClass).map { reactClass =>
      <(WithRouter(reactClass))()()
    }

    <(AppBrowsePanel())(^.wrapped := AppBrowsePanelProps(
      ButtonsPanelProps(props.buttons, actions, group = true),
      BrowseTreeProps(props.treeRoots, selectedItem, onSelect = onSelectItem)
    ))(panelComp)
  }

  private def getSelectedRoute(routes: Map[BrowseTreeDataKey, AppBrowseData],
                               path: String): Option[(BrowseTreeDataKey, AppBrowseData)] = {

    routes.find { case (_, panelData) =>
      panelData.path == path
    }
  }
}
