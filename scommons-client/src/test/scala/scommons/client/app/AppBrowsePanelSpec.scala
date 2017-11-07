package scommons.client.app

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.ui.tree.{BrowseTreeItemData, BrowseTreeNodeData, BrowseTreeProps}
import scommons.client.ui.{Buttons, ButtonsPanelProps}
import scommons.client.util.ActionsData

class AppBrowsePanelSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val b1 = Buttons.ADD
    val b2 = Buttons.REMOVE
    val childItem = BrowseTreeItemData("child item")
    val topNode = BrowseTreeNodeData("top node", List(childItem))
    val props = AppBrowsePanelProps(
      ButtonsPanelProps(List(b1, b2), ActionsData(Set(b1.command), _ => ()), group = false),
      BrowseTreeProps(List(topNode))
    )
    val component = E(AppBrowsePanel())(A.wrapped := props)(
      E.div()("Some child element")
    )

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class="row-fluid">
        <div class="span4">
          <div class="well sidebar-nav">
            <div class={AppBrowsePanelCss.sidebarBp}>
              <div class="btn-toolbar">
                <button class="btn">
                  <img class={s"${b1.image}"} src=""/>
                  <span style="padding-left: 3px; vertical-align: middle;">{b1.text}</span>
                </button>
                <button class="btn" disabled="">
                  <img class={s"${b2.disabledImage}"} src=""/>
                  <span style="padding-left: 3px; vertical-align: middle;">{b2.text}</span>
                </button>
              </div>
            </div>
            <div class={s"$browseTree"}>
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
                  <div class={s"$browseTreeItem"} style="padding-left: 16px;">
                    <div class={s"$browseTreeItem"}>
                      <div class={browseTreeItemValue}>{childItem.text}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="span8">
          <div>
            Some child element
          </div>
        </div>
      </div>
    )
  }
}
