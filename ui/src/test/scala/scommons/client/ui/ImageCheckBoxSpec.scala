package scommons.client.ui

import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class ImageCheckBoxSpec extends TestSpec with TestRendererUtils {
  
  it should "not call onChange if readOnly when input is changed" in {
    //given
    val onChange = mockFunction[TriState, Unit]
    val props = getImageCheckBoxProps(onChange = onChange, readOnly = true)
    val comp = testRender(<(ImageCheckBox())(^.wrapped := props)())
    val inputComp = findComponents(comp, <.input.name).head
    inputComp.props.checked shouldBe true
    val stopPropagationMock = mockFunction[Unit]
    val preventDefaultMock = mockFunction[Unit]
    val event = literal("stopPropagation" -> stopPropagationMock, "preventDefault" -> preventDefaultMock)

    //then
    stopPropagationMock.expects()
    preventDefaultMock.expects()
    onChange.expects(*).never()

    //when
    inputComp.props.onChange(event)
  }

  it should "call onChange callback when input is changed" in {
    //given
    val onChange = mockFunction[TriState, Unit]
    val props = getImageCheckBoxProps(onChange = onChange)
    val comp = testRender(<(ImageCheckBox())(^.wrapped := props)())
    val inputComp = findComponents(comp, <.input.name).head
    inputComp.props.checked shouldBe true
    val stopPropagationMock = mockFunction[Unit]
    val preventDefaultMock = mockFunction[Unit]
    val event = literal("stopPropagation" -> stopPropagationMock, "preventDefault" -> preventDefaultMock)

    //then
    stopPropagationMock.expects().never()
    preventDefaultMock.expects().never()
    onChange.expects(props.value.next)

    //when
    inputComp.props.onChange(event)
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
    val inputMock = literal()
    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock
      else null
    })
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe false
    
    val props = getImageCheckBoxProps(TriState.Selected)
    props should not be prevProps
    inputMock.indeterminate shouldBe true

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    inputMock.indeterminate shouldBe false
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe true
  }

  it should "not reset indeterminate on input element if value not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Indeterminate, text = "Old Text")
    val inputMock = literal()
    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock
      else null
    })
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe false

    val props = getImageCheckBoxProps(TriState.Indeterminate, text = "New Text")
    props should not be prevProps
    inputMock.indeterminate shouldBe true

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    inputMock.indeterminate shouldBe true
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe false
  }
  
  it should "focus input element if requestFocus = true" in {
    //given
    val props = getImageCheckBoxProps(requestFocus = true)
    val focusMock = mockFunction[Unit]
    val inputMock = literal("focus" -> focusMock)
    
    //then
    focusMock.expects()

    //when
    val comp = testRender(<(ImageCheckBox())(^.wrapped := props)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock
      else null
    })

    //then
    inputMock.indeterminate shouldBe false
    findComponents(comp, <.input.name).head.props.checked shouldBe true
  }

  it should "focus input element if requestFocus changed from false to true" in {
    //given
    val prevProps = getImageCheckBoxProps()
    val focusMock = mockFunction[Unit]
    val inputMock = literal("focus" -> focusMock)
    
    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock
      else null
    })
    val props = getImageCheckBoxProps(requestFocus = true)
    props should not be prevProps

    //then
    focusMock.expects()

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    inputMock.indeterminate shouldBe false
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe true
  }

  it should "not focus input element if requestFocus not changed" in {
    //given
    val prevProps = getImageCheckBoxProps(TriState.Deselected)
    val focusMock = mockFunction[Unit]
    val inputMock = literal("focus" -> focusMock)

    val renderer = createTestRenderer(<(ImageCheckBox())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock
      else null
    })
    val props = getImageCheckBoxProps(TriState.Selected)
    props should not be prevProps
    inputMock.indeterminate shouldBe false

    //then
    focusMock.expects().never()

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageCheckBox())(^.wrapped := props)())
    }

    //then
    inputMock.indeterminate shouldBe false
    findComponents(renderer.root, <.input.name).head.props.checked shouldBe true
  }
  
  private def getImageCheckBoxProps(value: TriState = TriState.Selected,
                                    image: String = ButtonImagesCss.folder,
                                    text: String = "Test",
                                    onChange: TriState => Unit = _ => (),
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
