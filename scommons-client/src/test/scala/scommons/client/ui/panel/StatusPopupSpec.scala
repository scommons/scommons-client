package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.TestReactDOM._
import scommons.client.ui.panel.PopupPanelCss._
import scommons.react.modal.NativeReactModal

class StatusPopupSpec extends FlatSpec with Matchers {

  "rendering" should "render the component" in {
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
