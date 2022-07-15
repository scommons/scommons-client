package scommons.client.ui.popup

import scommons.react.test._

class ModalHeaderSpec extends TestSpec with TestRendererUtils {

  it should "call onClose function when onCloseClick" in {
    //given
    val onClose = mockFunction[Unit]
    val props = ModalHeaderProps("Test Header", onClose = onClose)
    val comp = testRender(<(ModalHeader())(^.wrapped := props)())
    val button = inside(findComponents(comp, <.button.name)) {
      case List(b) => b
    }

    //then
    onClose.expects()

    //when
    button.props.onClick(null)
  }

  it should "render closable header component" in {
    //given
    val props = ModalHeaderProps("Test Header", () => ())
    val component = <(ModalHeader())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := "modal-header")(), inside(_) { case List(closeButton, h3) =>
      assertNativeComponent(closeButton, <.button(
        ^.`type` := "button",
        ^.className := "close"
      )("×"))
      assertNativeComponent(h3, <.h3()(props.header))
    })
  }

  it should "render non-closable header component" in {
    //given
    val props = ModalHeaderProps("Test Header", () => (), closable = false)
    val component = <(ModalHeader())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := "modal-header")(), inside(_) { case List(h3) =>
      assertNativeComponent(h3, <.h3()(props.header))
    })
  }
}
