package scommons.client.test

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ReactTestUtils._
import scommons.client.test.TestVirtualDOM._

class ReactTestUtilsSimulateSpec extends FlatSpec with Matchers {

  it should "simulate onClick event" in {
    //given
    var clicked = false
    def onClick() = { (_: MouseSyntheticEvent) =>
      clicked = true
    }
    val comp = renderIntoDocument(React.createElement(React.createClass[Unit, Unit](_ =>
      E.div()(
        E.button(A.onClick := onClick)("Click me")
      )
    )))
    val button = findRenderedDOMComponentWithTag(comp, "button")

    //when
    ReactTestUtils.Simulate.click(button)

    //then
    clicked shouldBe true
  }
}
