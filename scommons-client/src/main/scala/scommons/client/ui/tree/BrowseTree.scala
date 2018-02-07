package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui.UiComponent
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.util.BrowsePath

case class BrowseTreeProps(roots: List[BrowseTreeData],
                           selectedItem: Option[BrowsePath] = None,
                           onSelect: BrowseTreeData => Unit = _ => (),
                           openedNodes: Set[BrowsePath] = Set.empty,
                           initiallyOpenedNodes: Set[BrowsePath] = Set.empty)

object BrowseTree extends UiComponent[BrowseTreeProps] {

  private case class BrowseTreeState(opened: Set[BrowsePath])

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, BrowseTreeState](
    getInitialState = { self =>
      val props = self.props.wrapped
      BrowseTreeState(props.initiallyOpenedNodes ++ props.openedNodes)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        val currKeys = getAllPaths(props.roots)
        self.setState(s => s.copy(opened = (s.opened ++ props.openedNodes).intersect(currKeys)))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      def createElements(items: List[BrowseTreeData], level: Int): List[ReactElement] = items.map { data =>
        <(BrowseTreeNode())(^.wrapped := BrowseTreeNodeProps(
          data,
          level,
          isSelected(props, data),
          onSelect(props),
          isOpen(self.state, data),
          toggleState(self)
        ))(data match {
          case _: BrowseTreeItemData => None
          case nodeData: BrowseTreeNodeData =>
            createElements(nodeData.children, level + 1)
        })
      }

      <.div(^.className := browseTree)(
        createElements(props.roots, 0)
      )
    }
  )

  private[tree] def getAllPaths(roots: List[BrowseTreeData]): Set[BrowsePath] = {
    def loop(nodes: List[BrowseTreeData], result: Set[BrowsePath]): Set[BrowsePath] = nodes match {
      case Nil => result
      case head :: tail =>
        loop(tail, head match {
          case node: BrowseTreeNodeData => loop(node.children, result)
          case _ => result
        }) + head.path
    }

    loop(roots, Set.empty[BrowsePath])
  }

  private def isSelected(props: BrowseTreeProps, data: BrowseTreeData): Boolean = {
    props.selectedItem.contains(data.path)
  }

  private def onSelect(props: BrowseTreeProps)(data: BrowseTreeData): Unit = {
    if (!isSelected(props, data)) {
      props.onSelect(data)
    }
  }

  private def isOpen(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.opened.contains(data.path)
  }

  private def toggleState(self: Self[BrowseTreeProps, BrowseTreeState])(data: BrowseTreeData): Unit = {
    val currOpen = isOpen(self.state, data)
    val newState =
      if (currOpen) self.state.opened - data.path
      else self.state.opened + data.path

    self.setState(_.copy(opened = newState))
  }
}
