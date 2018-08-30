package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.test.TestSpec
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.util.TestDOMUtils._
import scommons.client.ui.tree.TreeCss._
import scommons.client.ui.tree.TreeNodeSpec.MouseSyntheticEventMock

import scala.scalajs.js.annotation.JSExportAll

class TreeNodeSpec extends TestSpec {

  it should "call onExpand when click on node arrow" in {
    //given
    val onExpand = mockFunction[Unit]
    val comp = renderIntoDocument(getTreeNode(getTreeNodeProps(isNode = true, 0, onExpand = onExpand)))
    val arrowDiv = findRenderedDOMComponentWithClass(comp, s"$treeNodeIcon")

    //then
    onExpand.expects()

    //when
    ReactTestUtils.Simulate.click(arrowDiv)
  }

  it should "call stopPropagation on click event" in {
    //given
    val onExpand = mockFunction[Unit]
    val props = getTreeNodeProps(isNode = true, 0, onExpand = onExpand)
    val self = mock[Self[TreeNodeProps, Unit]]
    val selfProps = mock[Props[TreeNodeProps]]
    val event = mock[MouseSyntheticEventMock]

    //then
    (event.stopPropagation _).expects()
    (self.props _).expects().returning(selfProps)
    (selfProps.wrapped _).expects().returning(props)
    onExpand.expects()

    //when
    TreeNode.arrowClick(self)(event.asInstanceOf[MouseSyntheticEvent])
  }

  it should "render item" in {
    //given
    val text = "Item"
    val props = getTreeNodeProps(isNode = false, 1, text = text)
    val component = getTreeNode(props)

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div(^.className := s"$treeItem", ^.style := Map("paddingLeft" -> "16px"))(
        <.div(^.className := "")(
          <.div(^.className := treeItemValue)(
            <.label()(text)
          )
        )
      )
    )
  }

  it should "render non-empty node" in {
    //given
    val nodeText = "Node"
    val props = getTreeNodeProps(isNode = true, 1, nodeText)
    val component = getTreeNode(props, List(
      getTreeNode(getTreeNodeProps(isNode = false, 2, "Item"))
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.div()(
        <.div(^("class") := s"$treeItem", ^("style") := "padding-left: 16px;")(
          <.div(^("class") := s"$treeNode")(
            <.div(^("class") := s"$treeItem $treeNodeIcon")(
              <.div(^("class") := treeClosedArrow)()
            ),
            <.div(^("class") := treeNodeValue)(
              <.label()(nodeText)
            )
          )
        )
      )
    )
  }

  it should "render expanded non-empty node" in {
    //given
    val childText = "child item"
    val nodeText = "expanded non-empty node"
    val component = getTreeNode(getTreeNodeProps(isNode = true, 1, nodeText, expanded = true), List(
      getTreeNode(getTreeNodeProps(isNode = false, 2, childText))
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.div()(
        <.div(^("class") := s"$treeItem", ^("style") := "padding-left: 16px;")(
          <.div(^("class") := s"$treeNode")(
            <.div(^("class") := s"$treeItem $treeNodeIcon")(
              <.div(^("class") := treeOpenArrow)()
            ),
            <.div(^("class") := treeNodeValue)(
              <.label()(nodeText)
            )
          )
        ),
        <.div(^("class") := s"$treeItem", ^("style") := "padding-left: 32px;")(
          <.div(^("class") := "")(
            <.div(^("class") := treeItemValue)(
              <.label()(childText)
            )
          )
        )
      )
    )
  }

  private def getTreeNodeProps(isNode: Boolean,
                               level: Int,
                               text: String = "Test",
                               expanded: Boolean = false,
                               onExpand: () => Unit = () => ()): TreeNodeProps = {


    TreeNodeProps(isNode, level, expanded, onExpand, { () =>
      <.label()(text)
    })
  }

  private def getTreeNode(props: TreeNodeProps,
                          children: List[ReactElement] = Nil): ReactElement = {

    <(TreeNode())(^.wrapped := props)(
      if (props.isNode && props.expanded) children
      else Nil
    )
  }
}

object TreeNodeSpec {

  @JSExportAll
  trait MouseSyntheticEventMock {

    def stopPropagation(): Unit
  }
}
