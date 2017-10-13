package scommons.client.browsetree

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.browsetree.BrowseTreeCss._
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._

import scala.scalajs.js.annotation.JSExportAll
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._

class BrowseTreeNodeSpec extends FlatSpec with Matchers with MockFactory {

  "itemClick" should "call onSelect when click on item div" in {
    //given
    val onSelect = mockFunction[BrowseTreeData, Unit]
    val data = BrowseTreeItemData("selected item")
    val comp = renderIntoDocument(treeNode(nodeProps(data, 0, selected = true, onSelect = onSelect)))
    val itemDiv = findRenderedDOMComponentWithClass(comp, s"$browseTreeItem $browseTreeSelectedItem")

    //then
    onSelect.expects(data)

    //when
    ReactTestUtils.Simulate.click(itemDiv)
  }

  "arrowClick" should "call onExpand when click on node arrow" in {
    //given
    val onExpand = mockFunction[BrowseTreeData, Unit]
    val data = BrowseTreeNodeData("top empty node", Nil)
    val comp = renderIntoDocument(treeNode(nodeProps(data, 0, onExpand = onExpand)))
    val arrowDiv = findRenderedDOMComponentWithClass(comp, s"$browseTreeNodeIcon")

    //then
    onExpand.expects(data)

    //when
    ReactTestUtils.Simulate.click(arrowDiv)
  }

  it should "call stopPropagation on click event" in {
    //given
    val onExpand = mockFunction[BrowseTreeData, Unit]
    val props = nodeProps(BrowseTreeNodeData("test"), 0, onExpand = onExpand)
    val self = mock[Self[BrowseTreeNodeProps, Unit]]
    val selfProps = mock[Props[BrowseTreeNodeProps]]
    val event = mock[MouseSyntheticEventMock]

    //then
    (event.stopPropagation _).expects()
    (self.props _).expects().returning(selfProps)
    (selfProps.wrapped _).expects().returning(props)
    onExpand.expects(props.data)

    //when
    BrowseTreeNode.arrowClick(self)(event.asInstanceOf[MouseSyntheticEvent])
  }

  "rendering" should "render top item" in {
    //given
    val data = BrowseTreeItemData("top item")
    val component = treeNode(nodeProps(data, 0))

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
    val component = treeNode(nodeProps(data, 1, selected = true))

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
    val component = treeNode(nodeProps(data, 0))

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
    val component = treeNode(nodeProps(data, 1), List(
      treeNode(nodeProps(child, 2))
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
    val component = treeNode(nodeProps(data, 1, expanded = true), List(
      treeNode(nodeProps(child, 2))
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

  private def nodeProps(data: BrowseTreeData,
                        level: Int,
                        selected: Boolean = false,
                        onSelect: BrowseTreeData => Unit = { _ => },
                        expanded: Boolean = false,
                        onExpand: BrowseTreeData => Unit = { _ => }): BrowseTreeNodeProps = {


    BrowseTreeNodeProps(data, level, selected, onSelect, expanded, onExpand)
  }

  private def treeNode(props: BrowseTreeNodeProps,
                       children: List[ReactElement] = Nil): ReactElement = {

    E(BrowseTreeNode.reactClass)(A.wrapped := props)(children)
  }
}

@JSExportAll
trait MouseSyntheticEventMock {

  def stopPropagation(): Unit
}
