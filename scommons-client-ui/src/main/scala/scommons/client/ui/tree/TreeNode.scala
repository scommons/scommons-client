package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.react.UiComponent

case class TreeNodeProps(isNode: Boolean,
                         paddingLeft: Int,
                         itemClass: String,
                         nodeClass: String,
                         nodeIconClass: String,
                         arrowClass: String,
                         valueClass: String,
                         onSelect: Option[() => Unit],
                         onExpand: () => Unit,
                         renderValue: () => ReactElement)

object TreeNode extends UiComponent[TreeNodeProps] {

  protected def create(): ReactClass = React.createClass[PropsType, Unit]{ self =>
    val props = self.props.wrapped

    val item = <.div(
      ^.className := props.itemClass,
      if (props.paddingLeft == 0) None
      else {
        ^.style := Map("paddingLeft" -> s"${props.paddingLeft}px")
      },
      props.onSelect.map { onSelect =>
        ^.onClick := { _: MouseSyntheticEvent =>
          onSelect()
        }
      }
    )(
      <.div(^.className := props.nodeClass)(
        if (props.isNode) {
          <.div(^.className := props.nodeIconClass, ^.onClick := arrowClick(self))(
            <.div(^.className := props.arrowClass)()
          )
        }
        else None,
        <.div(^.className := props.valueClass)(
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

  private[tree] def arrowClick(self: Self[TreeNodeProps, Unit]): MouseSyntheticEvent => Unit = { event =>
    event.stopPropagation()

    val props = self.props.wrapped
    props.onExpand()
  }
}
