package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.TestSpec
import scommons.client.test.TestUtils._
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.Buttons
import scommons.client.ui.tree.BrowseTreeCss._
import scommons.client.ui.tree.BrowseTreeNodeSpec.MouseSyntheticEventMock
import scommons.client.util.BrowsePath

import scala.scalajs.js.annotation.JSExportAll

class BrowseTreeNodeSpec extends TestSpec {

  it should "call onSelect when click on item div" in {
    //given
    val onSelect = mockFunction[BrowseTreeData, Unit]
    val data = BrowseTreeItemData("selected item", BrowsePath("/selected-item"))
    val comp = renderIntoDocument(treeNode(nodeProps(data, 0, selected = true, onSelect = onSelect)))
    val itemDiv = findRenderedDOMComponentWithClass(comp, s"$browseTreeItem $browseTreeSelectedItem")

    //then
    onSelect.expects(data)

    //when
    ReactTestUtils.Simulate.click(itemDiv)
  }

  it should "call onExpand when click on node arrow" in {
    //given
    val onExpand = mockFunction[BrowseTreeData, Unit]
    val data = BrowseTreeNodeData("top empty node", BrowsePath("/top-empty-node"))
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
    val props = nodeProps(BrowseTreeNodeData("test", BrowsePath("/test")), 0, onExpand = onExpand)
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

  it should "render top item" in {
    //given
    val data = BrowseTreeItemData("top item", BrowsePath("/top-item"))
    val component = treeNode(nodeProps(data, 0))

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div(^.className := s"$browseTreeItem $browseTreeTopItem")(
        <.div(^.className := s"$browseTreeItem $browseTreeTopItemImageValue")(
          <.div(^.className := browseTreeItemValue)(data.text)
        )
      )
    )
  }

  it should "render selected item" in {
    //given
    val data = BrowseTreeItemData("selected item", BrowsePath("/selected-item"))
    val component = treeNode(nodeProps(data, 1, selected = true))

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div(^.className := s"$browseTreeItem $browseTreeSelectedItem", ^.style := Map("paddingLeft" -> "16px"))(
        <.div(^.className := s"$browseTreeItem")(
          <.div(^.className := browseTreeItemValue)(data.text)
        )
      )
    )
  }

  it should "render item with image" in {
    //given
    val data = BrowseTreeItemData("item with image", BrowsePath("/item-with-image"), Some(Buttons.ADD.image))
    val component = treeNode(nodeProps(data, 1))

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div(^.className := s"$browseTreeItem", ^.style := Map("paddingLeft" -> "16px"))(
        <.div(^.className := s"$browseTreeItem")(
          <.div(^.className := browseTreeItemValue)(
            <.img(^.className := s"${data.image.get}", ^.src := "")(),
            <.span(^.style := Map("paddingLeft" -> "3px"))(data.text)
          )
        )
      )
    )
  }

  it should "render top empty node" in {
    //given
    val data = BrowseTreeNodeData("top empty node", BrowsePath("/top-empty-node"))
    val component = treeNode(nodeProps(data, 0))

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div()(
        <.div(^.className := s"$browseTreeItem $browseTreeTopItem")(
          <.div(^.className := s"$browseTreeItem $browseTreeNode $browseTreeTopItemImageValue")(
            <.div(^.className := s"$browseTreeNodeIcon")(
              <.div(^.className := browseTreeClosedArrow)()
            ),
            <.div(^.className := browseTreeNodeValue)(data.text)
          )
        ),
        <.div(^.style := Map("display" -> "none"))()
      )
    )
  }

  it should "render non-empty node" in {
    //given
    val child = BrowseTreeItemData("child item", BrowsePath("/child-item"))
    val data = BrowseTreeNodeData("non-empty node", BrowsePath("/non-empty-node"))
    val component = treeNode(nodeProps(data, 1), List(
      treeNode(nodeProps(child, 2))
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.div()(
        <.div(^("class") := s"$browseTreeItem", ^("style") := "padding-left: 16px;")(
          <.div(^("class") := s"$browseTreeItem $browseTreeNode")(
            <.div(^("class") := s"$browseTreeNodeIcon")(
              <.div(^("class") := browseTreeClosedArrow)()
            ),
            <.div(^("class") := browseTreeNodeValue)(data.text)
          )
        ),
        <.div(^("style") := "display: none;")(
          <.div(^("class") := s"$browseTreeItem", ^("style") := "padding-left: 32px;")(
            <.div(^("class") := s"$browseTreeItem")(
              <.div(^("class") := browseTreeItemValue)(child.text)
            )
          )
        )
      )
    )
  }

  it should "render expanded non-empty node" in {
    //given
    val child = BrowseTreeItemData("child item", BrowsePath("/child-item"))
    val data = BrowseTreeNodeData("expanded non-empty node", BrowsePath("/expanded-non-empty-node"))
    val component = treeNode(nodeProps(data, 1, expanded = true), List(
      treeNode(nodeProps(child, 2))
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.div()(
        <.div(^("class") := s"$browseTreeItem", ^("style") := "padding-left: 16px;")(
          <.div(^("class") := s"$browseTreeItem $browseTreeNode")(
            <.div(^("class") := s"$browseTreeNodeIcon")(
              <.div(^("class") := browseTreeOpenArrow)()
            ),
            <.div(^("class") := browseTreeNodeValue)(data.text)
          )
        ),
        <.div()(
          <.div(^("class") := s"$browseTreeItem", ^("style") := "padding-left: 32px;")(
            <.div(^("class") := s"$browseTreeItem")(
              <.div(^("class") := browseTreeItemValue)(child.text)
            )
          )
        )
      )
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

    <(BrowseTreeNode())(^.wrapped := props)(children)
  }
}

object BrowseTreeNodeSpec {

  @JSExportAll
  trait MouseSyntheticEventMock {

    def stopPropagation(): Unit
  }
}
