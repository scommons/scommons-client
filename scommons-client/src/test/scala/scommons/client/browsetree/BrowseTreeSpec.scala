package scommons.client.browsetree

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.browsetree.BrowseTreeCss._
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

class BrowseTreeSpec extends FlatSpec with Matchers with MockFactory {

  "isSelected" should "return false if item is not selected" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState()

    //when & then
    BrowseTree.isSelected(state, data) shouldBe false
  }

  it should "return true if item is selected" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState(Some(data.key))

    //when & then
    BrowseTree.isSelected(state, data) shouldBe true
  }

  "setSelected" should "put selected item into state" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState()
    val newState = BrowseTreeState(Some(data.key))
    val setState = mockFunction[BrowseTreeState => BrowseTreeState, Unit]
    setState.expects(where { (f: BrowseTreeState => BrowseTreeState) => f(state) == newState })
    BrowseTree.isSelected(state, data) shouldBe false
    BrowseTree.isSelected(newState, data) shouldBe true

    //when & then
    BrowseTree.setSelected(setState)(data)
  }

  it should "replace existing selected item in state" in {
    //given
    val data = BrowseTreeItemData("new item")
    val state = BrowseTreeState(Some(BrowseTreeItemData("current item").key))
    val newState = BrowseTreeState(Some(data.key))
    val setState = mockFunction[BrowseTreeState => BrowseTreeState, Unit]
    setState.expects(where { (f: BrowseTreeState => BrowseTreeState) => f(state) == newState })
    BrowseTree.isSelected(state, data) shouldBe false
    BrowseTree.isSelected(newState, data) shouldBe true

    //when & then
    BrowseTree.setSelected(setState)(data)
  }

  "isExpanded" should "return false if item is not expanded" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState()

    //when & then
    BrowseTree.isExpanded(state, data) shouldBe false
  }

  it should "return true if item is expanded" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState(expanded = Set(data.key))

    //when & then
    BrowseTree.isExpanded(state, data) shouldBe true
  }

  "toggleExpanded" should "put expanded item into state" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState()
    val newState = BrowseTreeState(expanded = Set(data.key))
    val setState = mockFunction[BrowseTreeState => BrowseTreeState, Unit]
    setState.expects(where { (f: BrowseTreeState => BrowseTreeState) => f(state) == newState })
    BrowseTree.isExpanded(state, data) shouldBe false
    BrowseTree.isExpanded(newState, data) shouldBe true

    //when & then
    BrowseTree.toggleExpanded(setState)(data)
  }

  it should "remove expanded item from state" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState(expanded = Set(data.key))
    val newState = BrowseTreeState()
    val setState = mockFunction[BrowseTreeState => BrowseTreeState, Unit]
    setState.expects(where { (f: BrowseTreeState => BrowseTreeState) => f(state) == newState })
    BrowseTree.isExpanded(state, data) shouldBe true
    BrowseTree.isExpanded(newState, data) shouldBe false

    //when & then
    BrowseTree.toggleExpanded(setState)(data)
  }

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
