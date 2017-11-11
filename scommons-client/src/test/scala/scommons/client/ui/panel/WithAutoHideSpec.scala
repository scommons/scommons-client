package scommons.client.ui.panel

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

class WithAutoHideSpec extends FlatSpec with Matchers with MockFactory {

  "rendering" should "render the component" in {
    //given
    val content = "test content"
    val component = E(WithAutoHide())(A.wrapped := WithAutoHideProps(() => ()))(
      E.p()(content)
    )

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div>
        <p>{content}</p>
      </div>
    )
  }
}
