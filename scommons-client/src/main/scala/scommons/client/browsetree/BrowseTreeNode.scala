package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.browsetree.BrowseTreeCss._

case class BrowseTreeNodeProps(data: BrowseTreeData,
                               level: Int,
                               isSelected: BrowseTreeData => Boolean,
                               setSelected: BrowseTreeData => Unit)

case class BrowseTreeNodeState(open: Boolean = false)

object BrowseTreeNode {

  lazy val reactClass: ReactClass = React.createClass[BrowseTreeNodeProps, BrowseTreeNodeState](
    getInitialState = { _ =>
      BrowseTreeNodeState()
    },
    render = { self =>
      val props = self.props.wrapped
      val state = self.state

      val (itemText, isNode) = props.data match {
        case BrowseTreeItemData(text) => (text, false)
        case BrowseTreeNodeData(text, _) => (text, true)
      }

      val topItemClass = if (props.level == 0) browseTreeTopItem else ""
      val topItemImageClass = if (props.level == 0) browseTreeTopItemImageValue else ""
      val selectedClass = if (props.isSelected(props.data)) browseTreeSelectedItem else ""
      val nodeClass = if (isNode) browseTreeNode else ""
      val arrowClass = if (state.open) browseTreeOpenArrow else browseTreeClosedArrow

      val itemStyle =
        if (props.level == 0) Map.empty[String, String]
        else Map("paddingLeft" -> s"${props.level * 16}px")

      val treeItem = <.div(
        ^.className := s"$browseTreeItem $selectedClass $topItemClass",
        ^.style := itemStyle,
        ^.onClick := itemClick(self)
      )(
        <.div(^.className := s"$browseTreeItem $nodeClass $topItemImageClass")(
          if (isNode) {
            <.div(^.className := s"$browseTreeNodeIcon", ^.onClick := arrowClick(self))(
              <.div(^.className := s"$arrowClass")()
            )
          }
          else None,
          <.div(^.className := s"$browseTreeItemValue")(itemText)
        )
      )

      if (isNode) {
        val childrenStyle =
          if (state.open) Map.empty[String, String]
          else Map("display" -> "none")

        <.div()(
          treeItem,
          <.div(^.style := childrenStyle)(
            self.props.children
          )
        )
      }
      else treeItem
    }
  )

  private def itemClick(self: Self[BrowseTreeNodeProps, BrowseTreeNodeState]) = { (_: MouseSyntheticEvent) =>
    val props = self.props.wrapped
    props.setSelected(props.data)
  }

  private def arrowClick(self: Self[BrowseTreeNodeProps, BrowseTreeNodeState]) = { (event: MouseSyntheticEvent) =>
    event.stopPropagation()
    self.setState(state => state.copy(open = !state.open))
  }
}
