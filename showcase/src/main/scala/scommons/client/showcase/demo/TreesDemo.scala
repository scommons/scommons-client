package scommons.client.showcase.demo

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.classes.ReactClass
import scommons.client.ui._
import scommons.client.ui.tree._

case class TreesState(checkboxValue: TriState,
                      roots: List[CheckBoxTreeData])

object TreesDemo {

  def apply(): ReactClass = reactClass

  private lazy val reactClass = React.createClass[Unit, TreesState](
    getInitialState = { _ =>
      val item1 = CheckBoxTreeItemData("i1", TriState.Deselected, "Item 1")
      val item2 = CheckBoxTreeItemData("i2", TriState.Deselected, "Item 2")
      val item3 = CheckBoxTreeItemData("i3", TriState.Selected, "Item 3")
      val node1 = CheckBoxTreeNodeData("n1", TriState.Selected, "Node 1", Some(ButtonImagesCss.folder))
      val node2 = CheckBoxTreeNodeData("n2", TriState.Selected, "Node 2", Some(ButtonImagesCss.folder),
        children = List(item3)
      )
      val node3 = CheckBoxTreeNodeData("n3", TriState.Indeterminate, "Node 3", Some(ButtonImagesCss.folder),
        children = List(item2, node2)
      )
      val roots = List(node1, node3, item1)
      
      TreesState(TriState.Indeterminate, roots)
    },
    render = { self =>
      <.div()(
        <.h2()("CheckBoxTree"),
        <.p()("Tree component with checkboxes in nodes/leafs."),
        <.p()(
          <(CheckBoxTree())(^.wrapped := CheckBoxTreeProps(
            roots = self.state.roots,
            onChange = { (data, value) =>
              self.setState(s => s.copy(roots = updateCheckBoxTree(s.roots, data, value)))
            },
            openNodes = Set("n2", "n3")
          ))()
        ),
        <.h3()("Read-only CheckBoxTree"),
        <.p()("Pre-filled Tree with read-only checkboxes in nodes/leafs."),
        <.p()(
          <(CheckBoxTree())(^.wrapped := CheckBoxTreeProps(
            roots = self.state.roots,
            onChange = { (data, value) =>
              self.setState(s => s.copy(roots = updateCheckBoxTree(s.roots, data, value)))
            },
            openNodes = Set("n2", "n3"),
            readOnly = true
          ))()
        )
      )
    }
  )

  private def updateCheckBoxTree(roots: List[CheckBoxTreeData],
                                 data: CheckBoxTreeData,
                                 value: TriState): List[CheckBoxTreeData] = {

    def loop(list: List[CheckBoxTreeData], setAll: Boolean): List[CheckBoxTreeData] = {
      list.map {
        case d: CheckBoxTreeItemData if setAll || d.key == data.key => d.copy(
          value = value
        )
        case d: CheckBoxTreeNodeData if setAll || d.key == data.key => d.copy(
          value = value,
          children = loop(d.children, setAll = true)
        )
        case d: CheckBoxTreeNodeData =>
          val children = loop(d.children, setAll)
          d.copy(value = CheckBoxTreeData.calcNodeValue(children, d.value), children = children)
        case d => d
      }
    }

    loop(roots, setAll = false)
  }
}
