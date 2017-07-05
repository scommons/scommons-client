package scommons.client.app

import org.scalatest.{FlatSpec, Matchers}
import scommons.client.TestUtils._
import scommons.client.TestVirtualDOM._
import scommons.client.test.ReactTestUtils._

class AppMainPanelSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val component = E(AppMainPanel.reactClass)()(
      E.div()("Some child element 1"),
      E.div()("Some child element 2")
    )

    //when
    val result = renderIntoDocument(component)

    //then
    val resultElement = asElement(asNode(result))
    resultElement.childElementCount shouldBe 3

    val container = resultElement.children(1)
    assertDOMElement(container,
      <div class="container-fluid">
        <div>
          Some child element 1
        </div>
        <div>
          Some child element 2
        </div>
      </div>
    )
  }
}
