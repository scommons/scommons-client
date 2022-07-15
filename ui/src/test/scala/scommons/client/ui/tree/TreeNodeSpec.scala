package scommons.client.ui.tree

import scommons.react._
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class TreeNodeSpec extends TestSpec with TestRendererUtils {

  it should "call onSelect when click on item div" in {
    //given
    val onSelect = mockFunction[Unit]
    val props = getTreeNodeProps(isNode = true, 0, onSelect = Some(onSelect))
    val comp = testRender(getTreeNode(props))
    val itemDiv = inside(findComponents(comp, <.div.name).filter(
      _.props.className.asInstanceOf[js.UndefOr[String]].contains(props.itemClass)
    )) {
      case List(div) => div
    }

    //then
    onSelect.expects()

    //when
    itemDiv.props.onClick(null)
  }

  it should "call onExpand when click on node arrow" in {
    //given
    val onExpand = mockFunction[Unit]
    val props = getTreeNodeProps(isNode = true, 0, onExpand = onExpand)
    val comp = testRender(getTreeNode(props))
    val arrowDiv = inside(findComponents(comp, <.div.name).filter(
      _.props.className.asInstanceOf[js.UndefOr[String]].contains(props.nodeIconClass)
    )) {
      case List(div) => div
    }
    val stopPropagationMock = mockFunction[Unit]
    val event = literal("stopPropagation" -> stopPropagationMock)

    //then
    stopPropagationMock.expects()
    onExpand.expects()

    //when
    arrowDiv.props.onClick(event.asInstanceOf[js.Any])
  }

  it should "render top item" in {
    //given
    val text = "top item"
    val props = getTreeNodeProps(isNode = false, 0, text = text)
    val component = getTreeNode(props)

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result,
      <.div(^.className := props.itemClass)(
        <.div(^.className := props.nodeClass)(
          <.div(^.className := props.valueClass)(
            <.label()(text)
          )
        )
      )
    )
  }

  it should "render empty node" in {
    //given
    val nodeText = "empty node"
    val props = getTreeNodeProps(isNode = true, 1, text = nodeText)
    val component = getTreeNode(props)

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result,
      <.div()(
        <.div(^.className := props.itemClass, ^.style := Map("paddingLeft" -> "1px"))(
          <.div(^.className := props.nodeClass)(
            <.div(^.className := props.nodeIconClass)(
              <.div(^.className := props.arrowClass)()
            ),
            <.div(^.className := props.valueClass)(
              <.label()(nodeText)
            )
          )
        )
      )
    )
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
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div()(), inside(_) { case List(item1, item2) =>
      assertNativeComponent(item1,
        <.div(^.className := props.itemClass, ^.style := Map("paddingLeft" -> "2px"))(
          <.div(^.className := props.nodeClass)(
            <.div(^.className := props.nodeIconClass)(
              <.div(^.className := props.arrowClass)()
            ),
            <.div(^.className := props.valueClass)(
              <.label()(nodeText)
            )
          )
        )
      )
      assertTestComponent(item2, TreeNode) { case TreeNodeProps(
        isNode,
        paddingLeft,
        itemClass,
        nodeClass,
        nodeIconClass,
        arrowClass,
        valueClass,
        _,
        _,
        renderValue
      ) =>
        isNode shouldBe false
        paddingLeft shouldBe 3
        itemClass shouldBe "itemClass"
        nodeClass shouldBe "nodeClass"
        nodeIconClass shouldBe "nodeIconClass"
        arrowClass shouldBe "arrowClass"
        valueClass shouldBe "valueClass"

        assertNativeComponent(createTestRenderer(renderValue()).root,
          <.label()(childText)
        )
      }
//        <.div(^.className := props.itemClass, ^.style := Map("paddingLeft" -> "3px"))(
//          <.div(^.className := props.nodeClass)(
//            <.div(^.className := props.valueClass)(
//              <.label()(childText)
//            )
//          )
//        )
//      )
    })
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
