package scommons.client.ui.list

import io.github.shogowada.statictags.Element
import scommons.react.test._

class PickButtonsSpec extends TestSpec with TestRendererUtils {

  it should "call onAdd when onClick in vertical group" in {
    //given
    val onAdd = mockFunction[Unit]
    val props = PickButtonsProps(onAdd = onAdd)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onAdd.expects()

    //when & then
    items.head.props.onClick(null)
  }

  it should "call onRemove when onClick in vertical group" in {
    //given
    val onRemove = mockFunction[Unit]
    val props = PickButtonsProps(onRemove = onRemove)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onRemove.expects()

    //when & then
    items(1).props.onClick(null)
  }

  it should "call onAddAll when onClick in vertical group" in {
    //given
    val onAddAll = mockFunction[Unit]
    val props = PickButtonsProps(onAddAll = onAddAll)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onAddAll.expects()

    //when & then
    items(2).props.onClick(null)
  }

  it should "call onRemoveAll when onClick in vertical group" in {
    //given
    val onRemoveAll = mockFunction[Unit]
    val props = PickButtonsProps(onRemoveAll = onRemoveAll)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onRemoveAll.expects()

    //when & then
    items(3).props.onClick(null)
  }

  it should "call onAdd when onClick in horizontal group" in {
    //given
    val onAdd = mockFunction[Unit]
    val props = PickButtonsProps(onAdd = onAdd)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onAdd.expects()

    //when & then
    items(4).props.onClick(null)
  }

  it should "call onRemove when onClick in horizontal group" in {
    //given
    val onRemove = mockFunction[Unit]
    val props = PickButtonsProps(onRemove = onRemove)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onRemove.expects()

    //when & then
    items(5).props.onClick(null)
  }

  it should "call onAddAll when onClick in horizontal group" in {
    //given
    val onAddAll = mockFunction[Unit]
    val props = PickButtonsProps(onAddAll = onAddAll)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onAddAll.expects()

    //when & then
    items(6).props.onClick(null)
  }

  it should "call onRemoveAll when onClick in horizontal group" in {
    //given
    val onRemoveAll = mockFunction[Unit]
    val props = PickButtonsProps(onRemoveAll = onRemoveAll)
    val comp = testRender(<(PickButtons())(^.wrapped := props)())
    val items = findComponents(comp, <.button.name)
    items.size shouldBe 8

    //then
    onRemoveAll.expects()

    //when & then
    items(7).props.onClick(null)
  }

  it should "render component" in {
    //given
    val props = PickButtonsProps()
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with custom className" in {
    //given
    val props = PickButtonsProps(className = Some("some style"))
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled add button" in {
    //given
    val props = PickButtonsProps(addEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled remove button" in {
    //given
    val props = PickButtonsProps(removeEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled addAll button" in {
    //given
    val props = PickButtonsProps(addAllEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPickButtons(result, props)
  }

  it should "render component with disabled removeAll button" in {
    //given
    val props = PickButtonsProps(removeAllEnabled = false)
    val comp = <(PickButtons())(^.wrapped := props)()

    //when
    val result = testRender(comp)

    //then
    assertPickButtons(result, props)
  }

  private def assertPickButtons(result: TestInstance, props: PickButtonsProps): Unit = {

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
    
    assertNativeComponent(result, <.div(props.className.map(cn => ^.className := cn))(), inside(_) { case List(vert, horiz) =>
      assertNativeComponent(vert, <.div(^.className := "btn-group btn-group-vertical hidden-phone")(), inside(_) {
        case List(add, remove, addAll, removeAll) =>
          assertNativeComponent(add, btn(btnVert, "Add", ">", props.addEnabled))
          assertNativeComponent(remove, btn(btnVert, "Remove", "<", props.removeEnabled))
          assertNativeComponent(addAll, btn(btnVert, "Add All", ">>", props.addAllEnabled))
          assertNativeComponent(removeAll, btn(btnVert, "Remove All", "<<", props.removeAllEnabled))
      })
      assertNativeComponent(horiz, <.div(^.className := "btn-group visible-phone", ^.style := btnGroupHoriz)(), inside(_) {
        case List(add, remove, addAll, removeAll) =>
          assertNativeComponent(add, btn(btnHoriz, "Add", ">", props.addEnabled))
          assertNativeComponent(remove, btn(btnHoriz, "Remove", "<", props.removeEnabled))
          assertNativeComponent(addAll, btn(btnHoriz, "Add All", ">>", props.addAllEnabled))
          assertNativeComponent(removeAll, btn(btnHoriz, "Remove All", "<<", props.removeAllEnabled))
      })
    })
  }
}
