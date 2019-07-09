package scommons.client.ui

import org.scalajs.dom.document
import org.scalajs.dom.ext.KeyCode
import org.scalajs.dom.raw.HTMLInputElement
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

import scala.scalajs.js.Dynamic.literal

class PasswordFieldSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call onChange function when input is changed" in {
    //given
    val onChange = mockFunction[String, Unit]
    val props = PasswordFieldProps("test password", onChange)
    domRender(<(PasswordField())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password

    //then
    onChange.expects(props.password)

    //when
    fireDomEvent(Simulate.change(inputElem, literal(target = inputElem)))
  }

  it should "call onEnter function when keyCode is Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = PasswordFieldProps("test password", _ => (), onEnter = onEnter)
    domRender(<(PasswordField())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password

    //then
    onEnter.expects()

    //when
    fireDomEvent(Simulate.keyDown(inputElem, literal(keyCode = KeyCode.Enter)))
  }

  it should "not call onEnter function when keyCode is other than Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = PasswordFieldProps("test password", _ => (), onEnter = onEnter)
    domRender(<(PasswordField())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password

    //then
    onEnter.expects().never()

    //when
    fireDomEvent(Simulate.keyDown(inputElem, literal(keyCode = KeyCode.Up)))
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

    //when
    val result = shallowRender(<(PasswordField())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.input(
      ^("readOnly") := props.readOnly,
      ^.`type` := "password",
      props.className.map(^.className := _),
      props.placeholder.map(^.placeholder := _),
      ^.value := props.password
    )())
  }

  it should "focus input element if requestFocus = true" in {
    //given
    val props = PasswordFieldProps("new password", onChange = _ => (), requestFocus = true)

    //when
    domRender(<(PasswordField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.value shouldBe props.password
  }

  it should "focus input element if requestFocus changed from false to true" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => ())
    domRender(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestFocus = true)
    props should not be prevProps
    domContainer.querySelector("input") should not be document.activeElement

    //when
    domRender(<(PasswordField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.value shouldBe props.password
  }

  it should "not focus input element if requestFocus not changed" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => ())
    domRender(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => ())
    props should not be prevProps

    //when
    domRender(<(PasswordField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem should not be document.activeElement
    inputElem.value shouldBe props.password
  }

  it should "select password if requestSelect = true" in {
    //given
    val props = PasswordFieldProps("new password", onChange = _ => (), requestSelect = true)

    //when
    domRender(<(PasswordField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe props.password.length
  }

  it should "select password if requestSelect changed from false to true" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => ())
    domRender(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestSelect = true)
    props should not be prevProps

    //when
    domRender(<(PasswordField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe props.password.length
  }

  it should "not select password if requestSelect not changed" in {
    //given
    val prevProps = PasswordFieldProps("test password", onChange = _ => (), requestSelect = true)
    domRender(<(PasswordField())(^.wrapped := prevProps)())
    val props = PasswordFieldProps("new password", onChange = _ => (), requestSelect = true)
    props should not be prevProps

    //when
    domRender(<(PasswordField())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.value shouldBe props.password
    inputElem.selectionStart shouldBe 0
    inputElem.selectionEnd shouldBe 0
  }
}
