package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLButtonElement
import scommons.client.test.TestSpec
import scommons.client.test.util.TestDOMUtils._
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.ButtonImagesCss._

class ImageButtonSpec extends TestSpec {

  it should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    val comp = renderIntoDocument(<(ImageButton())(^.wrapped := ImageButtonProps(data, onClick))())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onClick.expects()

    //when
    ReactTestUtils.Simulate.click(button)
  }

  it should "render button with text" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    val component = <(ImageButton())(^.wrapped := ImageButtonProps(data, () => ()))()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.button(^.`type` := "button", ^.className := "btn")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render button with title" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test title")
    val component = <(ImageButton())(^.wrapped := ImageButtonProps(data, () => (), showTextAsTitle = true))()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.button(^.`type` := "button", ^.className := "btn", ^.title := s"${data.text}")(
        <.img(^.className := s"${data.image}", ^.src := "")()
      )
    )
  }

  it should "render disabled button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Disabled")
    val component = <(ImageButton())(^.wrapped := ImageButtonProps(data, () => (), disabled = true))()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.button(^.`type` := "button", ^.className := "btn", ^.disabled := true)(
        <.img(^.className := s"${data.disabledImage}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render primary button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Primary", primary = true)
    val component = <(ImageButton())(^.wrapped := ImageButtonProps(data, () => ()))()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result,
      <.button(^.`type` := "button", ^.className := "btn btn-primary")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render button in the DOM" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    val component = <(ImageButton())(^.wrapped := ImageButtonProps(data, () => ()))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <.button(^.`type` := "button", ^("class") := "btn")(
        <.img(^("class") := s"${data.image}", ^.src := "")(),
        <.span(^("style") := "padding-left: 3px; vertical-align: middle;")(data.text)
      )
    )
  }

  it should "focus button element if requestFocus prop changed from false to true" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => ())
    val comp = renderIntoDocument(<(ImageButton())(^.wrapped := prevProps)())
    val props = ImageButtonProps(data, () => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(ImageButton())(^.wrapped := props)(), containerElement)

    //then
    val buttonElem = findRenderedDOMComponentWithTag(comp, "button").asInstanceOf[HTMLButtonElement]
    buttonElem shouldBe document.activeElement

    //cleanup
    document.body.removeChild(containerElement)
  }

  it should "not focus button element if requestFocus prop not changed" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => (), requestFocus = true)
    val comp = renderIntoDocument(<(ImageButton())(^.wrapped := prevProps)())
    val props = ImageButtonProps(data, () => (), showTextAsTitle = true, requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(ImageButton())(^.wrapped := props)(), containerElement)

    //then
    val buttonElem = findRenderedDOMComponentWithTag(comp, "button").asInstanceOf[HTMLButtonElement]
    buttonElem should not be document.activeElement

    //cleanup
    document.body.removeChild(containerElement)
  }
}
