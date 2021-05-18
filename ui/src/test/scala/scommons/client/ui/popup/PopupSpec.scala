package scommons.client.ui.popup

import scommons.client.ui.popup.Popup._
import scommons.client.ui.popup.raw.NativeModal._
import scommons.react._
import scommons.react.test._

class PopupSpec extends TestSpec with TestRendererUtils {

  Popup.reactModal = "ReactModal".asInstanceOf[ReactClass]

  it should "call onOpen function when onAfterOpen" in {
    //given
    val onOpen = mockFunction[Unit]
    val props = PopupProps(onClose = () => (), onOpen = onOpen)
    val component = testRender(<(Popup())(^.wrapped := props)())

    //then
    onOpen.expects()

    //when
    component.props.onAfterOpen()
  }

  it should "call onClose function when onRequestClose" in {
    //given
    val onClose = mockFunction[Unit]
    val props = PopupProps(onClose = onClose)
    val component = testRender(<(Popup())(^.wrapped := props)())

    //then
    onClose.expects()

    //when
    component.props.onRequestClose()
  }

  it should "render closable, non-focusable popup with default styles" in {
    //given
    val props = PopupProps(focusable = false, onClose = () => ())
    val component = <(Popup())(^.wrapped := props)(
      <.div()("some child")
    )

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result,
      <(reactModal)(
        ^.isOpen := true,
        ^.shouldCloseOnOverlayClick := true,
        ^.shouldFocusAfterRender := false,
        ^.shouldReturnFocusAfterClose := false,
        ^.overlayClassName := props.overlayClass,
        ^.modalClassName := props.popupClass
      )(
        <.div()("some child")
      )
    )
  }

  it should "render non-closable, focusable popup with custom styles" in {
    //given
    val props = PopupProps(
      onClose = () => (),
      closable = false,
      overlayClass = "test-overlay",
      popupClass = "test-popup"
    )
    val component = <(Popup())(^.wrapped := props)(
      <.div()("some child")
    )

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result,
      <(reactModal)(
        ^.isOpen := true,
        ^.shouldCloseOnOverlayClick := false,
        ^.shouldFocusAfterRender := true,
        ^.shouldReturnFocusAfterClose := true,
        ^.overlayClassName := props.overlayClass,
        ^.modalClassName := props.popupClass
      )(
        <.div()("some child")
      )
    )
  }
}
