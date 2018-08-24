package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui.{ImageCheckBox, ImageCheckBoxProps, TriState, UiComponent}
import scommons.client.ui.tree.CheckBoxTreeCss._

case class CheckBoxTreeProps(roots: List[CheckBoxTreeData],
                             onChange: (CheckBoxTreeData, TriState) => Unit,
                             readOnly: Boolean = false,
                             openNodes: Set[String] = Set.empty,
                             closeNodes: Set[String] = Set.empty)

object CheckBoxTree extends UiComponent[CheckBoxTreeProps] {

  private case class CheckBoxTreeState(opened: Set[String])

  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp
  
  private def createComp = React.createClass[PropsType, CheckBoxTreeState](
    getInitialState = { self =>
      val props = self.props.wrapped
      CheckBoxTreeState(props.openNodes -- props.closeNodes)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped.openNodes != props.openNodes) {
        val currKeys = CheckBoxTreeData.flattenNodes(props.roots).map(_.key).toSet
        self.setState(s => s.copy(opened = (s.opened ++ props.openNodes).intersect(currKeys)))
      }
      if (self.props.wrapped.closeNodes != props.closeNodes) {
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

        <(CheckBoxTreeNode())(^.wrapped := CheckBoxTreeNodeProps(
          isNode = isNode,
          level = level,
          expanded = isOpened,
          onExpand = { () =>
            toggleState(self)(data)
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

      <.div(^.className := checkBoxTree)(
        createElements(props.roots, 0)
      )
    }
  )

  private def isOpen(state: CheckBoxTreeState, data: CheckBoxTreeData): Boolean = {
    state.opened.contains(data.key)
  }

  private def toggleState(self: Self[CheckBoxTreeProps, CheckBoxTreeState])(data: CheckBoxTreeData): Unit = {
    val currOpen = isOpen(self.state, data)
    val newState =
      if (currOpen) self.state.opened - data.key
      else self.state.opened + data.key

    self.setState(_.copy(opened = newState))
  }
}
