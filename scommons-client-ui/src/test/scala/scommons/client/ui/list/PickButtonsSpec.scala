package scommons.client.ui.list

import io.github.shogowada.statictags.Element
import scommons.react.test.TestSpec
import scommons.react.test.dom.raw.ReactTestUtils
import scommons.react.test.dom.raw.ReactTestUtils._
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class PickButtonsSpec extends TestSpec with ShallowRendererUtils {

  it should "call onAdd when onClick in vertical group" in {
    //given
    val onAdd = mockFunction[Unit]
    val props = PickButtonsProps(onAdd = onAdd)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onAdd.expects()

    //when & then
    ReactTestUtils.Simulate.click(items.head)
  }

  it should "call onRemove when onClick in vertical group" in {
    //given
    val onRemove = mockFunction[Unit]
    val props = PickButtonsProps(onRemove = onRemove)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onRemove.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(1))
  }

  it should "call onAddAll when onClick in vertical group" in {
    //given
    val onAddAll = mockFunction[Unit]
    val props = PickButtonsProps(onAddAll = onAddAll)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onAddAll.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(2))
  }

  it should "call onRemoveAll when onClick in vertical group" in {
    //given
    val onRemoveAll = mockFunction[Unit]
    val props = PickButtonsProps(onRemoveAll = onRemoveAll)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onRemoveAll.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(3))
  }

  it should "call onAdd when onClick in horizontal group" in {
    //given
    val onAdd = mockFunction[Unit]
    val props = PickButtonsProps(onAdd = onAdd)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onAdd.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(4))
  }

  it should "call onRemove when onClick in horizontal group" in {
    //given
    val onRemove = mockFunction[Unit]
    val props = PickButtonsProps(onRemove = onRemove)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onRemove.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(5))
  }

  it should "call onAddAll when onClick in horizontal group" in {
    //given
    val onAddAll = mockFunction[Unit]
    val props = PickButtonsProps(onAddAll = onAddAll)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onAddAll.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(6))
  }

  it should "call onRemoveAll when onClick in horizontal group" in {
    //given
    val onRemoveAll = mockFunction[Unit]
    val props = PickButtonsProps(onRemoveAll = onRemoveAll)
    val comp = renderIntoDocument(<(PickButtons())(^.wrapped := props)())
    val items = scryRenderedDOMComponentsWithClass(comp, "btn")
    items.length shouldBe 8

    //then
    onRemoveAll.expects()

    //when & then
    ReactTestUtils.Simulate.click(items(7))
  }

  it should "render component" in {
    //given
    val props = PickButtonsProps()
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with custom className" in {
    //given
    val props = PickButtonsProps(className = Some("some style"))
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled add button" in {
    //given
    val props = PickButtonsProps(addEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled remove button" in {
    //given
    val props = PickButtonsProps(removeEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled addAll button" in {
    //given
    val props = PickButtonsProps(addAllEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled removeAll button" in {
    //given
    val props = PickButtonsProps(removeAllEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = shallowRender(comp)

    //then
    assertPickButtons(result, props)
  }

  private def assertPickButtons(result: ShallowInstance, props: PickButtonsProps): Unit = {

    def btn(style: Map[String, String], title: String, text: String, enabled: Boolean): Element = {
      <.button(
        ^.`type` := "button",
        ^.className := "btn",
        ^.style := style,
        ^.title := title,
        ^.disabled := !enabled
      )(text)
    }

    val btnVert = Map("width" -> "35px")
    val btnHoriz = Map("height" -> "30px", "writingMode" -> "tb-rl")
    val btnGroupHoriz = Map("margin" -> "10px 0")
    
    assertNativeComponent(result, <.div(props.className.map(cn => ^.className := cn))(), { case List(vert, horiz) =>
      assertNativeComponent(vert, <.div(^.className := "btn-group btn-group-vertical hidden-phone")(), {
        case List(add, remove, addAll, removeAll) =>
          assertNativeComponent(add, btn(btnVert, "Add", ">", props.addEnabled))
          assertNativeComponent(remove, btn(btnVert, "Remove", "<", props.removeEnabled))
          assertNativeComponent(addAll, btn(btnVert, "Add All", ">>", props.addAllEnabled))
          assertNativeComponent(removeAll, btn(btnVert, "Remove All", "<<", props.removeAllEnabled))
      })
      assertNativeComponent(horiz, <.div(^.className := "btn-group visible-phone", ^.style := btnGroupHoriz)(), {
        case List(add, remove, addAll, removeAll) =>
          assertNativeComponent(add, btn(btnHoriz, "Add", ">", props.addEnabled))
          assertNativeComponent(remove, btn(btnHoriz, "Remove", "<", props.removeEnabled))
          assertNativeComponent(addAll, btn(btnHoriz, "Add All", ">>", props.addAllEnabled))
          assertNativeComponent(removeAll, btn(btnHoriz, "Remove All", "<<", props.removeAllEnabled))
      })
    })
  }
}
