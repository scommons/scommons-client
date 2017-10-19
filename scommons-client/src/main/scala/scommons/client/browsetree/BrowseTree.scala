package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.browsetree.BrowseTreeCss._
import scommons.client.browsetree.BrowseTreeData.BrowseTreeDataKey

case class BrowseTreeProps(roots: List[BrowseTreeData],
                           onSelect: BrowseTreeData => Unit = _ => ())

case class BrowseTreeState(selected: Option[BrowseTreeDataKey] = None,
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
        <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps(
          data,
          level,
          isSelected(self.state, data),
          selectedData => {
            if (!isSelected(self.state, data)) {
              props.onSelect(selectedData)
              setSelected(self.setState)(selectedData)
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

  private[browsetree] def isSelected(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.selected.contains(data.key)
  }

  private[browsetree] def setSelected(setState: (BrowseTreeState => BrowseTreeState) => Unit)
                                     (data: BrowseTreeData): Unit = {

    setState(_.copy(selected = Some(data.key)))
  }

  private[browsetree] def isExpanded(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.expanded.contains(data.key)
  }

  private[browsetree] def toggleExpanded(setState: (BrowseTreeState => BrowseTreeState) => Unit)
                                        (data: BrowseTreeData): Unit = {
    setState { state =>
      val newExpanded =
        if (isExpanded(state, data)) state.expanded - data.key
        else state.expanded + data.key

      state.copy(expanded = newExpanded)
    }
  }
}
