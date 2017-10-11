package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.browsetree.BrowseTreeCss._
import scommons.client.browsetree.BrowseTreeData.BrowseTreeDataKey

case class BrowseTreeProps(roots: List[BrowseTreeData])

case class BrowseTreeState(selected: Option[BrowseTreeDataKey] = None,
                           expanded: Set[BrowseTreeDataKey] = Set.empty[BrowseTreeDataKey])

object BrowseTree {

  type BrowseTreeSelf = Self[BrowseTreeProps, BrowseTreeState]

  lazy val reactClass: ReactClass = React.createClass[BrowseTreeProps, BrowseTreeState](
    getInitialState = { _ =>
      BrowseTreeState()
    },
    render = { self =>
      val props = self.props.wrapped

      def createElements(items: List[BrowseTreeData], level: Int): List[ReactElement] = items.map { data =>
        <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps(
          data,
          level,
          isSelected(self, data),
          setSelected(self),
          isExpanded(self, data),
          toggleExpanded(self)
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

  private def isSelected(self: BrowseTreeSelf, data: BrowseTreeData): Boolean = {
    self.state.selected.contains(data.key)
  }

  private def setSelected(self: BrowseTreeSelf)(data: BrowseTreeData): Unit = {
    self.setState(_.copy(selected = Some(data.key)))
  }

  private def isExpanded(self: BrowseTreeSelf, data: BrowseTreeData): Boolean = {
    self.state.expanded.contains(data.key)
  }

  private def toggleExpanded(self: BrowseTreeSelf)(data: BrowseTreeData): Unit = {
    val expanded = isExpanded(self, data)

    self.setState { state =>
      val newExpanded =
        if (expanded) state.expanded - data.key
        else state.expanded + data.key

      state.copy(expanded = newExpanded)
    }
  }
}
