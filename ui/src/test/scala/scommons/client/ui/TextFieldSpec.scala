package scommons.client.ui

import org.scalajs.dom.document
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLInputElement
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

import scala.scalajs.js.Dynamic.literal

class TextFieldSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onChange function when input is changed" in {
    //given
    val onChange = mockFunction[String, Unit]
    val props = TextFieldProps("test text", onChange)
    domRender(<(TextField())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text

    //then
    onChange.expects(props.text)

    //when
    fireDomEvent(Simulate.change(inputElem, literal(target = inputElem)))
  }

  it should "call onEnter function when keyCode is Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = TextFieldProps("test text", _ => (), onEnter = onEnter)
    domRender(<(TextField())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text

    //then
    onEnter.expects()

    //when
    fireDomEvent(Simulate.keyDown(inputElem, literal(keyCode = KeyCode.Enter)))
  }

  it should "not call onEnter function when keyCode is other than Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = TextFieldProps("test text", _ => (), onEnter = onEnter)
    domRender(<(TextField())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text

    //then
    onEnter.expects().never()

    //when
    fireDomEvent(Simulate.keyDown(inputElem, literal(keyCode = KeyCode.Up)))
  }

  it should "render correct props" in {
    //given
    val props = TextFieldProps(
      "test text",
      onChange = _ => (),
      className = Some("test-class"),
      placeholder = Some("test-placeholder"),
      readOnly = true
    )

    //when
    val result = shallowRender(<(TextField())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.input(
      ^("readOnly") := props.readOnly,
      ^.`type` := "text",
      props.className.map(^.className := _),
      props.placeholder.map(^.placeholder := _),
      ^.value := props.text
    )())
  }

  it should "focus input element if requestFocus = true" in {
    //given
    val props = TextFieldProps("new test text", onChange = _ => (), requestFocus = true)

    //when
    domRender(<(TextField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.value shouldBe props.text
  }

  it should "focus input element if requestFocus changed from false to true" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    domRender(<(TextField())(^.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => (), requestFocus = true)
    props should not be prevProps
    domContainer.querySelector("input") should not be document.activeElement

    //when
    domRender(<(TextField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.value shouldBe props.text
  }

  it should "not focus input element if requestFocus not changed" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    domRender(<(TextField())(^.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => ())
    props should not be prevProps

    //when
    domRender(<(TextField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem should not be document.activeElement
    inputElem.value shouldBe props.text
  }

  it should "select text if requestSelect = true" in {
    //given
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)

    //when
    domRender(<(TextField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe props.text.length
  }

  it should "select text if requestSelect changed from false to true" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    domRender(<(TextField())(^.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)
    props should not be prevProps

    //when
    domRender(<(TextField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe props.text.length
  }

  it should "not select text if requestSelect not changed" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    domRender(<(TextField())(^.wrapped := prevProps)())
    val props = TextFieldProps("new test text", onChange = _ => ())
    props should not be prevProps

    //when
    domRender(<(TextField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.text
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe 0
  }
}
