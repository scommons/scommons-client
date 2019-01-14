package scommons.client.ui.popup

import scommons.client.ui.popup.raw.NativeReactModal
import scommons.react.test.TestSpec
import scommons.react.test.dom.raw.ReactTestUtils._
import scommons.react.test.dom.raw.TestReactDOM._
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

class PopupSpec extends TestSpec with ShallowRendererUtils with TestDOMUtils {

  it should "call onOpen function when onAfterOpen" in {
    //given
    val onOpen = mockFunction[Unit]
    val props = PopupProps(show = true, onClose = () => (), onOpen = onOpen)
    val component = shallowRender(<(Popup())(^.wrapped := props)())

    //then
    onOpen.expects()

    //when
    component.props.onAfterOpen()
  }

  it should "call onClose function when onRequestClose" in {
    //given
    val onClose = mockFunction[Unit]
    val props = PopupProps(show = true, onClose = onClose)
    val component = shallowRender(<(Popup())(^.wrapped := props)())

    //then
    onClose.expects()

    //when
    component.props.onRequestClose()
  }

  it should "render closable, non-focusable popup with default styles" in {
    //given
    val props = PopupProps(show = true, focusable = false, onClose = () => ())
    val component = <(Popup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe true
    result.props.shouldFocusAfterRender shouldBe false
    result.props.shouldReturnFocusAfterClose shouldBe false
    result.props.overlayClassName shouldBe props.overlayClass
    result.props.className shouldBe props.popupClass
  }

  it should "render non-closable, focusable popup with custom styles" in {
    //given
    val props = PopupProps(show = true, () => (),
      closable = false,
      overlayClass = "test-overlay",
      popupClass = "test-popup"
    )
    val component = <(Popup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe false
    result.props.shouldFocusAfterRender shouldBe true
    result.props.shouldReturnFocusAfterClose shouldBe true
    result.props.overlayClassName shouldBe props.overlayClass
    result.props.className shouldBe props.popupClass
  }

  it should "render and call onOpen function when render in the DOM" in {
    //given
    val onOpen = mockFunction[Unit]
    val props = PopupProps(show = true, onClose = () => (), onOpen = onOpen)
    val component = <(Popup())(^.wrapped := props)(
      <.p()("some children")
    )

    //then
    onOpen.expects()

    //when
    renderIntoDocument(component)
  }

  it should "render component in the DOM" in {
    //given
    val props = PopupProps(show = true, () => ())
    val component = <(Popup())(^.wrapped := props)(
      <.p()("some children")
    )

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <.div(^("class") := s"ReactModal__Overlay ReactModal__Overlay--after-open ${props.overlayClass}")(
        <.div(^("class") := s"ReactModal__Content ReactModal__Content--after-open ${props.popupClass}", ^.tabindex := -1)(
          <.p()("some children")
        )
      )
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }
}
