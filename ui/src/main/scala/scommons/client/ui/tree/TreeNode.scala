package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.react._

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

object TreeNode extends FunctionComponent[TreeNodeProps] {

  protected def render(compProps: Props): ReactElement = {
    val props = compProps.wrapped

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
          <.div(^.className := props.nodeIconClass, ^.onClick := arrowClick(props))(
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
        compProps.children
      )
    }
    else item
  }

  private def arrowClick(props: TreeNodeProps): MouseSyntheticEvent => Unit = { event =>
    event.stopPropagation()

    props.onExpand()
  }
}
