package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.UiComponent
import scommons.client.ui.tree.TreeCss._

case class TreeNodeProps(isNode: Boolean,
                         level: Int,
                         expanded: Boolean,
                         onExpand: () => Unit,
                         renderValue: () => ReactElement)

object TreeNode extends UiComponent[TreeNodeProps] {

  private[tree] type TreeNodeSelf = Self[TreeNodeProps, Unit]

  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp

  private def createComp = React.createClass[PropsType, Unit]{ self =>
    val props = self.props.wrapped

    val nodeClass = if (props.isNode) treeNode else ""
    val valueClass = if (props.isNode) treeNodeValue else treeItemValue
    val arrowClass = if (props.expanded) treeOpenArrow else treeClosedArrow

    val item = <.div(
      ^.className := treeItem,
      if (props.level == 0) None
      else Some(^.style := Map("paddingLeft" -> s"${props.level * 16}px"))
    )(
      <.div(^.className := nodeClass)(
        if (props.isNode) {
          <.div(
            ^.className := s"$treeItem $treeNodeIcon",
            ^.onClick := arrowClick(self)
          )(
            <.div(^.className := arrowClass)()
          )
        }
        else None,
        <.div(^.className := valueClass)(
          props.renderValue()
        )
      )
    )

    if (props.isNode) {
      <.div()(
        item,
        self.props.children
      )
    }
    else item
  }

  private[tree] def arrowClick(self: TreeNodeSelf): MouseSyntheticEvent => Unit = { event =>
    event.stopPropagation()

    val props = self.props.wrapped
    props.onExpand()
  }
}
