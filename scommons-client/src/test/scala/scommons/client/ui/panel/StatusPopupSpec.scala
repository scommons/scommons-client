package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer
import scommons.client.test.raw.TestReactDOM._
import scommons.client.ui.panel.PopupPanelCss._
import scommons.react.modal.NativeReactModal

class StatusPopupSpec extends FlatSpec with Matchers {

  "rendering" should "pass correct props to the children" in {
    //given
    val props = StatusPopupProps("test text", show = true, () => ())
    val component = E(StatusPopup())(A.wrapped := props)()

    //when
    val result = ShallowRenderer.renderAndGetOutput(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe true
    result.props.overlayClassName shouldBe "scommons-status-no-overlay"
    result.props.className shouldBe statusContent
    result.props.children.`type` shouldBe WithAutoHide()
    result.props.children.props.wrapped shouldBe WithAutoHideProps(props.onHide)
    result.props.children.props.children shouldBe props.text
  }

  it should "render the component in the DOM" in {
    //given
    val text = "test text"
    val component = E(StatusPopup())(A.wrapped := StatusPopupProps(text, show = true, () => ()))()

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class="ReactModal__Overlay ReactModal__Overlay--after-open scommons-status-no-overlay">
        <div class={s"ReactModal__Content ReactModal__Content--after-open $statusContent"} tabindex="-1">
          <div>
            {text}
          </div>
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }
}
