package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.ui.tree.BrowseTreeData.BrowseTreeDataKey

case class BrowseTreeProps(roots: List[BrowseTreeData],
                           selectedItem: Option[BrowseTreeDataKey] = None,
                           onSelect: BrowseTreeData => Unit = _ => ())

case class BrowseTreeState(selected: Option[BrowseTreeDataKey] = None, //TODO: remove (use selected item from props)
                           expanded: Set[BrowseTreeDataKey] = Set.empty[BrowseTreeDataKey])

object BrowseTree {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[BrowseTreeProps, BrowseTreeState](
    getInitialState = { _ =>
      BrowseTreeState()
    },
    render = { self =>
      val props = self.props.wrapped

      def createElements(items: List[BrowseTreeData], level: Int): List[ReactElement] = items.map { data =>
        <(BrowseTreeNode())(^.wrapped := BrowseTreeNodeProps(
          data,
          level,
          props.selectedItem.contains(data.key),//isSelected(self.state, data),
          selectedData => {
            if (!props.selectedItem.contains(selectedData.key)) {
              props.onSelect(selectedData)
            }
          },
          isExpanded(self.state, data),
          toggleExpanded(self.setState)
        ))(data match {
          case _: BrowseTreeItemData => None
          case data: BrowseTreeNodeData =>
            createElements(data.children, level + 1)
        })
      }

      <.div(^.className := browseTree)(
        createElements(props.roots, 0)
      )
    }
  )

  private[tree] def isSelected(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.selected.contains(data.key)
  }

  private[tree] def setSelected(setState: (BrowseTreeState => BrowseTreeState) => Unit)
                                     (data: BrowseTreeData): Unit = {

    setState(_.copy(selected = Some(data.key)))
  }

  private[tree] def isExpanded(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.expanded.contains(data.key)
  }

  private[tree] def toggleExpanded(setState: (BrowseTreeState => BrowseTreeState) => Unit)
                                        (data: BrowseTreeData): Unit = {
    setState { state =>
      val newExpanded =
        if (isExpanded(state, data)) state.expanded - data.key
        else state.expanded + data.key

      state.copy(expanded = newExpanded)
    }
  }
}
