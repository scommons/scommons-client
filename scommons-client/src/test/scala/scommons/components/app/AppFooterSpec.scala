package scommons.components.app

import io.github.shogowada.scalajs.reactjs.React._
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ReactTestUtils._

class AppFooterSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val component = createElement(AppFooter.reactClass, wrap(AppFooterProps()))

    //when
    val result = renderIntoDocument(component)

    //then
    val p = findRenderedDOMComponentWithClass(result, "text-center")
    isDOMComponent(p) shouldBe true
  }
}
