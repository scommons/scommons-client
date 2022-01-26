package scommons.client.ui

import scommons.client.ui.ButtonImagesCss._
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal

class ImageButtonSpec extends TestSpec with TestRendererUtils {

  it should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")
    val comp = testRender(<(ImageButton())(^.wrapped := ImageButtonProps(data, onClick))())

    //then
    onClick.expects()

    //when
    comp.props.onClick(null)
  }

  it should "render button with text" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "button with text")

    //when
    val result = testRender(<(ImageButton())(^.wrapped := ImageButtonProps(data, () => ()))())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render button with title" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test title")
    val props = ImageButtonProps(data, () => (), showTextAsTitle = true)

    //when
    val result = testRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ", ^.title := s"${data.text}")(
        <.img(^.className := s"${data.image}", ^.src := "")()
      )
    )
  }

  it should "render disabled button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Disabled")
    val props = ImageButtonProps(data, () => (), disabled = true)

    //when
    val result = testRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ", ^.disabled := true)(
        <.img(^.className := s"${data.disabledImage}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render primary button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Primary", primary = true)
    val props = ImageButtonProps(data, () => ())

    //when
    val result = testRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn btn-primary")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "render non-primary button" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "Primary")
    val props = ImageButtonProps(data, () => ())

    //when
    val result = testRender(<(ImageButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result,
      <.button(^.`type` := "button", ^.className := "btn ")(
        <.img(^.className := s"${data.image}", ^.src := "")(),
        <.span(^.style := Map("paddingLeft" -> "3px", "verticalAlign" -> "middle"))(data.text)
      )
    )
  }

  it should "focus button element if requestFocus = true" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val props = ImageButtonProps(data, () => (), requestFocus = true)
    val focusMock = mockFunction[Unit]
    val buttonMock = literal("focus" -> focusMock)

    //then
    focusMock.expects()
    
    //when
    testRender(<(ImageButton())(^.wrapped := props)(), { el =>
      if (el.`type` == "button".asInstanceOf[js.Any]) buttonMock
      else null
    })
  }

  it should "focus button element if requestFocus changed from false to true" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => ())
    val focusMock = mockFunction[Unit]
    val buttonMock = literal("focus" -> focusMock)
    val renderer = createTestRenderer(<(ImageButton())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "button".asInstanceOf[js.Any]) buttonMock
      else null
    })
    val props = ImageButtonProps(data, () => (), requestFocus = true)
    props should not be prevProps

    //then
    focusMock.expects()

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageButton())(^.wrapped := props)())
    }
  }

  it should "not focus button element if requestFocus not changed" in {
    //given
    val data = ImageButtonData("accept", accept, acceptDisabled, "test button")
    val prevProps = ImageButtonProps(data, () => ())
    val focusMock = mockFunction[Unit]
    val buttonMock = literal("focus" -> focusMock)
    val renderer = createTestRenderer(<(ImageButton())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "button".asInstanceOf[js.Any]) buttonMock
      else null
    })
    val props = ImageButtonProps(data, () => (), showTextAsTitle = true)
    props should not be prevProps

    //then
    focusMock.expects().never()

    //when
    TestRenderer.act { () =>
      renderer.update(<(ImageButton())(^.wrapped := props)())
    }
  }
}
