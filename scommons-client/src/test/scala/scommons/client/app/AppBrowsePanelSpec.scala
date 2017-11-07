package scommons.client.app

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.tree.{BrowseTree, BrowseTreeItemData, BrowseTreeNodeData, BrowseTreeProps}
import scommons.client.ui.{Buttons, ButtonsPanel, ButtonsPanelProps}
import scommons.client.util.ActionsData

class AppBrowsePanelSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val b1 = Buttons.ADD
    val b2 = Buttons.REMOVE
    val buttonsPanelProps = ButtonsPanelProps(List(b1, b2), ActionsData(Set(b1.command), _ => ()), group = false)

    val childItem = BrowseTreeItemData("child item")
    val topNode = BrowseTreeNodeData("top node", List(childItem))
    val browseTreeProps = BrowseTreeProps(List(topNode))

    val props = AppBrowsePanelProps(buttonsPanelProps, browseTreeProps)
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
              {renderAsXml(ButtonsPanel(), buttonsPanelProps)}
            </div>
            {renderAsXml(BrowseTree(), browseTreeProps)}
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
