package scommons.client.ui

import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLButtonElement
import scommons.client.ui.ButtonImagesCss._
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

class ImageButtonSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    domRender(<(ImageButton())(^.wrapped := ImageButtonProps(data, onClick))())
    val button = domContainer.querySelector("button")

    //then
    onClick.expects()

    //when
    fireDomEvent(Simulate.click(button))
  }

  it should "render button with text" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")

    //when
    val result = shallowRender(<(ImageButton())(^.wrapped := ImageButtonProps(data, () => ()))())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render button with title" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test title")
    val props = ImageButtonProps(data, () => (), showTextAsTitle = true)

    //when
    val result = shallowRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ", ^.title := s"${data.text}")(
        <.img(^.className := s"${data.image}", ^.src := "")()
      )
    )
  }

  it should "render disabled button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Disabled")
    val props = ImageButtonProps(data, () => (), disabled = true)

    //when
    val result = shallowRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ", ^.disabled := true)(
        <.img(^.className := s"${data.disabledImage}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render primary button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Primary", primary = true)
    val props = ImageButtonProps(data, () => ())

    //when
    val result = shallowRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn btn-primary")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render button in the DOM" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")

    //when
    domRender(<(ImageButton())(^.wrapped := ImageButtonProps(data, () => ()))())

    //then
    assertDOMElement(domContainer, <.div()(
      <.button(^.`type` := "button", ^("class") := "btn")(
        <.img(^("class") := s"${data.image}", ^.src := "")(),
        <.span(^("style") := "padding-left: 3px; vertical-align: middle;")(data.text)
      )
    ))
  }

  it should "focus button element if requestFocus = true" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val props = ImageButtonProps(data, () => (), requestFocus = true)

    //when
    domRender(<(ImageButton())(^.wrapped := props)())

    //then
    val buttonElem = domContainer.querySelector("button").asInstanceOf[HTMLButtonElement]
    buttonElem shouldBe document.activeElement
  }

  it should "focus button element if requestFocus changed from false to true" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => ())
    domRender(<(ImageButton())(^.wrapped := prevProps)())
    val props = ImageButtonProps(data, () => (), requestFocus = true)
    props should not be prevProps
    domContainer.querySelector("button") should not be document.activeElement

    //when
    domRender(<(ImageButton())(^.wrapped := props)())

    //then
    val buttonElem = domContainer.querySelector("button").asInstanceOf[HTMLButtonElement]
    buttonElem shouldBe document.activeElement
  }

  it should "not focus button element if requestFocus not changed" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => ())
    domRender(<(ImageButton())(^.wrapped := prevProps)())
    val props = ImageButtonProps(data, () => (), showTextAsTitle = true)
    props should not be prevProps

    //when
    domRender(<(ImageButton())(^.wrapped := props)())

    //then
    val buttonElem = domContainer.querySelector("button").asInstanceOf[HTMLButtonElement]
    buttonElem should not be document.activeElement
  }
}
