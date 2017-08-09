package scommons.client.test.raw

import io.github.shogowada.scalajs.reactjs.React
import io.github.shogowada.scalajs.reactjs.events.MouseSyntheticEvent
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

import scala.scalajs.js

class SimulateSpec extends FlatSpec with Matchers {

  it should "simulate onClick event" in {
    //given
    var clicked = false
    var clientX = 0
    var clientY = 0
    def onClick() = { (e: MouseSyntheticEvent) =>
      clicked = true
      clientX = e.clientX
      clientY = e.clientY
    }
    val comp = renderIntoDocument(React.createElement(React.createClass[Unit, Unit](_ =>
      E.div()(
        E.button(A.onClick := onClick)("Click me")
      )
    )))
    val button = findRenderedDOMComponentWithTag(comp, "button")

    //when
    ReactTestUtils.Simulate.click(button, js.Dynamic.literal(
      clientX = 1,
      clientY = 2
    ))

    //then
    clicked shouldBe true
    clientX shouldBe 1
    clientY shouldBe 2
  }
}
