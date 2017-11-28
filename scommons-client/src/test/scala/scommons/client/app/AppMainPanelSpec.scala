package scommons.client.app

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.test.TestUtils._
import scommons.client.test.raw.ReactTestUtils._

class AppMainPanelSpec extends TestSpec {

  it should "render the component" in {
    //given
    val props = AppMainPanelProps(
      name = "test name",
      user = "test user",
      copyright = "test copyright",
      version = "test version"
    )
    val component = <(AppMainPanel.reactClass)(^.wrapped := props)(
      <.div()("Some child element 1"),
      <.div()("Some child element 2")
    )

    //when
    val result = renderIntoDocument(component)

    //then
    result.props.wrapped shouldBe props

    val resultElement = findReactElement(result)
    resultElement.childElementCount shouldBe 3

    val appHeader = findRenderedComponentWithType(result, AppHeader.reactClass)
    appHeader.props.wrapped shouldBe AppHeaderProps(props.name, props.user)

    val appFooter = findRenderedComponentWithType(result, AppFooter.reactClass)
    appFooter.props.wrapped shouldBe AppFooterProps(props.copyright, props.version)

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
