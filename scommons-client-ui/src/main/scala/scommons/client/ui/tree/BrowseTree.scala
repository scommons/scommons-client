package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.React.Self
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.ui.tree.TreeCss._
import scommons.client.ui.{ImageLabelWrapper, UiComponent}
import scommons.client.util.BrowsePath

case class BrowseTreeProps(roots: List[BrowseTreeData],
                           selectedItem: Option[BrowsePath] = None,
                           onSelect: BrowseTreeData => Unit = _ => (),
                           openedNodes: Set[BrowsePath] = Set.empty,
                           initiallyOpenedNodes: Set[BrowsePath] = Set.empty)

object BrowseTree extends UiComponent[BrowseTreeProps] {

  private case class BrowseTreeState(opened: Set[BrowsePath])

  def apply(): ReactClass = reactClass
  lazy val reactClass: ReactClass = createComp
  
  private def createComp = React.createClass[PropsType, BrowseTreeState](
    getInitialState = { self =>
      val props = self.props.wrapped
      BrowseTreeState(props.initiallyOpenedNodes ++ props.openedNodes)
    },
    componentWillReceiveProps = { (self, nextProps) =>
      val props = nextProps.wrapped
      if (self.props.wrapped != props) {
        val currKeys = BrowseTreeData.flattenNodes(props.roots).map(_.path).toSet
        self.setState(s => s.copy(opened = (s.opened ++ props.openedNodes).intersect(currKeys)))
      }
    },
    render = { self =>
      val props = self.props.wrapped

      def createElements(items: List[BrowseTreeData], level: Int): List[ReactElement] = items.map { data =>
        val (isNode, isOpened, children) = data match {
          case _: BrowseTreeItemData => (false, false, Nil)
          case n: BrowseTreeNodeData => (true, isOpen(self.state, data), n.children)
        }

        val selected = props.selectedItem.contains(data.path)
        val topItemClass = if (level == 0) browseTreeTopItem else ""
        val topItemImageClass = if (level == 0) browseTreeTopItemImageValue else ""
        val selectedClass = if (selected) browseTreeSelectedItem else ""
        val nodeClass = if (isNode) treeNode else ""

        <(TreeNode())(^.wrapped := TreeNodeProps(
          isNode = isNode,
          paddingLeft = level * 16,
          itemClass = s"$treeItem $selectedClass $topItemClass",
          nodeClass = s"$treeItem $nodeClass $topItemImageClass",
          nodeIconClass = s"$treeItem $treeNodeIcon",
          arrowClass = if (isOpened) browseTreeOpenArrow else browseTreeClosedArrow,
          valueClass = if (isNode) treeNodeValue else treeItemValue,
          onSelect = Some({ () =>
            if (!selected) {
              props.onSelect(data)
            }
          }),
          onExpand = { () =>
            toggleState(self, data)
          },
          renderValue = { () =>
            <.div()(
              data.image match {
                case None => data.text
                case Some(image) => ImageLabelWrapper(image, Some(data.text), alignText = false)
              }
            )
          }
        ))(
          if (isNode && isOpened) createElements(children, level + 1)
          else Nil
        )
      }

      <.div(^.className := TreeCss.tree)(
        createElements(props.roots, 0)
      )
    }
  )

  private def isOpen(state: BrowseTreeState, data: BrowseTreeData): Boolean = {
    state.opened.contains(data.path)
  }

  private def toggleState(self: Self[BrowseTreeProps, BrowseTreeState], data: BrowseTreeData): Unit = {
    val currOpen = isOpen(self.state, data)
    val newState =
      if (currOpen) self.state.opened - data.path
      else self.state.opened + data.path

    self.setState(_.copy(opened = newState))
  }
}
