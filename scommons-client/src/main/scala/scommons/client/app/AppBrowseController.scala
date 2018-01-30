package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.ui._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey
import scommons.client.ui.tree.{BrowseTreeData, BrowseTreeNodeData, BrowseTreeProps}
import scommons.client.util.ActionsData

case class AppBrowseControllerProps(buttons: List[ButtonData],
                                    treeRoots: List[BrowseTreeData],
                                    initiallyOpenedNodes: Set[BrowseTreeDataKey] = Set.empty)

object AppBrowseController extends RouterProps {

  def apply(): ReactClass = WithRouter(reactClass)

  private lazy val reactClass = React.createClass[AppBrowseControllerProps, Unit] { self =>
    val props = self.props.wrapped
    val path = self.props.location.pathname

    val selectedRoute = findItemAndPath(props.treeRoots, path)
    val selectedItem = selectedRoute.map(_._1.key)
    val openedNodes = selectedRoute.map { case (_, itemPath) =>
      itemPath.map(_.key).toSet
    }.getOrElse(Set.empty[BrowseTreeDataKey])

    def onSelectItem(data: BrowseTreeData): Unit = {
      self.props.history.push(data.path)
    }

    val actions = selectedRoute.map(_._1.actions).getOrElse(ActionsData.empty)

    val panelComp = selectedRoute.flatMap(_._1.reactClass).map { reactClass =>
      <(WithRouter(reactClass))()()
    }

    <(AppBrowsePanel())(^.wrapped := AppBrowsePanelProps(
      ButtonsPanelProps(props.buttons, actions, group = true),
      BrowseTreeProps(props.treeRoots, selectedItem, onSelect = onSelectItem,
        openedNodes = openedNodes, initiallyOpenedNodes = props.initiallyOpenedNodes)
    ))(panelComp)
  }

  private[app] def findItemAndPath(roots: List[BrowseTreeData],
                                   path: String): Option[(BrowseTreeData, List[BrowseTreeData])] = {

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
