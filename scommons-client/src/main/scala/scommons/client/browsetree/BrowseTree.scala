package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.browsetree.BrowseTreeCss._

case class BrowseTreeProps(roots: List[BrowseTreeData])

case class BrowseTreeState(selected: Option[BrowseTreeData] = None)

object BrowseTree {

  lazy val reactClass: ReactClass = React.createClass[BrowseTreeProps, BrowseTreeState](
    getInitialState = { _ =>
      BrowseTreeState()
    },
    render = { self =>
      val props = self.props.wrapped

      def isSelected(data: BrowseTreeData): Boolean = self.state.selected.contains(data)

      def setSelected(data: BrowseTreeData): Unit = self.setState(_.copy(selected = Some(data)))

      def createElements(items: List[BrowseTreeData], level: Int): List[ReactElement] = items.map { data =>
        <(BrowseTreeNode.reactClass)(^.wrapped := BrowseTreeNodeProps(
          data,
          level,
          isSelected,
          setSelected
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
}
