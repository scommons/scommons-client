package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui.UiComponent
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey

case class BrowseTreeProps(roots: List[BrowseTreeData],
                           selectedItem: Option[BrowseTreeDataKey] = None,
                           onSelect: BrowseTreeData => Unit = _ => (),
                           openedNodes: Set[BrowseTreeDataKey] = Set.empty[BrowseTreeDataKey])

object BrowseTree extends UiComponent[BrowseTreeProps] {

  private case class BrowseTreeState(opened: Set[BrowseTreeDataKey])

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, BrowseTreeState](
    getInitialState = { self =>
      BrowseTreeState(self.props.wrapped.openedNodes)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        val currKeys = getAllKeys(props.roots)
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

  private[tree] def getAllKeys(roots: List[BrowseTreeData]): Set[BrowseTreeDataKey] = {
    def loop(nodes: List[BrowseTreeData], result: Set[BrowseTreeDataKey]): Set[BrowseTreeDataKey] = nodes match {
      case Nil => result
      case head :: tail =>
        loop(tail, head match {
          case node: BrowseTreeNodeData => loop(node.children, result)
          case _ => result
        }) + head.key
    }

    loop(roots, Set.empty[BrowseTreeDataKey])
  }

  private def isSelected(props: BrowseTreeProps, data: BrowseTreeData): Boolean = {
    props.selectedItem.contains(data.key)
  }

  private def onSelect(props: BrowseTreeProps)(data: BrowseTreeData): Unit = {
    if (!isSelected(props, data)) {
      props.onSelect(data)
    }
  }

  private def isOpen(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.opened.contains(data.key)
  }

  private def toggleState(self: Self[BrowseTreeProps, BrowseTreeState])(data: BrowseTreeData): Unit = {
    val currOpen = isOpen(self.state, data)
    val newState =
      if (currOpen) self.state.opened - data.key
      else self.state.opened + data.key

    self.setState(_.copy(opened = newState))
  }
}
