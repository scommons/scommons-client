package scommons.client.ui

import io.github.shogowada.scalajs.reactjs.ReactDOM
import io.github.shogowada.scalajs.reactjs.events.FormSyntheticEvent
import org.scalajs.dom.document
import org.scalajs.dom.raw.HTMLInputElement
import scommons.client.ui.ImageCheckBoxSpec.FormSyntheticEventMock
import scommons.react.test.TestSpec
import scommons.react.test.dom.raw.ReactTestUtils
import scommons.react.test.dom.raw.ReactTestUtils._
import scommons.react.test.dom.util.TestDOMUtils
import scommons.react.test.util.ShallowRendererUtils

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

class ImageCheckBoxSpec extends TestSpec with ShallowRendererUtils with TestDOMUtils {

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
    val comp = renderIntoDocument(<(ImageCheckBox())(^.wrapped := props)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.checked shouldBe true

    //then
    onChange.expects(props.value.next)

    //when
    ReactTestUtils.Simulate.change(inputElem, js.Dynamic.literal(target = inputElem))
  }

  it should "render correct props" in {
    //given
    val props = getImageCheckBoxProps()
    val component = <(ImageCheckBox())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

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

  it should "reset indeterminate on input element if value prop changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Indeterminate)
    val comp = renderIntoDocument(<(ImageCheckBox())(^.wrapped := prevProps)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.indeterminate shouldBe true
    inputElem.checked shouldBe false
    
    val props = getImageCheckBoxProps(TriState.Selected)
    val containerElement = findReactElement(comp).parentNode
    props should not be prevProps

    //when
    ReactDOM.render(<(ImageCheckBox())(^.wrapped := props)(), containerElement)

    //then
    val updatedElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    updatedElem.indeterminate shouldBe false
    updatedElem.checked shouldBe true
  }

  it should "not reset indeterminate on input element if value prop not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Indeterminate, text = "Old Text")
    val comp = renderIntoDocument(<(ImageCheckBox())(^.wrapped := prevProps)())
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem.indeterminate shouldBe true
    inputElem.checked shouldBe false

    val props = getImageCheckBoxProps(TriState.Indeterminate, text = "New Text")
    val containerElement = findReactElement(comp).parentNode
    props should not be prevProps

    //when
    ReactDOM.render(<(ImageCheckBox())(^.wrapped := props)(), containerElement)

    //then
    val updatedElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    updatedElem.indeterminate shouldBe true
    updatedElem.checked shouldBe false
  }
  
  it should "focus input element if requestFocus prop changed from false to true" in {
    //given
    val prevProps = getImageCheckBoxProps()
    val comp = renderIntoDocument(<(ImageCheckBox())(^.wrapped := prevProps)())
    val props = getImageCheckBoxProps(requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(ImageCheckBox())(^.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem shouldBe document.activeElement
    inputElem.checked shouldBe true

    //cleanup
    document.body.removeChild(containerElement)
  }

  it should "not focus input element if requestFocus prop not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Deselected, requestFocus = true)
    val comp = renderIntoDocument(<(ImageCheckBox())(^.wrapped := prevProps)())
    val props = getImageCheckBoxProps(TriState.Selected, requestFocus = true)
    val containerElement = findReactElement(comp).parentNode
    document.body.appendChild(containerElement)
    props should not be prevProps

    //when
    ReactDOM.render(<(ImageCheckBox())(^.wrapped := props)(), containerElement)

    //then
    val inputElem = findRenderedDOMComponentWithTag(comp, "input").asInstanceOf[HTMLInputElement]
    inputElem should not be document.activeElement
    inputElem.checked shouldBe true

    //cleanup
    document.body.removeChild(containerElement)
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
