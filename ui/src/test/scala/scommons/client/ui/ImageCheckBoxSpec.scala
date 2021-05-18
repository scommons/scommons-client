package scommons.client.ui

import scommons.client.ui.ImageCheckBoxSpec._
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

class ImageCheckBoxSpec extends TestSpec with TestRendererUtils {
  
  it should "not call onChange if readOnly when input is changed" in {
    //given
    val onChange = mockFunction[TriState, Unit]
    val props = getImageCheckBoxProps(onChange = onChange, readOnly = true)
    val comp = testRender(<(ImageCheckBox())(^.wrapped := props)())
    val inputComp = findComponents(comp, <.input.name).head
    inputComp.props.checked shouldBe true
    val event = mock[FormSyntheticEventMock]

    //then
    (event.stopPropagation _).expects()
    (event.preventDefault _).expects()
    onChange.expects(*).never()

    //when
    inputComp.props.onChange(event.asInstanceOf[js.Any])
  }

  it should "call onChange callback when input is changed" in {
    //given
    val onChange = mockFunction[TriState, Unit]
    val props = getImageCheckBoxProps(onChange = onChange)
    val comp = testRender(<(ImageCheckBox())(^.wrapped := props)())
    val inputComp = findComponents(comp, <.input.name).head
    inputComp.props.checked shouldBe true
    val event = mock[FormSyntheticEventMock]

    //then
    (event.stopPropagation _).expects().never()
    (event.preventDefault _).expects().never()
    onChange.expects(props.value.next)

    //when
    inputComp.props.onChange(event.asInstanceOf[js.Any])
  }

  it should "render correct props" in {
    //given
    val props = getImageCheckBoxProps()

    //when
    val result = testRender(<(ImageCheckBox())(^.wrapped := props)())

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
    val inputMock = mock[InputMock]
    (inputMock.indeterminate_= _).expects(true)

    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe false
    
    val props = getImageCheckBoxProps(TriState.Selected)
    props should not be prevProps

    //then
    (inputMock.indeterminate_= _).expects(false)

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe true
  }

  it should "not reset indeterminate on input element if value not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Indeterminate, text = "Old Text")
    val inputMock = mock[InputMock]
    (inputMock.indeterminate_= _).expects(true)

    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe false

    val props = getImageCheckBoxProps(TriState.Indeterminate, text = "New Text")
    props should not be prevProps

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe false
  }
  
  it should "focus input element if requestFocus = true" in {
    //given
    val props = getImageCheckBoxProps(requestFocus = true)
    val inputMock = mock[InputMock]
    (inputMock.indeterminate_= _).expects(false)
    
    //then
    (inputMock.focus _).expects()

    //when
    val comp = testRender(<(ImageCheckBox())(^.wrapped := props)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })

    //then
    findComponents(comp, <.input.name).head.props.checked shouldBe true
  }

  it should "focus input element if requestFocus changed from false to true" in {
    //given
    val prevProps = getImageCheckBoxProps()
    val inputMock = mock[InputMock]
    (inputMock.indeterminate_= _).expects(false)
    
    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    val props = getImageCheckBoxProps(requestFocus = true)
    props should not be prevProps

    //then
    (inputMock.focus _).expects()

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe true
  }

  it should "not focus input element if requestFocus not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Deselected)
    val inputMock = mock[InputMock]
    (inputMock.indeterminate_= _).expects(false)

    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    val props = getImageCheckBoxProps(TriState.Selected)
    props should not be prevProps

    //then
    (inputMock.focus _).expects().never()
    (inputMock.indeterminate_= _).expects(false)

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe true
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

  @JSExportAll
  trait InputMock {

    def indeterminate_=(value: Boolean): Unit
    def focus(): Unit
  }
}
