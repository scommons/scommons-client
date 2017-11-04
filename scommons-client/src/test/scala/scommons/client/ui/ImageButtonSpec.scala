package scommons.client.ui

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
    val props = ImageButtonProps(accept, Some("button with text"), onClick)
    val comp = renderIntoDocument(E(ImageButton())(A.wrapped := props)())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onClick.expects()

    //when
    ReactTestUtils.Simulate.click(button)
  }

  "rendering" should "render button with text" in {
    //given
    val props = ImageButtonProps(accept, Some("button with text"), () => ())
    val component = E(ImageButton())(A.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button class="btn">
        <img class={s"${props.image}"} src=""/>
        <span style="padding-left: 3px; vertical-align: middle;">{props.text.get}</span>
      </button>
    )
  }

  it should "render button without text" in {
    //given
    val props = ImageButtonProps(accept, None, () => ())
    val component = E(ImageButton())(A.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button class="btn">
        <img class={s"${props.image}"} src=""/>
      </button>
    )
  }

  it should "render disabled button" in {
    //given
    val props = ImageButtonProps(accept, Some("Disabled"), () => (), Some(acceptDisabled), disabled = true)
    val component = E(ImageButton())(A.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button class="btn" disabled="">
        <img class={s"${props.disabledImage.get}"} src=""/>
        <span style="padding-left: 3px; vertical-align: middle;">{props.text.get}</span>
      </button>
    )
  }

  it should "render primary button" in {
    //given
    val props = ImageButtonProps(accept, Some("Primary"), () => (), primary = true)
    val component = E(ImageButton())(A.wrapped := props)()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <button class="btn btn-primary">
        <img class={s"${props.image}"} src=""/>
        <span style="padding-left: 3px; vertical-align: middle;">{props.text.get}</span>
      </button>
    )
  }
}
