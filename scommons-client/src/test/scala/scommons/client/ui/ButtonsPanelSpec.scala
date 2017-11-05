package scommons.client.ui

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.ui.ButtonImagesCss._

class ButtonsPanelSpec extends FlatSpec with Matchers with MockFactory {

  "onClick" should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[ButtonData, Unit]
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val comp = renderIntoDocument(E(ButtonsPanel())(A.wrapped := ButtonsPanelProps(
      List(data), Set(data.command), group = false, onClick
    ))())
    val button = findRenderedDOMComponentWithClass(comp, "btn")

    //then
    onClick.expects(data)

    //when
    ReactTestUtils.Simulate.click(button)
  }

  "rendering" should "render buttons toolbar" in {
    //given
    val b1 = ImageButtonData("accept", accept, acceptDisabled, "test button 1")
    val b2 = ImageButtonData("add", add, addDisabled, "test button 2")
    val component = E(ButtonsPanel())(A.wrapped := ButtonsPanelProps(
      List(b1, b2), Set(b1.command), group = false, _ => ()
    ))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class="btn-toolbar">
        <button class="btn">
          <img class={s"${b1.image}"} src=""/>
          <span style="padding-left: 3px; vertical-align: middle;">{b1.text}</span>
        </button>
        <button class="btn" disabled="">
          <img class={s"${b2.disabledImage}"} src=""/>
          <span style="padding-left: 3px; vertical-align: middle;">{b2.text}</span>
        </button>
      </div>
    )
  }

  it should "render buttons group" in {
    //given
    val b1 = ImageButtonData("accept", accept, acceptDisabled, "test button 1")
    val b2 = ImageButtonData("add", add, addDisabled, "test button 2")
    val component = E(ButtonsPanel())(A.wrapped := ButtonsPanelProps(
      List(b1, b2), Set(b1.command), group = true, _ => ()
    ))()

    //when
    val result = renderIntoDocument(component)

    //then
    assertDOMElement(findReactElement(result),
      <div class="btn-group">
        <button class="btn" title={s"${b1.text}"}>
          <img class={s"${b1.image}"} src=""/>
        </button>
        <button class="btn" disabled="" title={s"${b2.text}"}>
          <img class={s"${b2.disabledImage}"} src=""/>
        </button>
      </div>
    )
  }
}
