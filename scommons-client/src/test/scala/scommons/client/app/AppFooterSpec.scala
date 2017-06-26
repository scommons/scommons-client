package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React._
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.TestUtils._
import scommons.client.test.ReactTestUtils._

class AppFooterSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val props = AppFooterProps(
      "test copyright",
      "test version"
    )
    val component = createElement(AppFooter.reactClass, wrap(props))

    //when
    val result = renderIntoDocument(component)

    //then
    result.props.wrapped shouldBe props

    val div = asElement(asNode(result), "div", 2)
    val hr = asElement(div.firstElementChild, "hr")
    val footer = asElement(hr.nextElementSibling, "footer", 1)

    val p = asElement(footer.firstElementChild, "p", 2)
    p.classList.contains("text-center") shouldBe true
    p.textContent shouldBe s"${props.copyright} ${props.version}"

    val span = asElement(p.firstElementChild, "span")
    span.textContent shouldBe s"${props.copyright}"

    val small = asElement(span.nextElementSibling, "small")
    small.textContent shouldBe s"${props.version}"
  }
}
