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

    val div = asNode(result, "div")
    val divChildren = asArray(div.children)
    divChildren.length shouldBe 2
    asNode(divChildren(0), "hr")

    val footer = asNode(divChildren(1), "footer")
    val p = asNode(footer.firstChild, "p")
    p.className shouldBe "text-center"
    p.textContent shouldBe s"${props.copyright} ${props.version}"

    val pChildren = asArray(p.children)
    pChildren.length shouldBe 2
    asNode(pChildren(0), "span").textContent shouldBe s"${props.copyright}"
    asNode(pChildren(1), "small").textContent shouldBe s"${props.version}"
  }
}
