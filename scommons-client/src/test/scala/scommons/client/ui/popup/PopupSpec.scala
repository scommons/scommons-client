package scommons.client.ui.popup

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.TestReactDOM._
import scommons.react.modal.NativeReactModal

class PopupSpec extends FlatSpec
  with Matchers
  with ShallowRendererUtils
  with MockFactory {

  "onAfterOpen" should "call onOpen function" in {
    //given
    val onOpen = mockFunction[Unit]
    val props = PopupProps(show = true, onClose = () => (), onOpen = onOpen)
    val component = shallowRender(E(Popup())(A.wrapped := props)())

    //then
    onOpen.expects()

    //when
    component.props.onAfterOpen()
  }

  "onRequestClose" should "call onClose function" in {
    //given
    val onClose = mockFunction[Unit]
    val props = PopupProps(show = true, onClose = onClose)
    val component = shallowRender(E(Popup())(A.wrapped := props)())

    //then
    onClose.expects()

    //when
    component.props.onRequestClose()
  }

  "rendering" should "render closable popup with default styles" in {
    //given
    val props = PopupProps(show = true, () => ())
    val component = E(Popup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe props.closable
    result.props.overlayClassName shouldBe props.overlayClass
    result.props.className shouldBe props.popupClass
  }

  it should "render non-closable popup with custom styles" in {
    //given
    val props = PopupProps(show = true, () => (),
      closable = false,
      overlayClass = "test-overlay",
      popupClass = "test-popup"
    )
    val component = E(Popup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe props.closable
    result.props.overlayClassName shouldBe props.overlayClass
    result.props.className shouldBe props.popupClass
  }

  it should "render component in the DOM" in {
    //given
    val props = PopupProps(show = true, () => ())
    val component = E(Popup())(A.wrapped := props)(
      E.p()("some children")
    )

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class={s"ReactModal__Overlay ReactModal__Overlay--after-open ${props.overlayClass}"}>
        <div class={s"ReactModal__Content ReactModal__Content--after-open ${props.popupClass}"} tabindex="-1">
          <p>
            {"some children"}
          </p>
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }
}
