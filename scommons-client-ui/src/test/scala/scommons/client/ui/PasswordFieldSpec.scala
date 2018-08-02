package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import org.scalajs.dom.document
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLInputElement
import scommons.client.test.TestSpec
import scommons.client.test.raw.ReactTestUtils
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.util.TestDOMUtils._

import scala.scalajs.js

class PasswordFieldSpec extends TestSpec {

  it should "call onChange function when input is changed" in {
    //given
    val onChange = mockFunction[String, Unit]
    val props = PasswordFieldProps("test password", onChange)
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password

    //then
    onChange.expects(props.password)

    //when
    ReactTestUtils.Simulate.change(inputElem, js.Dynamic.literal(target = inputElem))
  }

  it should "call onEnter function when keyCode is Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = PasswordFieldProps("test password", _ => (), onEnter = onEnter)
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password

    //then
    onEnter.expects()

    //when
    ReactTestUtils.Simulate.keyDown(inputElem, js.Dynamic.literal(keyCode = KeyCode.Enter))
  }

  it should "not call onEnter function when keyCode is other than Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = PasswordFieldProps("test password", _ => (), onEnter = onEnter)
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password

    //then
    onEnter.expects().never()

    //when
    ReactTestUtils.Simulate.keyDown(inputElem, js.Dynamic.literal(keyCode = KeyCode.Up))
  }

  it should "render correct props" in {
    //given
    val props = PasswordFieldProps(
      "test password",
      onChange = _ => (),
      className = Some("test-class"),
      placeholder = Some("test-placeholder"),
      readOnly = true
    )
    val component = <(PasswordField())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, <.input(
      ^("readOnly") := props.readOnly,
      ^.`type` := "password",
      props.className.map(^.className := _),
      props.placeholder.map(^.placeholder := _),
      ^.value := props.password
    )())
  }

  it should "focus input element if requestFocus prop changed from false to true" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => ())
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(PasswordField())(^.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.value shouldBe props.password

    //cleanup
    document.body.removeChild(containerElement)
  }

  it should "not focus input element if requestFocus prop not changed" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => (), requestFocus = true)
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(PasswordField())(^.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem should not be document.activeElement
    inputElem.value shouldBe props.password

    //cleanup
    document.body.removeChild(containerElement)
  }

  it should "select password if requestSelect prop changed from false to true" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => ())
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestSelect = true)
    val containerElement = findReactElement(comp).parentNode
    props should not be prevProps

    //when
    ReactDOM.render(<(PasswordField())(^.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe props.password.length
  }

  it should "not select password if requestSelect prop not changed" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => (), requestSelect = true)
    val comp = renderIntoDocument(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestSelect = true)
    val containerElement = findReactElement(comp).parentNode
    props should not be prevProps

    //when
    ReactDOM.render(<(PasswordField())(^.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe 0
  }
}
