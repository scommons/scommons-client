package scommons.client.browsetree

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.browsetree.BrowseTreeCss._
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

class BrowseTreeSpec extends FlatSpec with Matchers {

  "rendering" should "render the component" in {
    //given
    val topItem = BrowseTreeItemData("top item")
    val childItem = BrowseTreeItemData("child item")
    val childNode = BrowseTreeNodeData("child node", List(childItem))
    val topNode = BrowseTreeNodeData("top node", List(childNode))
    val props = BrowseTreeProps(List(topItem, topNode))
    val component = E(BrowseTree.reactClass)(A.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class={s"$browseTree"}>
        <div class={s"$browseTreeItem $browseTreeTopItem"}>
          <div class={s"$browseTreeItem $browseTreeTopItemImageValue"}>
            <div class={browseTreeItemValue}>{topItem.text}</div>
          </div>
        </div>
        <div>
          <div class={s"$browseTreeItem $browseTreeTopItem"}>
            <div class={s"$browseTreeItem $browseTreeNode $browseTreeTopItemImageValue"}>
              <div class={s"$browseTreeNodeIcon"}>
                <div class={browseTreeClosedArrow}/>
              </div>
              <div class={browseTreeItemValue}>{topNode.text}</div>
            </div>
          </div>
          <div style="display: none;">
            <div>
              <div class={s"$browseTreeItem"} style="padding-left: 16px;">
                <div class={s"$browseTreeItem $browseTreeNode"}>
                  <div class={s"$browseTreeNodeIcon"}>
                    <div class={browseTreeClosedArrow}/>
                  </div>
                  <div class={browseTreeItemValue}>{childNode.text}</div>
                </div>
              </div>
              <div style="display: none;">
                <div class={s"$browseTreeItem"} style="padding-left: 32px;">
                  <div class={s"$browseTreeItem"}>
                    <div class={browseTreeItemValue}>{childItem.text}</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    )
  }
}
