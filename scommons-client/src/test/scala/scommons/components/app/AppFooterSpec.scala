package scommons.components.app

import io.github.shogowada.scalajs.reactjs.React
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ReactTestUtils

class AppFooterSpec extends FlatSpec with Matchers {

  it should "render the component" in {
    ReactTestUtils.renderIntoDocument(React.createElement(AppFooter.reactClass, React.wrap(AppFooterProps())))
  }
}
