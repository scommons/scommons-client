package scommons.client.app

import io.github.shogowada.scalajs.reactjs.redux.Redux.Dispatch
import io.github.shogowada.scalajs.reactjs.router.dom.RouterDOM._
import io.github.shogowada.scalajs.reactjs.router.{RouterProps, WithRouter}
import scommons.client.ui._
import scommons.client.ui.tree._
import scommons.client.util.{ActionsData, BrowsePath}
import scommons.react._

case class AppBrowseControllerProps(buttons: List[ButtonData],
                                    treeRoots: List[BrowseTreeData],
                                    dispatch: Dispatch,
                                    initiallyOpenedNodes: Set[BrowsePath] = Set.empty)

object AppBrowseController extends FunctionComponent[AppBrowseControllerProps] with RouterProps {

  override protected def create(): ReactClass = WithRouter(super.create())

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped
    val path = compProps.location.pathname

    val selectedRoute = findItemAndPath(props.treeRoots, path)
    val selectedItem = selectedRoute.map(_._1.path)
    val openedNodes = selectedRoute.map { case (_, itemPath) =>
      itemPath.map(_.path).toSet
    }.getOrElse(Set.empty[BrowsePath])

    def onSelectItem(data: BrowseTreeData): Unit = {
      compProps.history.push(data.path.value)
    }

    val actions = selectedRoute.map(_._1.actions).getOrElse(ActionsData.empty)
    val allNodes = BrowseTreeData.flattenNodes(props.treeRoots)

    <(AppBrowsePanel())(^.wrapped := AppBrowsePanelProps(
      ButtonsPanelProps(props.buttons, actions, props.dispatch, group = true),
      BrowseTreeProps(props.treeRoots, selectedItem, onSelect = onSelectItem,
        openedNodes = openedNodes, initiallyOpenedNodes = props.initiallyOpenedNodes)
    ))(
      compProps.children,
      
      <.Switch()(
        allNodes.flatMap { n =>
          n.reactClass.map { comp =>
            <.Route(^.path := n.path.prefix, ^.exact := n.path.exact, ^.component := comp)()
          }
        }
      )
    )
  }

  private[app] def findItemAndPath(roots: List[BrowseTreeData],
                                   path: String): Option[(BrowseTreeData, List[BrowseTreeData])] = {

    def loop(nodes: List[BrowseTreeData], itemPath: List[BrowseTreeData]): List[BrowseTreeData] = nodes match {
      case Nil => Nil
      case head :: tail =>
        if (head.path.matches(path)) head :: itemPath
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
