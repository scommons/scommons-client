package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.browsetree.BrowseTreeCss._
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

class BrowseTreeNodeSpec extends FlatSpec with Matchers {

  it should "render top item" in {
    //given
    val data = BrowseTreeItemData("top item")
    val component = treeNode(data, 0)

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class={s"$browseTreeItem $browseTreeTopItem"}>
        <div class={s"$browseTreeItem $browseTreeTopItemImageValue"}>
          <div class={browseTreeItemValue}>{data.text}</div>
        </div>
      </div>
    )
  }

  it should "render selected item" in {
    //given
    val data = BrowseTreeItemData("selected item")
    val component = treeNode(data, 1, selected = true)

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class={s"$browseTreeItem $browseTreeSelectedItem"} style="padding-left: 16px;">
        <div class={s"$browseTreeItem"}>
          <div class={browseTreeItemValue}>{data.text}</div>
        </div>
      </div>
    )
  }

  it should "render top empty node" in {
    //given
    val data = BrowseTreeNodeData("top empty node", Nil)
    val component = treeNode(data, 0)

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div>
        <div class={s"$browseTreeItem $browseTreeTopItem"}>
          <div class={s"$browseTreeItem $browseTreeNode $browseTreeTopItemImageValue"}>
            <div class={s"$browseTreeNodeIcon"}>
              <div class={browseTreeClosedArrow}/>
            </div>
            <div class={browseTreeItemValue}>{data.text}</div>
          </div>
        </div>
        <div style="display: none;"/>
      </div>
    )
  }

  it should "render non-empty node" in {
    //given
    val child = BrowseTreeItemData("child item")
    val data = BrowseTreeNodeData("non-empty node")
    val component = treeNode(data, 1, children = List(
      treeNode(child, 2)
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div>
        <div class={s"$browseTreeItem"} style="padding-left: 16px;">
          <div class={s"$browseTreeItem $browseTreeNode"}>
            <div class={s"$browseTreeNodeIcon"}>
              <div class={browseTreeClosedArrow}/>
            </div>
            <div class={browseTreeItemValue}>{data.text}</div>
          </div>
        </div>
        <div style="display: none;">
          <div class={s"$browseTreeItem"} style="padding-left: 32px;">
            <div class={s"$browseTreeItem"}>
              <div class={browseTreeItemValue}>{child.text}</div>
            </div>
          </div>
        </div>
      </div>
    )
  }

  it should "render expanded non-empty node" in {
    //given
    val child = BrowseTreeItemData("child item")
    val data = BrowseTreeNodeData("expanded non-empty node")
    val component = treeNode(data, 1, expanded = true, children = List(
      treeNode(child, 2)
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div>
        <div class={s"$browseTreeItem"} style="padding-left: 16px;">
          <div class={s"$browseTreeItem $browseTreeNode"}>
            <div class={s"$browseTreeNodeIcon"}>
              <div class={browseTreeOpenArrow}/>
            </div>
            <div class={browseTreeItemValue}>{data.text}</div>
          </div>
        </div>
        <div>
          <div class={s"$browseTreeItem"} style="padding-left: 32px;">
            <div class={s"$browseTreeItem"}>
              <div class={browseTreeItemValue}>{child.text}</div>
            </div>
          </div>
        </div>
      </div>
    )
  }

  private def treeNode(data: BrowseTreeData,
                       level: Int,
                       selected: Boolean = false,
                       onSelect: BrowseTreeData => Unit = { _ => },
                       expanded: Boolean = false,
                       onExpand: BrowseTreeData => Unit = { _ => },
                       children: List[ReactElement] = Nil): ReactElement = {


    val props = BrowseTreeNodeProps(data, level, selected, onSelect, expanded, onExpand)

    E(BrowseTreeNode.reactClass)(A.wrapped := props)(children)
  }
}
