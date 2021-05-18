package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React.Self
import scommons.client.ui.tree.TreeCss._
import scommons.client.ui.{ImageCheckBox, ImageCheckBoxProps, TriState}
import scommons.react._

case class CheckBoxTreeProps(roots: List[CheckBoxTreeData],
                             onChange: (CheckBoxTreeData, TriState) => Unit = (_, _) => (),
                             readOnly: Boolean = false,
                             openNodes: Set[String] = Set.empty,
                             closeNodes: Set[String] = Set.empty)

object CheckBoxTree extends ClassComponent[CheckBoxTreeProps] {

  private[tree] var treeNodeComp: UiComponent[TreeNodeProps] = TreeNode

  private case class CheckBoxTreeState(opened: Set[String])

  protected def create(): ReactClass = createClass[CheckBoxTreeState](
    getInitialState = { self =>
      val props = self.props.wrapped
      CheckBoxTreeState(props.openNodes -- props.closeNodes)
    },
    componentDidUpdate = { (self, prevProps, _) =>
      val props = self.props.wrapped
      if (props.openNodes != prevProps.wrapped.openNodes) {
        val currKeys = CheckBoxTreeData.flattenNodes(props.roots).map(_.key).toSet
        self.setState(s => s.copy(opened = (s.opened ++ props.openNodes).intersect(currKeys)))
      }
      if (props.closeNodes != prevProps.wrapped.closeNodes) {
        val currKeys = CheckBoxTreeData.flattenNodes(props.roots).map(_.key).toSet
        self.setState(s => s.copy(opened = (s.opened -- props.closeNodes).intersect(currKeys)))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      def createElements(items: List[CheckBoxTreeData], level: Int): List[ReactElement] = items.map { data =>
        val (isNode, isOpened, children) = data match {
          case _: CheckBoxTreeItemData => (false, false, Nil)
          case n: CheckBoxTreeNodeData => (true, isOpen(self.state, n), n.children)
        }

        <(treeNodeComp())(^.wrapped := TreeNodeProps(
          isNode = isNode,
          paddingLeft = level * 16,
          itemClass = treeItem,
          nodeClass = if (isNode) treeNode else "",
          nodeIconClass = s"$treeItem $treeNodeIcon",
          arrowClass = if (isOpened) treeOpenArrow else treeClosedArrow,
          valueClass = if (isNode) treeNodeValue else treeItemValue,
          onSelect = None,
          onExpand = { () =>
            toggleState(self, data)
          },
          renderValue = { () =>
            <(ImageCheckBox())(^.wrapped := ImageCheckBoxProps(
              value = data.value,
              image = data.image.getOrElse(""),
              text = data.text,
              onChange = { value =>
                props.onChange(data, value)
              },
              readOnly = props.readOnly
            ))()
          }
        ))(
          if (isNode && isOpened) createElements(children, level + 1)
          else Nil
        )
      }

      <.div(^.className := tree)(
        createElements(props.roots, 0)
      )
    }
  )

  private def isOpen(state: CheckBoxTreeState, data: CheckBoxTreeData): Boolean = {
    state.opened.contains(data.key)
  }

  private def toggleState(self: Self[CheckBoxTreeProps, CheckBoxTreeState], data: CheckBoxTreeData): Unit = {
    val currOpen = isOpen(self.state, data)
    val newState =
      if (currOpen) self.state.opened - data.key
      else self.state.opened + data.key

    self.setState(_.copy(opened = newState))
  }
}
