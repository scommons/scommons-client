package scommons.client.ui.tree

import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import scommons.client.ui.tree.TreeNodeSpec.MouseSyntheticEventMock
import scommons.react._
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils

import scala.scalajs.js.annotation.JSExportAll

class TreeNodeSpec extends TestSpec with TestDOMUtils {

  it should "call onSelect when click on item div" in {
    //given
    val onSelect = mockFunction[Unit]
    val props = getTreeNodeProps(isNode = true, 0, onSelect = Some(onSelect))
    domRender(getTreeNode(props))
    val itemDiv = domContainer.querySelector(s".${props.itemClass}")

    //then
    onSelect.expects()

    //when
    fireDomEvent(Simulate.click(itemDiv))
  }

  it should "call onExpand when click on node arrow" in {
    //given
    val onExpand = mockFunction[Unit]
    val props = getTreeNodeProps(isNode = true, 0, onExpand = onExpand)
    domRender(getTreeNode(props))
    val arrowDiv = domContainer.querySelector(s".${props.nodeIconClass}")

    //then
    onExpand.expects()

    //when
    fireDomEvent(Simulate.click(arrowDiv))
  }

  it should "call stopPropagation on click event" in {
    //given
    val onExpand = mockFunction[Unit]
    val props = getTreeNodeProps(isNode = true, 0, onExpand = onExpand)
    val event = mock[MouseSyntheticEventMock]

    //then
    (event.stopPropagation _).expects()
    onExpand.expects()

    //when
    TreeNode.arrowClick(props)(event.asInstanceOf[MouseSyntheticEvent])
  }

  it should "render top item" in {
    //given
    val text = "top item"
    val props = getTreeNodeProps(isNode = false, 0, text = text)
    val component = getTreeNode(props)

    //when
    domRender(component)

    //then
    assertDOMElement(domContainer, <.div()(
      <.div(^("class") := props.itemClass)(
        <.div(^("class") := props.nodeClass)(
          <.div(^("class") := props.valueClass)(
            <.label()(text)
          )
        )
      )
    ))
  }

  it should "render empty node" in {
    //given
    val nodeText = "empty node"
    val props = getTreeNodeProps(isNode = true, 1, text = nodeText)
    val component = getTreeNode(props)

    //when
    domRender(component)

    //then
    assertDOMElement(domContainer, <.div()(
      <.div()(
        <.div(^("class") := props.itemClass, ^("style") := "padding-left: 1px;")(
          <.div(^("class") := props.nodeClass)(
            <.div(^("class") := props.nodeIconClass)(
              <.div(^("class") := props.arrowClass)()
            ),
            <.div(^("class") := props.valueClass)(
              <.label()(nodeText)
            )
          )
        )
      )
    ))
  }

  it should "render non-empty node" in {
    //given
    val childText = "child item"
    val nodeText = "non-empty node"
    val props = getTreeNodeProps(isNode = true, 2, text = nodeText)
    val component = getTreeNode(props, List(
      getTreeNode(getTreeNodeProps(isNode = false, 3, text = childText))
    ))

    //when
    domRender(component)

    //then
    assertDOMElement(domContainer, <.div()(
      <.div()(
        <.div(^("class") := props.itemClass, ^("style") := "padding-left: 2px;")(
          <.div(^("class") := props.nodeClass)(
            <.div(^("class") := props.nodeIconClass)(
              <.div(^("class") := props.arrowClass)()
            ),
            <.div(^("class") := props.valueClass)(
              <.label()(nodeText)
            )
          )
        ),
        <.div(^("class") := props.itemClass, ^("style") := "padding-left: 3px;")(
          <.div(^("class") := props.nodeClass)(
            <.div(^("class") := props.valueClass)(
              <.label()(childText)
            )
          )
        )
      )
    ))
  }

  private def getTreeNodeProps(isNode: Boolean,
                               paddingLeft: Int,
                               itemClass: String = "itemClass",
                               nodeClass: String = "nodeClass",
                               nodeIconClass: String = "nodeIconClass",
                               arrowClass: String = "arrowClass",
                               valueClass: String = "valueClass",
                               onSelect: Option[() => Unit] = None,
                               onExpand: () => Unit = () => (),
                               text: String = "Test"): TreeNodeProps = {


    TreeNodeProps(
      isNode,
      paddingLeft,
      itemClass,
      nodeClass,
      nodeIconClass,
      arrowClass,
      valueClass,
      onSelect,
      onExpand,
      { () =>
        <.label()(text)
      }
    )
  }

  private def getTreeNode(props: TreeNodeProps,
                          children: List[ReactElement] = Nil): ReactElement = {
    
    <(TreeNode())(^.wrapped := props)(
      children
    )
  }
}

object TreeNodeSpec {

  @JSExportAll
  trait MouseSyntheticEventMock {

    def stopPropagation(): Unit
  }
}
