package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.ReactDOM
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.util.KeyCodes

import scala.scalajs.js

class TextFieldSpec extends FlatSpec
  with Matchers
  with ShallowRendererUtils
  with MockFactory {

  "onChange" should "call onChange function when input is changed" in {
    //given
    val onChange = mockFunction[String, Unit]
    val props = TextFieldProps("test text", onChange)
    val comp = renderIntoDocument(E(TextField())(A.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text

    //then
    onChange.expects(props.text)

    //when
    ReactTestUtils.Simulate.change(inputElem, js.Dynamic.literal(target = inputElem))
  }

  "onKeyDown" should "call onEnter function when keyCode is Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = TextFieldProps("test text", _ => (), onEnter = onEnter)
    val comp = renderIntoDocument(E(TextField())(A.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text

    //then
    onEnter.expects()

    //when
    ReactTestUtils.Simulate.keyDown(inputElem, js.Dynamic.literal(keyCode = KeyCodes.KEY_ENTER))
  }

  it should "not call onEnter function when keyCode is other than Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = TextFieldProps("test text", _ => (), onEnter = onEnter)
    val comp = renderIntoDocument(E(TextField())(A.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text

    //then
    onEnter.expects().never()

    //when
    ReactTestUtils.Simulate.keyDown(inputElem, js.Dynamic.literal(keyCode = KeyCodes.KEY_UP))
  }

  "rendering" should "render correct props" in {
    //given
    val props = TextFieldProps(
      "test text",
      onChange = _ => (),
      className = Some("test-class"),
      placeholder = Some("test-placeholder")
    )
    val component = E(TextField())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, E.input(
      ^.`type` := "text",
      props.className.map(^.className := _),
      props.placeholder.map(^.placeholder := _),
      ^.value := props.text
    )())
  }

  "requestFocus" should "focus input element if requestFocus prop changed from false to true" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    val comp = renderIntoDocument(E(TextField())(A.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(E(TextField())(A.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.value shouldBe props.text

    //cleanup
    document.body.removeChild(containerElement)
  }

  it should "not focus input element if requestFocus prop not changed" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => (), requestFocus = true)
    val comp = renderIntoDocument(E(TextField())(A.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(E(TextField())(A.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem should not be document.activeElement
    inputElem.value shouldBe props.text

    //cleanup
    document.body.removeChild(containerElement)
  }

  "requestSelect" should "select text if requestSelect prop changed from false to true" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    val comp = renderIntoDocument(E(TextField())(A.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)
    val containerElement = findReactElement(comp).parentNode
    props should not be prevProps

    //when
    ReactDOM.render(E(TextField())(A.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe props.text.length
  }

  it should "not select text if requestSelect prop not changed" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => (), requestSelect = true)
    val comp = renderIntoDocument(E(TextField())(A.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)
    val containerElement = findReactElement(comp).parentNode
    props should not be prevProps

    //when
    ReactDOM.render(E(TextField())(A.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe 0
  }
}
