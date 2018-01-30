package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.{ImageLabelWrapper, UiComponent}
import scommons.client.ui.tree.BrowseTreeCss._

case class BrowseTreeNodeProps(data: BrowseTreeData,
                               level: Int = 0,
                               selected: Boolean = false,
                               onSelect: BrowseTreeData => Unit = _ => (),
                               expanded: Boolean = false,
                               onExpand: BrowseTreeData => Unit = _ => ())

object BrowseTreeNode extends UiComponent[BrowseTreeNodeProps] {

  type BrowseTreeNodeSelf = Self[BrowseTreeNodeProps, Unit]

  def apply(): ReactClass = reactClass

  lazy val reactClass: ReactClass = React.createClass[PropsType, Unit]{ self =>
    val props = self.props.wrapped

    val (itemText, itemImage, isNode) = props.data match {
      case BrowseTreeItemData(text, _, image, _, _) => (text, image, false)
      case BrowseTreeNodeData(text, _, image, _, _, _) => (text, image, true)
    }

    val topItemClass = if (props.level == 0) browseTreeTopItem else ""
    val topItemImageClass = if (props.level == 0) browseTreeTopItemImageValue else ""
    val selectedClass = if (props.selected) browseTreeSelectedItem else ""
    val nodeClass = if (isNode) browseTreeNode else ""
    val valueClass = if (isNode) browseTreeNodeValue else browseTreeItemValue
    val arrowClass = if (props.expanded) browseTreeOpenArrow else browseTreeClosedArrow

    val itemStyle =
      if (props.level == 0) None
      else Some(^.style := Map("paddingLeft" -> s"${props.level * 16}px"))

    val treeItem = <.div(
      ^.className := s"$browseTreeItem $selectedClass $topItemClass",
      itemStyle,
      ^.onClick := itemClick(self)
    )(
      <.div(^.className := s"$browseTreeItem $nodeClass $topItemImageClass")(
        if (isNode) {
          <.div(^.className := s"$browseTreeNodeIcon", ^.onClick := arrowClick(self))(
            <.div(^.className := s"$arrowClass")()
          )
        }
        else None,
        <.div(^.className := s"$valueClass")(itemImage match {
          case None => itemText
          case Some(image) => ImageLabelWrapper(image, Some(itemText), alignText = false)
        })
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
