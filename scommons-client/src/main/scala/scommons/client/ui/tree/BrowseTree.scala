package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui.UiComponent
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey

case class BrowseTreeProps(roots: List[BrowseTreeData],
                           selectedItem: Option[BrowseTreeDataKey] = None,
                           onSelect: BrowseTreeData => Unit = _ => ())

object BrowseTree extends UiComponent[BrowseTreeProps] {

  private case class BrowseTreeState(expanded: Set[BrowseTreeDataKey] = Set.empty[BrowseTreeDataKey])

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, BrowseTreeState](
    getInitialState = { _ =>
      BrowseTreeState()
    },
    render = { self =>
      val props = self.props.wrapped

      def createElements(items: List[BrowseTreeData], level: Int): List[ReactElement] = items.map { data =>
        <(BrowseTreeNode())(^.wrapped := BrowseTreeNodeProps(
          data,
          level,
          isSelected(props, data),
          onSelect(props),
          isExpanded(self.state, data),
          toggleExpanded(self.setState)
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

  private def isSelected(props: BrowseTreeProps, data: BrowseTreeData): Boolean = {
    props.selectedItem.contains(data.key)
  }

  private def onSelect(props: BrowseTreeProps)(data: BrowseTreeData): Unit = {
    if (!isSelected(props, data)) {
      props.onSelect(data)
    }
  }

  private def isExpanded(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.expanded.contains(data.key)
  }

  private def toggleExpanded(setState: (BrowseTreeState => BrowseTreeState) => Unit)
                            (data: BrowseTreeData): Unit = {
    setState { state =>
      val newExpanded =
        if (isExpanded(state, data)) state.expanded - data.key
        else state.expanded + data.key

      state.copy(expanded = newExpanded)
    }
  }
}
