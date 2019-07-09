package scommons.client.ui

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLButtonElement
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

class SimpleButtonSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = onClick)
    domRender(<(SimpleButton())(^.wrapped := props)())
    val button = domContainer.querySelector("button")

    //then
    onClick.expects()

    //when
    fireDomEvent(Simulate.click(button))
  }

  it should "render normal button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => ())

    //when
    val result = shallowRender(<(SimpleButton())(^.wrapped := props)())

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

    //when
    val result = shallowRender(<(SimpleButton())(^.wrapped := props)())

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

    //when
    val result = shallowRender(<(SimpleButton())(^.wrapped := props)())

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

    //when
    val result = shallowRender(<(SimpleButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary",
      ^.disabled := true
    )(data.text))
  }

  it should "focus button element if requestFocus = true" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, () => (), requestFocus = true)

    //when
    domRender(<(SimpleButton())(^.wrapped := props)())

    //then
    val buttonElem = domContainer.querySelector("button").asInstanceOf[HTMLButtonElement]
    buttonElem shouldBe document.activeElement
  }

  it should "focus button element if requestFocus changed from false to true" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val prevProps = SimpleButtonProps(data, () => ())
    domRender(<(SimpleButton())(^.wrapped := prevProps)())
    val props = SimpleButtonProps(data, () => (), requestFocus = true)
    props should not be prevProps
    domContainer.querySelector("button") should not be document.activeElement

    //when
    domRender(<(SimpleButton())(^.wrapped := props)())

    //then
    val buttonElem = domContainer.querySelector("button").asInstanceOf[HTMLButtonElement]
    buttonElem shouldBe document.activeElement
  }

  it should "not focus button element if requestFocus not changed" in {
    //given
    val prevProps = SimpleButtonProps(SimpleButtonData("test-command", "Text"), () => ())
    domRender(<(SimpleButton())(^.wrapped := prevProps)())
    val props = SimpleButtonProps(SimpleButtonData("test-command", "New Text"), () => ())
    props should not be prevProps

    //when
    domRender(<(SimpleButton())(^.wrapped := props)())

    //then
    val buttonElem = domContainer.querySelector("button").asInstanceOf[HTMLButtonElement]
    buttonElem should not be document.activeElement
  }
}
