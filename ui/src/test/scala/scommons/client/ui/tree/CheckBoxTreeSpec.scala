package scommons.client.ui.tree

import org.scalatest.Assertion
import scommons.client.ui.TriState._
import scommons.client.ui._
import scommons.client.ui.tree.CheckBoxTree._
import scommons.client.ui.tree.TreeCss._
import scommons.react._
import scommons.react.test._

class CheckBoxTreeSpec extends TestSpec with TestRendererUtils {

  CheckBoxTree.treeNodeComp = () => "TreeNode".asInstanceOf[ReactClass]

  it should "expand node when onExpand" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data))
    val renderer = createTestRenderer(<(CheckBoxTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(renderer.root, treeNodeComp)
    nodeProps.arrowClass shouldBe treeClosedArrow

    //when
    nodeProps.onExpand()

    //then
    findComponentProps(renderer.root, treeNodeComp).arrowClass shouldBe treeOpenArrow
  }

  it should "collapse initially opened node when onExpand" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data), openNodes = Set(data.key))
    val renderer = createTestRenderer(<(CheckBoxTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(renderer.root, treeNodeComp)
    nodeProps.arrowClass shouldBe treeOpenArrow

    //when
    nodeProps.onExpand()

    //then
    findComponentProps(renderer.root, treeNodeComp).arrowClass shouldBe treeClosedArrow
  }

  it should "collapse node when onExpand again" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data))
    val renderer = createTestRenderer(<(CheckBoxTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(renderer.root, treeNodeComp)
    nodeProps.arrowClass shouldBe treeClosedArrow
    nodeProps.onExpand()
    val nodePropsV2 = findComponentProps(renderer.root, treeNodeComp)
    nodePropsV2.arrowClass shouldBe treeOpenArrow

    //when
    nodePropsV2.onExpand()

    //then
    val nodePropsV3 = findComponentProps(renderer.root, treeNodeComp)
    nodePropsV3.arrowClass shouldBe treeClosedArrow
  }

  it should "call props.onChange when onChange on checkBox" in {
    //given
    val onChange = mockFunction[CheckBoxTreeData, TriState, Unit]
    val data = CheckBoxTreeItemData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data), onChange = onChange)
    val comp = testRender(<(CheckBoxTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(comp, treeNodeComp)
    val checkBoxWrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        nodeProps.renderValue()
      }
    }
    val result = testRender(<(checkBoxWrapper()).empty)
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
    val comp = testRender(<(CheckBoxTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(comp, treeNodeComp)
    val checkBoxWrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        nodeProps.renderValue()
      }
    }

    //when
    val result = testRender(<(checkBoxWrapper()).empty)

    //then
    assertCheckBox(findComponentProps(result, ImageCheckBox), props, data)
  }
  
  it should "render read-only tree with selected item with image" in {
    //given
    val data = CheckBoxTreeItemData("test", Selected, "/test", Some(ButtonImagesCss.folder))
    val props = CheckBoxTreeProps(List(data), readOnly = true)
    val comp = testRender(<(CheckBoxTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(comp, treeNodeComp)
    val checkBoxWrapper = new FunctionComponent[Unit] {
      protected def render(props: Props): ReactElement = {
        nodeProps.renderValue()
      }
    }

    //when
    val result = testRender(<(checkBoxWrapper()).empty)

    //then
    assertCheckBox(findComponentProps(result, ImageCheckBox), props, data)
  }
  
  it should "render opened node" in {
    //given
    val data = CheckBoxTreeNodeData("test", Deselected, "/test")
    val props = CheckBoxTreeProps(List(data), openNodes = Set(data.key))
    val component = <(CheckBoxTree())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertTreeNode(findComponentProps(result, treeNodeComp), props, data)
  }

  it should "render opened node when update" in {
    //given
    val node1 = CheckBoxTreeNodeData("node 1", Deselected, "/node-1")
    val prevProps = CheckBoxTreeProps(List(node1), openNodes = Set(node1.key))
    val renderer = createTestRenderer(<(CheckBoxTree())(^.wrapped := prevProps)())
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeOpenArrow) shouldBe 1
    val node2 = CheckBoxTreeNodeData("node 2", Deselected, "/node-2")
    val props = CheckBoxTreeProps(List(node1, node2), openNodes = Set(node2.key))

    //when
    TestRenderer.act { () =>
      renderer.update(<(CheckBoxTree())(^.wrapped := props)())
    }

    //then
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeOpenArrow) shouldBe 2
  }

  it should "render closed node when update" in {
    //given
    val node1 = CheckBoxTreeNodeData("node 1", Deselected, "/node-1")
    val prevProps = CheckBoxTreeProps(List(node1), openNodes = Set(node1.key))
    val renderer = createTestRenderer(<(CheckBoxTree())(^.wrapped := prevProps)())
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeOpenArrow) shouldBe 1
    val node2 = CheckBoxTreeNodeData("node 2", Deselected, "/node-2")
    val props = CheckBoxTreeProps(List(node1, node2), closeNodes = Set(node1.key))

    //when
    TestRenderer.act { () =>
      renderer.update(<(CheckBoxTree())(^.wrapped := props)())
    }

    //then
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeClosedArrow) shouldBe 2
  }

  it should "not render opened node when it was removed" in {
    //given
    val node1 = CheckBoxTreeNodeData("node 1", Deselected, "/node-1")
    val props = CheckBoxTreeProps(List(node1), openNodes = Set(node1.key))
    val renderer = createTestRenderer(<(CheckBoxTree())(^.wrapped := props)())
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeOpenArrow) shouldBe 1
    val node2 = CheckBoxTreeNodeData("node 2", Deselected, "/node-2")
    val propsV2 = CheckBoxTreeProps(List(node2), openNodes = Set(node2.key))
    TestRenderer.act { () =>
      renderer.update(<(CheckBoxTree())(^.wrapped := propsV2)())
    }
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeOpenArrow) shouldBe 1
    val propsV3 = CheckBoxTreeProps(List(node1, node2))

    //when
    TestRenderer.act { () =>
      renderer.update(<(CheckBoxTree())(^.wrapped := propsV3)())
    }

    //then
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeClosedArrow) shouldBe 1
    findProps(renderer.root, treeNodeComp).count(_.arrowClass == treeOpenArrow) shouldBe 1
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
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := TreeCss.tree)(), { case List(topItemE, topNodeE) =>
      assertTestComponent(topItemE, treeNodeComp) { topItemProps =>
        assertTreeNode(topItemProps, props, topItem)
      }
      assertTestComponent(topNodeE, treeNodeComp)({ topNodeProps =>
        assertTreeNode(topNodeProps, props, topNode)
      }, { case List(childNodeE) =>
        assertTestComponent(childNodeE, treeNodeComp)({ childNodeProps =>
          assertTreeNode(childNodeProps, props, childNode, level = 1)
        }, { case List(childItemE) =>
          assertTestComponent(childItemE, treeNodeComp) { childItemProps =>
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
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := TreeCss.tree)(), { case List(topItemE, topNodeE) =>
      assertTestComponent(topItemE, treeNodeComp) { topItemProps =>
        assertTreeNode(topItemProps, props, topItem)
      }
      assertTestComponent(topNodeE, treeNodeComp) { topNodeProps =>
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
