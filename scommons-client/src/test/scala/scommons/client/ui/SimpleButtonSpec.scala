package scommons.client.ui

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._

class SimpleButtonSpec extends FlatSpec
  with Matchers
  with ShallowRendererUtils
  with MockFactory {

  "onClick" should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = onClick)
    val comp = renderIntoDocument(E(SimpleButton())(A.wrapped := props)())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onClick.expects()

    //when
    Simulate.click(button)
  }

  "rendering" should "render normal button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => ())
    val component = E(SimpleButton())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertShallowRenderedElement(result, E.button(
      ^.`type` := "button",
      ^.className := "btn"
    )(data.text))
  }

  it should "render disabled button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => (), disabled = true)
    val component = E(SimpleButton())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertShallowRenderedElement(result, E.button(
      ^.`type` := "button",
      ^.className := "btn",
      ^.disabled := true
    )(data.text))
  }

  it should "render primary button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text", primary = true)
    val props = SimpleButtonProps(data, onClick = () => ())
    val component = E(SimpleButton())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertShallowRenderedElement(result, E.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary"
    )(data.text))
  }

  it should "render primary disabled button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text", primary = true)
    val props = SimpleButtonProps(data, onClick = () => (), disabled = true)
    val component = E(SimpleButton())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertShallowRenderedElement(result, E.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary",
      ^.disabled := true
    )(data.text))
  }
}
