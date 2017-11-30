package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec
import scommons.client.ui.tree.BrowseTreeCss._

class BrowseTreeSpec extends TestSpec {

  it should "call onSelect function when select new item" in {
    //given
    val onSelect = mockFunction[BrowseTreeData, Unit]
    val data = BrowseTreeItemData("test")
    val props = BrowseTreeProps(List(data), onSelect = onSelect)
    val comp = shallowRender(<(BrowseTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(comp, BrowseTreeNode)

    //then
    onSelect.expects(data)

    //when
    nodeProps.onSelect(data)
  }

  it should "not call onSelect function when select the same item" in {
    //given
    val onSelect = mockFunction[BrowseTreeData, Unit]
    val data = BrowseTreeItemData("test")
    val props = BrowseTreeProps(List(data), selectedItem = Some(data.key), onSelect = onSelect)
    val comp = shallowRender(<(BrowseTree())(^.wrapped := props)())
    val nodeProps = findComponentProps(comp, BrowseTreeNode)
    nodeProps.selected shouldBe true

    //then
    onSelect.expects(_: BrowseTreeData).never()

    //when
    nodeProps.onSelect(data)
  }

  it should "expand node when onExpand" in {
    //given
    val data = BrowseTreeItemData("test")
    val props = BrowseTreeProps(List(data))
    val renderer = createRenderer()
    renderer.render(<(BrowseTree())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val nodeProps = findComponentProps(comp, BrowseTreeNode)
    nodeProps.expanded shouldBe false

    //when
    nodeProps.onExpand(data)

    //then
    val updatedComp = renderer.getRenderOutput()
    findComponentProps(updatedComp, BrowseTreeNode).expanded shouldBe true
  }

  it should "collapse node when onExpand again" in {
    //given
    val data = BrowseTreeItemData("test")
    val props = BrowseTreeProps(List(data))
    val renderer = createRenderer()
    renderer.render(<(BrowseTree())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val nodeProps = findComponentProps(comp, BrowseTreeNode)
    nodeProps.expanded shouldBe false
    nodeProps.onExpand(data)
    val nodePropsV2 = findComponentProps(renderer.getRenderOutput(), BrowseTreeNode)
    nodePropsV2.expanded shouldBe true

    //when
    nodePropsV2.onExpand(data)

    //then
    val nodePropsV3 = findComponentProps(renderer.getRenderOutput(), BrowseTreeNode)
    nodePropsV3.expanded shouldBe false
  }

  it should "render selected node" in {
    //given
    val data = BrowseTreeItemData("test")
    val props = BrowseTreeProps(List(data), selectedItem = Some(data.key))
    val component = <(BrowseTree())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    findComponentProps(result, BrowseTreeNode).selected shouldBe true
  }

  it should "render child nodes" in {
    //given
    val topItem = BrowseTreeItemData("top item")
    val childItem = BrowseTreeItemData("child item")
    val childNode = BrowseTreeNodeData("child node", List(childItem))
    val topNode = BrowseTreeNodeData("top node", List(childNode))
    val props = BrowseTreeProps(List(topItem, topNode))
    val component = <(BrowseTree())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, <.div(^.className := browseTree)(), { case List(topItemE, topNodeE) =>
      assertComponent(topItemE, BrowseTreeNode(), { topItemProps: BrowseTreeNodeProps =>
        inside(topItemProps) { case BrowseTreeNodeProps(data, level, selected, _, expanded, _) =>
          data shouldBe topItem
          level shouldBe 0
          selected shouldBe false
          expanded shouldBe false
        }
      })
      assertComponent(topNodeE, BrowseTreeNode(), { topNodeProps: BrowseTreeNodeProps =>
        inside(topNodeProps) { case BrowseTreeNodeProps(data, level, selected, _, expanded, _) =>
          data shouldBe topNode
          level shouldBe 0
          selected shouldBe false
          expanded shouldBe false
        }
      }, { case List(childNodeE) =>
        assertComponent(childNodeE, BrowseTreeNode(), { childNodeProps: BrowseTreeNodeProps =>
          inside(childNodeProps) { case BrowseTreeNodeProps(data, level, selected, _, expanded, _) =>
            data shouldBe childNode
            level shouldBe 1
            selected shouldBe false
            expanded shouldBe false
          }
        }, { case List(childItemE) =>
          assertComponent(childItemE, BrowseTreeNode(), { childItemProps: BrowseTreeNodeProps =>
            inside(childItemProps) { case BrowseTreeNodeProps(data, level, selected, _, expanded, _) =>
              data shouldBe childItem
              level shouldBe 2
              selected shouldBe false
              expanded shouldBe false
            }
          })
        })
      })
    })
  }
}
