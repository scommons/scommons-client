package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer
import scommons.client.test.raw.TestReactDOM._
import scommons.client.ui.panel.PopupPanelCss._
import scommons.react.modal.NativeReactModal

class LoadingPopupSpec extends FlatSpec with Matchers {

  "rendering" should "pass correct props to the children" in {
    //given
    val component = E(LoadingPopup())(A.wrapped := LoadingPopupProps(show = true))()

    //when
    val result = ShallowRenderer.renderAndGetOutput(component)

    //then
    result.`type` shouldBe NativeReactModal
    result.props.isOpen shouldBe true
    result.props.shouldCloseOnOverlayClick shouldBe false
    result.props.overlayClassName shouldBe loadingOverlay
    result.props.className shouldBe loadingContent
    result.props.children.`type` shouldBe "img"
    result.props.children.props.className shouldBe loadingImg
    result.props.children.props.src shouldBe ""
  }

  it should "render the component in the DOM" in {
    //given
    val component = E(LoadingPopup())(A.wrapped := LoadingPopupProps(show = true))()

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class={s"ReactModal__Overlay ReactModal__Overlay--after-open $loadingOverlay"}>
        <div class={s"ReactModal__Content ReactModal__Content--after-open $loadingContent"} tabindex="-1">
          <img class={s"$loadingImg"} src=""/>
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }
}
