package scommons.client.ui.popup

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

class ModalHeaderSpec extends FlatSpec
  with Matchers
  with ShallowRendererUtils
  with MockFactory {

  "onCloseClick" should "call onClose function" in {
    //given
    val onClose = mockFunction[Unit]
    val props = ModalHeaderProps("Test Header", onClose = onClose)
    val comp = renderIntoDocument(E(ModalHeader())(^.wrapped := props)())
    val button = findRenderedDOMComponentWithClass(comp, "close")

    //then
    onClose.expects()

    //when
    Simulate.click(button)
  }

  "rendering" should "render closable header component" in {
    //given
    val props = ModalHeaderProps("Test Header", () => ())
    val component = E(ModalHeader())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, E.div(^.className := "modal-header")(), { case List(closeButton, h3) =>
      assertDOMComponent(closeButton, E.button(
        ^.`type` := "button",
        ^.className := "close"
      )("×"))
      assertDOMComponent(h3, E.h3()(props.header))
    })
  }

  it should "render non-closable header component" in {
    //given
    val props = ModalHeaderProps("Test Header", () => (), closable = false)
    val component = E(ModalHeader())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, E.div(^.className := "modal-header")(), { case List(h3) =>
      assertDOMComponent(h3, E.h3()(props.header))
    })
  }
}
