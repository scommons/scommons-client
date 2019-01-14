package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React
import org.scalatest.Assertion
import scommons.client.ui.TriState._
import scommons.client.ui._
import scommons.client.ui.tree.TreeCss._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class CheckBoxTreeSpec extends TestSpec with ShallowRendererUtils {

  it should "expand node when onExpand" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data))
    val renderer = createRenderer()
    renderer.render(<(CheckBoxTree())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val nodeProps = findComponentProps(comp, TreeNode)
    nodeProps.arrowClass shouldBe treeClosedArrow

    //when
    nodeProps.onExpand()

    //then
    val updatedComp = renderer.getRenderOutput()
    findComponentProps(updatedComp, TreeNode).arrowClass shouldBe treeOpenArrow
  }

  it should "collapse initially opened node when onExpand" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data), openNodes = Set(data.key))
    val renderer = createRenderer()
    renderer.render(<(CheckBoxTree())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val nodeProps = findComponentProps(comp, TreeNode)
    nodeProps.arrowClass shouldBe treeOpenArrow

    //when
    nodeProps.onExpand()

    //then
    val updatedComp = renderer.getRenderOutput()
    findComponentProps(updatedComp, TreeNode).arrowClass shouldBe treeClosedArrow
  }

  it should "collapse node when onExpand again" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data))
    val renderer = createRenderer()
    renderer.render(<(CheckBoxTree())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val nodeProps = findComponentProps(comp, TreeNode)
    nodeProps.arrowClass shouldBe treeClosedArrow
    nodeProps.onExpand()
    val nodePropsV2 = findComponentProps(renderer.getRenderOutput(), TreeNode)
    nodePropsV2.arrowClass shouldBe treeOpenArrow

    //when
    nodePropsV2.onExpand()

    //then
    val nodePropsV3 = findComponentProps(renderer.getRenderOutput(), TreeNode)
    nodePropsV3.arrowClass shouldBe treeClosedArrow
  }

  it should "call props.onChange when onChange on checkBox" in {
    //given
    val onChange = mockFunction[CheckBoxTreeData, TriState, Unit]
    val data = CheckBoxTreeItemData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data), onChange = onChange)
    val comp = shallowRender(<(CheckBoxTree())(^.wrapped := props)())
    val checkBoxWrapper = React.createClass[Unit, Unit] { _ =>
      findComponentProps(comp, TreeNode).renderValue()
    }
    val result = shallowRender(<(checkBoxWrapper).empty)
    val checkBoxProps = findComponentProps(result, ImageCheckBox)

    //then
    onChange.expects(data, Selected)
    
    //when
    checkBoxProps.onChange(Selected)
  }
  
  it should "render tree with deselected item" in {
    //given
    val data = CheckBoxTreeItemData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data))
    val comp = shallowRender(<(CheckBoxTree())(^.wrapped := props)())
    val checkBoxWrapper = React.createClass[Unit, Unit] { _ =>
      findComponentProps(comp, TreeNode).renderValue()
    }

    //when
    val result = shallowRender(<(checkBoxWrapper).empty)

    //then
    assertCheckBox(findComponentProps(result, ImageCheckBox), props, data)
  }
  
  it should "render read-only tree with selected item with image" in {
    //given
    val data = CheckBoxTreeItemData("test", Selected, "/test", Some(ButtonImagesCss.folder))
    val props = CheckBoxTreeProps(List(data), readOnly = true)
    val comp = shallowRender(<(CheckBoxTree())(^.wrapped := props)())
    val checkBoxWrapper = React.createClass[Unit, Unit] { _ =>
      findComponentProps(comp, TreeNode).renderValue()
    }

    //when
    val result = shallowRender(<(checkBoxWrapper).empty)

    //then
    assertCheckBox(findComponentProps(result, ImageCheckBox), props, data)
  }
  
  it should "render opened node" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data), openNodes = Set(data.key))
    val component = <(CheckBoxTree())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertTreeNode(findComponentProps(result, TreeNode), props, data)
  }

  it should "render opened node when componentWillReceiveProps" in {
    //given
    val node1 = CheckBoxTreeNodeData("node 1", Deselected, "/node-1")
    val prevProps = CheckBoxTreeProps(List(node1), openNodes = Set(node1.key))
    val renderer = createRenderer()
    renderer.render(<(CheckBoxTree())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    findComponentProps(comp, TreeNode).arrowClass shouldBe treeOpenArrow
    val node2 = CheckBoxTreeNodeData("node 2", Deselected, "/node-2")
    val props = CheckBoxTreeProps(List(node1, node2), openNodes = Set(node2.key))

    //when
    renderer.render(<(CheckBoxTree())(^.wrapped := props)())

    //then
    findProps(renderer.getRenderOutput(), TreeNode).map(_.arrowClass) shouldBe List(
      treeOpenArrow,
      treeOpenArrow
    )
  }

  it should "render closed node when componentWillReceiveProps" in {
    //given
    val node1 = CheckBoxTreeNodeData("node 1", Deselected, "/node-1")
    val prevProps = CheckBoxTreeProps(List(node1), openNodes = Set(node1.key))
    val renderer = createRenderer()
    renderer.render(<(CheckBoxTree())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    findComponentProps(comp, TreeNode).arrowClass shouldBe treeOpenArrow
    val node2 = CheckBoxTreeNodeData("node 2", Deselected, "/node-2")
    val props = CheckBoxTreeProps(List(node1, node2), closeNodes = Set(node1.key))

    //when
    renderer.render(<(CheckBoxTree())(^.wrapped := props)())

    //then
    findProps(renderer.getRenderOutput(), TreeNode).map(_.arrowClass) shouldBe List(
      treeClosedArrow,
      treeClosedArrow
    )
  }

  it should "not render opened node when it was removed" in {
    //given
    val renderer = createRenderer()
    val node1 = CheckBoxTreeNodeData("node 1", Deselected, "/node-1")
    val props = CheckBoxTreeProps(List(node1), openNodes = Set(node1.key))
    renderer.render(<(CheckBoxTree())(^.wrapped := props)())
    findComponentProps(renderer.getRenderOutput(), TreeNode).arrowClass shouldBe treeOpenArrow
    val node2 = CheckBoxTreeNodeData("node 2", Deselected, "/node-2")
    val propsV2 = CheckBoxTreeProps(List(node2), openNodes = Set(node2.key))
    renderer.render(<(CheckBoxTree())(^.wrapped := propsV2)())
    findComponentProps(renderer.getRenderOutput(), TreeNode).arrowClass shouldBe treeOpenArrow
    val propsV3 = CheckBoxTreeProps(List(node1, node2))

    //when
    renderer.render(<(CheckBoxTree())(^.wrapped := propsV3)())

    //then
    findProps(renderer.getRenderOutput(), TreeNode).map(_.arrowClass) shouldBe List(
      treeClosedArrow,
      treeOpenArrow
    )
  }

  it should "render opened child nodes" in {
    //given
    val topItem = CheckBoxTreeItemData("top item", Deselected, "/top-item")
    val childItem = CheckBoxTreeItemData("child item", Deselected, "/child-item")
    val childNode = CheckBoxTreeNodeData("child node", Deselected, "/child-node", children = List(childItem))
    val topNode = CheckBoxTreeNodeData("top node", Deselected, "/top-node", children = List(childNode))
    val props = CheckBoxTreeProps(List(topItem, topNode), openNodes = Set(topNode.key, childNode.key))
    val component = <(CheckBoxTree())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := TreeCss.tree)(), { case List(topItemE, topNodeE) =>
      assertComponent(topItemE, TreeNode) { topItemProps =>
        assertTreeNode(topItemProps, props, topItem)
      }
      assertComponent(topNodeE, TreeNode)({ topNodeProps =>
        assertTreeNode(topNodeProps, props, topNode)
      }, { case List(childNodeE) =>
        assertComponent(childNodeE, TreeNode)({ childNodeProps =>
          assertTreeNode(childNodeProps, props, childNode, level = 1)
        }, { case List(childItemE) =>
          assertComponent(childItemE, TreeNode) { childItemProps =>
            assertTreeNode(childItemProps, props, childItem, level = 2)
          }
        })
      })
    })
  }

  it should "not render closed child nodes" in {
    //given
    val topItem = CheckBoxTreeItemData("top item", Deselected, "/top-item")
    val childItem = CheckBoxTreeItemData("child item", Deselected, "/child-item")
    val childNode = CheckBoxTreeNodeData("child node", Deselected, "/child-node", children = List(childItem))
    val topNode = CheckBoxTreeNodeData("top node", Deselected, "/top-node", children = List(childNode))
    val props = CheckBoxTreeProps(List(topItem, topNode))
    val component = <(CheckBoxTree())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := TreeCss.tree)(), { case List(topItemE, topNodeE) =>
      assertComponent(topItemE, TreeNode) { topItemProps =>
        assertTreeNode(topItemProps, props, topItem)
      }
      assertComponent(topNodeE, TreeNode) { topNodeProps =>
        assertTreeNode(topNodeProps, props, topNode)
      }
    })
  }

  private def assertTreeNode(nodeProps: TreeNodeProps,
                             props: CheckBoxTreeProps,
                             data: CheckBoxTreeData,
                             level: Int = 0): Assertion = {
    
    val (expectedIsNode, expectedIsOpened) = data match {
      case _: CheckBoxTreeItemData => (false, false)
      case _: CheckBoxTreeNodeData => (true, props.openNodes.contains(data.key))
    }
    
    inside (nodeProps) {
      case TreeNodeProps(isNode, paddingLeft, itemClass, nodeClass, nodeIconClass, arrowClass, valueClass, _, _, _) =>
        isNode shouldBe expectedIsNode
        paddingLeft shouldBe (level * 16)
        itemClass shouldBe treeItem
        nodeClass shouldBe (if (expectedIsNode) treeNode else "")
        nodeIconClass shouldBe s"$treeItem $treeNodeIcon"
        arrowClass shouldBe (if (expectedIsOpened) treeOpenArrow else treeClosedArrow)
        valueClass shouldBe (if (expectedIsNode) treeNodeValue else treeItemValue)
    }
  }
  
  private def assertCheckBox(checkBoxProps: ImageCheckBoxProps,
                             props: CheckBoxTreeProps,
                             data: CheckBoxTreeData): Unit = {
    
    inside (checkBoxProps) {
      case ImageCheckBoxProps(resultValue, image, text, _, requestFocus, readOnly) =>
        resultValue shouldBe data.value
        image shouldBe data.image.getOrElse("")
        text shouldBe data.text
        requestFocus shouldBe false
        readOnly shouldBe props.readOnly
    }
  }
}
