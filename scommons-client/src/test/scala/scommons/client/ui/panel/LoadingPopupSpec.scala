package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.TestReactDOM._
import scommons.react.modal.NativeReactModal

class LoadingPopupSpec extends FlatSpec with Matchers {

  "rendering" should "render the component" in {
    //given
    val component = E(LoadingPopup())(A.wrapped := LoadingPopupProps(show = true))()

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class="ReactModal__Overlay ReactModal__Overlay--after-open"
           style="position: fixed; top: 0px; left: 0px; right: 0px; bottom: 0px; background-color: rgba(0, 0, 0, 0); cursor: wait;">
        <div class="ReactModal__Content ReactModal__Content--after-open"
             style="position: fixed; top: 50%; left: 50%; background: rgba(0, 0, 0, 0); overflow: auto; outline: none; margin-top: -16px; margin-left: -16px;"
             tabindex="-1">
          <img class={PopupPanelCss.loading} src=""/>
        </div>
      </div>
    )

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }
}
