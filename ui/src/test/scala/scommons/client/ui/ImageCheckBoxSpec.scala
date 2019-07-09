package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import scommons.client.ui.ImageCheckBoxSpec.FormSyntheticEventMock
import scommons.react.test.TestSpec
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

class ImageCheckBoxSpec extends TestSpec
  with ShallowRendererUtils
  with TestDOMUtils {

  it should "call stopPropagation and preventDefault if readOnly when onChange" in {
    //given
    val f = mockFunction[Unit]
    val event = mock[FormSyntheticEventMock]
    
    //then
    (event.stopPropagation _).expects()
    (event.preventDefault _).expects()
    f.expects().never()
    
    //when
    ImageCheckBox.onChange(readOnly = true)(f)(event.asInstanceOf[FormSyntheticEvent[HTMLInputElement]])
  }
  
  it should "call callback function if not readOnly when onChange" in {
    //given
    val f = mockFunction[Unit]
    val event = mock[FormSyntheticEventMock]
    
    //then
    (event.stopPropagation _).expects().never()
    (event.preventDefault _).expects().never()
    f.expects()
    
    //when
    ImageCheckBox.onChange(readOnly = false)(f)(event.asInstanceOf[FormSyntheticEvent[HTMLInputElement]])
  }

  it should "call onChange function when input is changed" in {
    //given
    val onChange = mockFunction[TriState, Unit]
    val props = getImageCheckBoxProps(onChange = onChange)
    domRender(<(ImageCheckBox())(^.wrapped := props)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.checked shouldBe true

    //then
    onChange.expects(props.value.next)

    //when
    fireDomEvent(Simulate.change(inputElem, js.Dynamic.literal(target = inputElem)))
  }

  it should "render correct props" in {
    //given
    val props = getImageCheckBoxProps()

    //when
    val result = shallowRender(<(ImageCheckBox())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.label()(
      <.input(
        ^.`type` := "checkbox",
        ^.checked := TriState.isSelected(props.value)
      )(),
      <.img(^.className := props.image, ^.src := "")(),
      <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(props.text)
    ))
  }

  it should "reset indeterminate on input element if value changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Indeterminate)
    domRender(<(ImageCheckBox())(^.wrapped := prevProps)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.indeterminate shouldBe true
    inputElem.checked shouldBe false
    
    val props = getImageCheckBoxProps(TriState.Selected)
    props should not be prevProps

    //when
    domRender(<(ImageCheckBox())(^.wrapped := props)())

    //then
    val updatedElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    updatedElem.indeterminate shouldBe false
    updatedElem.checked shouldBe true
  }

  it should "not reset indeterminate on input element if value not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Indeterminate, text = "Old Text")
    domRender(<(ImageCheckBox())(^.wrapped := prevProps)())
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem.indeterminate shouldBe true
    inputElem.checked shouldBe false

    val props = getImageCheckBoxProps(TriState.Indeterminate, text = "New Text")
    props should not be prevProps

    //when
    domRender(<(ImageCheckBox())(^.wrapped := props)())

    //then
    val updatedElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    updatedElem.indeterminate shouldBe true
    updatedElem.checked shouldBe false
  }
  
  it should "focus input element if requestFocus = true" in {
    //given
    val props = getImageCheckBoxProps(requestFocus = true)

    //when
    domRender(<(ImageCheckBox())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.checked shouldBe true
  }

  it should "focus input element if requestFocus changed from false to true" in {
    //given
    val prevProps = getImageCheckBoxProps()
    domRender(<(ImageCheckBox())(^.wrapped := prevProps)())
    val props = getImageCheckBoxProps(requestFocus = true)
    props should not be prevProps
    domContainer.querySelector("input") should not be document.activeElement

    //when
    domRender(<(ImageCheckBox())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.checked shouldBe true
  }

  it should "not focus input element if requestFocus not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Deselected)
    domRender(<(ImageCheckBox())(^.wrapped := prevProps)())
    val props = getImageCheckBoxProps(TriState.Selected)
    props should not be prevProps

    //when
    domRender(<(ImageCheckBox())(^.wrapped := props)())

    //then
    val inputElem = domContainer.querySelector("input").asInstanceOf[HTMLInputElement]
    inputElem should not be document.activeElement
    inputElem.checked shouldBe true
  }
  
  private def getImageCheckBoxProps(value: TriState = TriState.Selected,
                                    image: String = ButtonImagesCss.folder,
                                    text: String = "Test",
                                    onChange: (TriState) => Unit = _ => (),
                                    requestFocus: Boolean = false,
                                    readOnly: Boolean = false): ImageCheckBoxProps = {
    
    ImageCheckBoxProps(
      value = value,
      image = image,
      text = text,
      onChange = onChange,
      requestFocus = requestFocus,
      readOnly = readOnly
    )
  }
}

object ImageCheckBoxSpec {

  @JSExportAll
  trait FormSyntheticEventMock {

    def stopPropagation(): Unit
    def preventDefault(): Unit
  }
}
