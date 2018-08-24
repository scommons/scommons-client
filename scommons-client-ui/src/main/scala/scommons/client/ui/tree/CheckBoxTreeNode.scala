package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui._
import scommons.client.ui.tree.CheckBoxTreeCss._

case class CheckBoxTreeNodeProps(isNode: Boolean,
                                 level: Int,
                                 expanded: Boolean,
                                 onExpand: () => Unit,
                                 renderValue: () => ReactElement)

object CheckBoxTreeNode extends UiComponent[CheckBoxTreeNodeProps] {

  private[tree] type CheckBoxTreeNodeSelf = Self[CheckBoxTreeNodeProps, Unit]

  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp

  private def createComp = React.createClass[PropsType, Unit]{ self =>
    val props = self.props.wrapped

    val nodeClass = if (props.isNode) checkBoxTreeNode else ""
    val valueClass = if (props.isNode) checkBoxTreeNodeValue else checkBoxTreeItemValue
    val arrowClass = if (props.expanded) checkBoxTreeOpenArrow else checkBoxTreeClosedArrow

    val treeItem = <.div(
      ^.className := checkBoxTreeItem,
      if (props.level == 0) None
      else Some(^.style := Map("paddingLeft" -> s"${props.level * 16}px"))
    )(
      <.div(^.className := nodeClass)(
        if (props.isNode) {
          <.div(^.className := s"$checkBoxTreeItem $checkBoxTreeNodeIcon", ^.onClick := arrowClick(self))(
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
        treeItem,
        self.props.children
      )
    }
    else treeItem
  }

  private[tree] def arrowClick(self: CheckBoxTreeNodeSelf): MouseSyntheticEvent => Unit = { event =>
    event.stopPropagation()

    val props = self.props.wrapped
    props.onExpand()
  }
}
