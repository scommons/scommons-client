package scommons.client.app

import io.github.shogowada.scalajs.reactjs.React._
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.raw.ReactTestUtils._

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

    assertDOMElement(findReactElement(result),
      <div>
        <hr/>
        <footer>
          <p class="text-center">
            <span>
              {s"${props.copyright}"}
            </span>{" "}<small>
            {s"${props.version}"}
          </small>
          </p>
        </footer>
      </div>
    )
  }
}
