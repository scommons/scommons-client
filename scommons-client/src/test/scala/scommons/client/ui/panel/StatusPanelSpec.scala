package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.TestReactDOM._
import scommons.react.modal.NativeReactModal

class StatusPanelSpec extends FlatSpec with Matchers {

  "rendering" should "render the component" in {
    //given
    val text = "test text"
    val component = E(StatusPanel())(A.wrapped := StatusPanelProps(text, show = true, () => ()))()

    //when
    val result = renderIntoDocument(component)

    //then
    val modal = findRenderedComponentWithType(result, NativeReactModal).portal
    assertDOMElement(findReactElement(modal),
      <div class="ReactModal__Overlay ReactModal__Overlay--after-open no-overlay">
        <div class="ReactModal__Content ReactModal__Content--after-open"
             style="position: fixed; top: 100%; background: rgb(255, 255, 255); overflow: auto; border-radius: 4px; outline: none; margin-top: -20px;"
             tabindex="-1">
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
