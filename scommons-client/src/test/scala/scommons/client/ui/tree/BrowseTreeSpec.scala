package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.test.TestUtils._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.tree.BrowseTreeCss._

class BrowseTreeSpec extends TestSpec {

  it should "return false if item is not selected when isSelected" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState()

    //when & then
    BrowseTree.isSelected(state, data) shouldBe false
  }

  it should "return true if item is selected when isSelected" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState(Some(data.key))

    //when & then
    BrowseTree.isSelected(state, data) shouldBe true
  }

  it should "put selected item into state when setSelected" in {
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

  it should "replace existing selected item in state when setSelected" in {
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

  it should "return false if item is not expanded when isExpanded" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState()

    //when & then
    BrowseTree.isExpanded(state, data) shouldBe false
  }

  it should "return true if item is expanded when isExpanded" in {
    //given
    val data = BrowseTreeItemData("test")
    val state = BrowseTreeState(expanded = Set(data.key))

    //when & then
    BrowseTree.isExpanded(state, data) shouldBe true
  }

  it should "put expanded item into state when toggleExpanded" in {
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

  it should "remove expanded item from state when toggleExpanded" in {
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

  it should "render the component" in {
    //given
    val topItem = BrowseTreeItemData("top item")
    val childItem = BrowseTreeItemData("child item")
    val childNode = BrowseTreeNodeData("child node", List(childItem))
    val topNode = BrowseTreeNodeData("top node", List(childNode))
    val props = BrowseTreeProps(List(topItem, topNode))
    val component = <(BrowseTree())(^.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class={s"$browseTree"}>
        {renderAsXml(BrowseTreeNode(), BrowseTreeNodeProps(topItem))}
        {renderAsXml(BrowseTreeNode(), BrowseTreeNodeProps(topNode),
          <(BrowseTreeNode())(^.wrapped := BrowseTreeNodeProps(childNode, level = 1))(
            <(BrowseTreeNode())(^.wrapped := BrowseTreeNodeProps(childItem, level = 2))()
          )
        )}
      </div>
    )
  }
}
