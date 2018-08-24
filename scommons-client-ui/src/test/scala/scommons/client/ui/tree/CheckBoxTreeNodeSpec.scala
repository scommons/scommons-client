package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.React.{Props, Self}
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import io.github.shogowada.scalajs.reactjs.elements.ReactElement
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.test.TestSpec
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.util.TestDOMUtils._
import scommons.client.ui.tree.CheckBoxTreeCss._
import scommons.client.ui.tree.CheckBoxTreeNodeSpec.MouseSyntheticEventMock

import scala.scalajs.js.annotation.JSExportAll

class CheckBoxTreeNodeSpec extends TestSpec {

  it should "call onExpand when click on node arrow" in {
    //given
    val onExpand = mockFunction[Unit]
    val comp = renderIntoDocument(treeNode(nodeProps(isNode = true, 0, onExpand = onExpand)))
    val arrowDiv = findRenderedDOMComponentWithClass(comp, s"$checkBoxTreeNodeIcon")

    //then
    onExpand.expects()

    //when
    ReactTestUtils.Simulate.click(arrowDiv)
  }

  it should "call stopPropagation on click event" in {
    //given
    val onExpand = mockFunction[Unit]
    val props = nodeProps(isNode = true, 0, onExpand = onExpand)
    val self = mock[Self[CheckBoxTreeNodeProps, Unit]]
    val selfProps = mock[Props[CheckBoxTreeNodeProps]]
    val event = mock[MouseSyntheticEventMock]

    //then
    (event.stopPropagation _).expects()
    (self.props _).expects().returning(selfProps)
    (selfProps.wrapped _).expects().returning(props)
    onExpand.expects()

    //when
    CheckBoxTreeNode.arrowClick(self)(event.asInstanceOf[MouseSyntheticEvent])
  }

  it should "render item" in {
    //given
    val text = "Item"
    val props = nodeProps(isNode = false, 1, text = text)
    val component = treeNode(props)

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.div(^.className := s"$checkBoxTreeItem", ^.style := Map("paddingLeft" -> "16px"))(
        <.div(^.className := "")(
          <.div(^.className := checkBoxTreeItemValue)(
            <.label()(text)
          )
        )
      )
    )
  }

  it should "render non-empty node" in {
    //given
    val nodeText = "Node"
    val props = nodeProps(isNode = true, 1, nodeText)
    val component = treeNode(props, List(
      treeNode(nodeProps(isNode = false, 2, "Item"))
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.div()(
        <.div(^("class") := s"$checkBoxTreeItem", ^("style") := "padding-left: 16px;")(
          <.div(^("class") := s"$checkBoxTreeNode")(
            <.div(^("class") := s"$checkBoxTreeItem $checkBoxTreeNodeIcon")(
              <.div(^("class") := checkBoxTreeClosedArrow)()
            ),
            <.div(^("class") := checkBoxTreeNodeValue)(
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
    val component = treeNode(nodeProps(isNode = true, 1, nodeText, expanded = true), List(
      treeNode(nodeProps(isNode = false, 2, childText))
    ))

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.div()(
        <.div(^("class") := s"$checkBoxTreeItem", ^("style") := "padding-left: 16px;")(
          <.div(^("class") := s"$checkBoxTreeNode")(
            <.div(^("class") := s"$checkBoxTreeItem $checkBoxTreeNodeIcon")(
              <.div(^("class") := checkBoxTreeOpenArrow)()
            ),
            <.div(^("class") := checkBoxTreeNodeValue)(
              <.label()(nodeText)
            )
          )
        ),
        <.div(^("class") := s"$checkBoxTreeItem", ^("style") := "padding-left: 32px;")(
          <.div(^("class") := "")(
            <.div(^("class") := checkBoxTreeItemValue)(
              <.label()(childText)
            )
          )
        )
      )
    )
  }
  
  private def nodeProps(isNode: Boolean,
                        level: Int,
                        text: String = "Test",
                        expanded: Boolean = false,
                        onExpand: () => Unit = () => ()): CheckBoxTreeNodeProps = {


    CheckBoxTreeNodeProps(isNode, level, expanded, onExpand, { () =>
      <.label()(text)
    })
  }

  private def treeNode(props: CheckBoxTreeNodeProps,
                       children: List[ReactElement] = Nil): ReactElement = {

    <(CheckBoxTreeNode())(^.wrapped := props)(
      if (props.isNode && props.expanded) children
      else Nil
    )
  }
}

object CheckBoxTreeNodeSpec {

  @JSExportAll
  trait MouseSyntheticEventMock {

    def stopPropagation(): Unit
  }
}
