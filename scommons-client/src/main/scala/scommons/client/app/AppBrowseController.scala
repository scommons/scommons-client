package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.ui._
import scommons.client.ui.tree._
import scommons.client.util.{ActionsData, BrowsePath}

case class AppBrowseControllerProps(buttons: List[ButtonData],
                                    treeRoots: List[BrowseTreeData],
                                    initiallyOpenedNodes: Set[BrowsePath] = Set.empty)

object AppBrowseController extends RouterProps {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[AppBrowseControllerProps, Unit] { self =>
    val props = self.props.wrapped
    val path = BrowsePath(self.props.location.pathname)

    val selectedRoute = findItemAndPath(props.treeRoots, path)
    val selectedItem = selectedRoute.map(_._1.path)
    val openedNodes = selectedRoute.map { case (_, itemPath) =>
      itemPath.map(_.path).toSet
    }.getOrElse(Set.empty[BrowsePath])

    def onSelectItem(data: BrowseTreeData): Unit = {
      self.props.history.push(data.path.value)
    }

    val actions = selectedRoute.map(_._1.actions).getOrElse(ActionsData.empty)
    val allNodes = BrowseTreeData.flattenNodes(props.treeRoots)

    <(AppBrowsePanel())(^.wrapped := AppBrowsePanelProps(
      ButtonsPanelProps(props.buttons, actions, group = true),
      BrowseTreeProps(props.treeRoots, selectedItem, onSelect = onSelectItem,
        openedNodes = openedNodes, initiallyOpenedNodes = props.initiallyOpenedNodes)
    ))(
      <.Switch()(
        allNodes.flatMap { n =>
          n.reactClass.map { comp =>
            <.Route(^.path := n.path.value, ^.component := comp)()
          }
        }
      )
    )
  }

  private[app] def findItemAndPath(roots: List[BrowseTreeData],
                                   path: BrowsePath): Option[(BrowseTreeData, List[BrowseTreeData])] = {

    def loop(nodes: List[BrowseTreeData], itemPath: List[BrowseTreeData]): List[BrowseTreeData] = nodes match {
      case Nil => Nil
      case head :: tail =>
        if (head.path == path) head :: itemPath
        else {
          (head match {
            case node: BrowseTreeNodeData => loop(node.children, head :: itemPath)
            case _ => Nil
          }) match {
            case Nil => loop(tail, itemPath)
            case result => result
          }
        }
    }

    loop(roots, Nil) match {
      case head :: tail => Some(head -> tail.reverse)
      case _ => None
    }
  }
}
