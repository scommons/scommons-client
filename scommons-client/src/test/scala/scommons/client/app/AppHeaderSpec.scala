package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React._
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.TestUtils._
import scommons.client.test.ReactTestUtils._

class AppHeaderSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    //given
    val props = AppHeaderProps(
      "test app",
      "test user"
    )
    val component = createElement(AppHeader.reactClass, wrap(props))

    //when
    val result = renderIntoDocument(component)

    //then
    result.props.wrapped shouldBe props

    val navbar = asElement(asNode(result), "div", 1)
    navbar.classList.contains("navbar") shouldBe true
    navbar.classList.contains("navbar-inverse") shouldBe true
    navbar.classList.contains("navbar-fixed-top") shouldBe true

    val navbarInner = asElement(navbar.firstElementChild, "div", 1)
    navbarInner.classList.contains("navbar-inner") shouldBe true

    val container = asElement(navbarInner.firstElementChild, "div", 3)
    container.classList.contains("container-fluid") shouldBe true

    val btnNavbar = asElement(container.firstElementChild, "button", 3)
    btnNavbar.getAttribute("type") shouldBe "button"
    btnNavbar.classList.contains("btn") shouldBe true
    btnNavbar.classList.contains("btn-navbar") shouldBe true
    btnNavbar.getAttribute("data-toggle") shouldBe "collapse"
    btnNavbar.getAttribute("data-target") shouldBe ".nav-collapse"
  }
}
