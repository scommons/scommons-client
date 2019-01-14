package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.ReactDOM
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLButtonElement
import scommons.react.test.TestSpec
import scommons.react.test.dom.raw.ReactTestUtils._
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

class SimpleButtonSpec extends TestSpec with ShallowRendererUtils with TestDOMUtils {

  it should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = onClick)
    val comp = renderIntoDocument(<(SimpleButton())(^.wrapped := props)())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onClick.expects()

    //when
    Simulate.click(button)
  }

  it should "render normal button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => ())
    val component = <(SimpleButton())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn"
    )(data.text))
  }

  it should "render disabled button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => (), disabled = true)
    val component = <(SimpleButton())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn",
      ^.disabled := true
    )(data.text))
  }

  it should "render primary button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text", primary = true)
    val props = SimpleButtonProps(data, onClick = () => ())
    val component = <(SimpleButton())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary"
    )(data.text))
  }

  it should "render primary disabled button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text", primary = true)
    val props = SimpleButtonProps(data, onClick = () => (), disabled = true)
    val component = <(SimpleButton())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary",
      ^.disabled := true
    )(data.text))
  }

  it should "focus button element if requestFocus prop changed from false to true" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val prevProps = SimpleButtonProps(data, () => ())
    val comp = renderIntoDocument(<(SimpleButton())(^.wrapped := prevProps)())
    val props = SimpleButtonProps(data, () => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(SimpleButton())(^.wrapped := props)(), containerElement)

    //then
    val buttonElem = findRenderedDOMComponentWithTag(comp, "button").asInstanceOf[HTMLButtonElement]
    buttonElem shouldBe document.activeElement

    //cleanup
    document.body.removeChild(containerElement)
  }

  it should "not focus button element if requestFocus prop not changed" in {
    //given
    val prevProps = SimpleButtonProps(SimpleButtonData("test-command", "Text"), () => (), requestFocus = true)
    val comp = renderIntoDocument(<(SimpleButton())(^.wrapped := prevProps)())
    val props = SimpleButtonProps(SimpleButtonData("test-command", "New Text"), () => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(SimpleButton())(^.wrapped := props)(), containerElement)

    //then
    val buttonElem = findRenderedDOMComponentWithTag(comp, "button").asInstanceOf[HTMLButtonElement]
    buttonElem should not be document.activeElement

    //cleanup
    document.body.removeChild(containerElement)
  }
}
