package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.ReactDOM
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLButtonElement
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.ButtonImagesCss._

class ImageButtonSpec extends FlatSpec with Matchers with MockFactory {

  "onClick" should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    val comp = renderIntoDocument(E(ImageButton())(A.wrapped := ImageButtonProps(data, onClick))())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onClick.expects()

    //when
    ReactTestUtils.Simulate.click(button)
  }

  "rendering" should "render button with text" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    val component = E(ImageButton())(A.wrapped := ImageButtonProps(data, () => ()))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button type= "button" class="btn">
        <img class={s"${data.image}"} src=""/>
        <span style="padding-left: 3px; vertical-align: middle;">{data.text}</span>
      </button>
    )
  }

  it should "render button with title" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test title")
    val component = E(ImageButton())(A.wrapped := ImageButtonProps(data, () => (), showTextAsTitle = true))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button type= "button" class="btn" title={s"${data.text}"}>
        <img class={s"${data.image}"} src=""/>
      </button>
    )
  }

  it should "render disabled button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Disabled")
    val component = E(ImageButton())(A.wrapped := ImageButtonProps(data, () => (), disabled = true))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button type= "button" class="btn" disabled="">
        <img class={s"${data.disabledImage}"} src=""/>
        <span style="padding-left: 3px; vertical-align: middle;">{data.text}</span>
      </button>
    )
  }

  it should "render primary button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Primary", primary = true)
    val component = E(ImageButton())(A.wrapped := ImageButtonProps(data, () => ()))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button type= "button" class="btn btn-primary">
        <img class={s"${data.image}"} src=""/>
        <span style="padding-left: 3px; vertical-align: middle;">{data.text}</span>
      </button>
    )
  }

  "requestFocus" should "focus button element if requestFocus prop changed from false to true" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => ())
    val comp = renderIntoDocument(E(ImageButton())(A.wrapped := prevProps)())
    val props = ImageButtonProps(data, () => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(E(ImageButton())(A.wrapped := props)(), containerElement)

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
    val comp = renderIntoDocument(E(ImageButton())(A.wrapped := prevProps)())
    val props = ImageButtonProps(data, () => (), showTextAsTitle = true, requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(E(ImageButton())(A.wrapped := props)(), containerElement)

    //then
    val buttonElem = findRenderedDOMComponentWithTag(comp, "button").asInstanceOf[HTMLButtonElement]
    buttonElem should not be document.activeElement

    //cleanup
    document.body.removeChild(containerElement)
  }
}
