package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.tree.BrowseTreeCss._

case class BrowseTreeNodeProps(data: BrowseTreeData,
                               level: Int,
                               selected: Boolean,
                               onSelect: BrowseTreeData => Unit,
                               expanded: Boolean,
                               onExpand: BrowseTreeData => Unit)

object BrowseTreeNode {

  type BrowseTreeNodeSelf = Self[BrowseTreeNodeProps, Unit]

  lazy val reactClass: ReactClass = React.createClass[BrowseTreeNodeProps, Unit]{ self =>
    val props = self.props.wrapped

    val (itemText, isNode) = props.data match {
      case BrowseTreeItemData(text) => (text, false)
      case BrowseTreeNodeData(text, _) => (text, true)
    }

    val topItemClass = if (props.level == 0) browseTreeTopItem else ""
    val topItemImageClass = if (props.level == 0) browseTreeTopItemImageValue else ""
    val selectedClass = if (props.selected) browseTreeSelectedItem else ""
    val nodeClass = if (isNode) browseTreeNode else ""
    val arrowClass = if (props.expanded) browseTreeOpenArrow else browseTreeClosedArrow

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
        if (props.expanded) Map.empty[String, String]
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

  private def itemClick(self: BrowseTreeNodeSelf): MouseSyntheticEvent => Unit = { _ =>
    val props = self.props.wrapped
    props.onSelect(props.data)
  }

  private[tree] def arrowClick(self: BrowseTreeNodeSelf): MouseSyntheticEvent => Unit = { event =>
    event.stopPropagation()

    val props = self.props.wrapped
    props.onExpand(props.data)
  }
}
