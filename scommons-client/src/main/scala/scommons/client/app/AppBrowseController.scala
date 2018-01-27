package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.ui._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeData, BrowseTreeNodeData, BrowseTreeProps}
import scommons.client.util.ActionsData

case class AppBrowseControllerProps(treeRoots: List[BrowseTreeData],
                                    routes: Map[BrowseTreeDataKey, AppBrowseData],
                                    buttons: List[ButtonData],
                                    initiallyOpenedNodes: Set[BrowseTreeDataKey] = Set.empty)

object AppBrowseController extends RouterProps {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[AppBrowseControllerProps, Unit] { self =>
    val props = self.props.wrapped
    val path = self.props.location.pathname

    val selectedRoute = getSelectedRoute(props.routes, path)
    val selectedItem = selectedRoute.map(_._1)
    val openedNodes = selectedRoute.map { case (itemKey, _) =>
      getItemPath(props.treeRoots, itemKey.obj).map(_.key).toSet
    }.getOrElse(Set.empty[BrowseTreeDataKey])

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
      BrowseTreeProps(props.treeRoots, selectedItem, onSelect = onSelectItem,
        openedNodes = openedNodes, initiallyOpenedNodes = props.initiallyOpenedNodes)
    ))(panelComp)
  }

  private def getSelectedRoute(routes: Map[BrowseTreeDataKey, AppBrowseData],
                               path: String): Option[(BrowseTreeDataKey, AppBrowseData)] = {

    routes.find { case (_, panelData) =>
      panelData.path == path
    }
  }

  private[app] def getItemPath(roots: List[BrowseTreeData], item: BrowseTreeData): List[BrowseTreeData] = {
    def loop(nodes: List[BrowseTreeData], path: List[BrowseTreeData]): Option[List[BrowseTreeData]] = nodes match {
      case Nil => None
      case head :: tail =>
        if (head.key == item.key) Some(path)
        else {
          (head match {
            case node: BrowseTreeNodeData => loop(node.children, head :: path)
            case _ => None
          }) match {
            case None => loop (tail, path)
            case result => result
          }
        }
    }

    loop(roots, Nil) match {
      case None => Nil
      case Some(path) => path.reverse
    }
  }
}
