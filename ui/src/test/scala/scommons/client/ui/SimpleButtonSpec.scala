package scommons.client.ui

import scommons.client.ui.SimpleButtonSpec.ButtonMock
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportAll

class SimpleButtonSpec extends TestSpec with TestRendererUtils {

  it should "call onClick when click on button" in {
    //given
    val onClick = mockFunction[Unit]
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = onClick)
    val comp = testRender(<(SimpleButton())(^.wrapped := props)())

    //then
    onClick.expects()

    //when
    comp.props.onClick(null)
  }

  it should "render normal button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => ())

    //when
    val result = testRender(<(SimpleButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn"
    )(data.text))
  }

  it should "render disabled button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, onClick = () => (), disabled = true)

    //when
    val result = testRender(<(SimpleButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn",
      ^.disabled := true
    )(data.text))
  }

  it should "render primary button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text", primary = true)
    val props = SimpleButtonProps(data, onClick = () => ())

    //when
    val result = testRender(<(SimpleButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary"
    )(data.text))
  }

  it should "render primary disabled button" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text", primary = true)
    val props = SimpleButtonProps(data, onClick = () => (), disabled = true)

    //when
    val result = testRender(<(SimpleButton())(^.wrapped := props)())

    //then
    assertNativeComponent(result, <.button(
      ^.`type` := "button",
      ^.className := "btn btn-primary",
      ^.disabled := true
    )(data.text))
  }

  it should "focus button element if requestFocus = true" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val props = SimpleButtonProps(data, () => (), requestFocus = true)
    val buttonMock = mock[ButtonMock]

    //then
    (buttonMock.focus _).expects()

    //when
    testRender(<(SimpleButton())(^.wrapped := props)(), { el =>
      if (el.`type` == "button".asInstanceOf[js.Any]) buttonMock.asInstanceOf[js.Any]
      else null
    })
  }

  it should "focus button element if requestFocus changed from false to true" in {
    //given
    val data = SimpleButtonData("test-command", "Test Text")
    val prevProps = SimpleButtonProps(data, () => ())
    val buttonMock = mock[ButtonMock]
    val renderer = createTestRenderer(<(SimpleButton())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "button".asInstanceOf[js.Any]) buttonMock.asInstanceOf[js.Any]
      else null
    })
    val props = SimpleButtonProps(data, () => (), requestFocus = true)
    props should not be prevProps

    //then
    (buttonMock.focus _).expects()

    //when
    TestRenderer.act { () =>
      renderer.update(<(SimpleButton())(^.wrapped := props)())
    }
  }

  it should "not focus button element if requestFocus not changed" in {
    //given
    val prevProps = SimpleButtonProps(SimpleButtonData("test-command", "Text"), () => ())
    val buttonMock = mock[ButtonMock]
    val renderer = createTestRenderer(<(SimpleButton())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "button".asInstanceOf[js.Any]) buttonMock.asInstanceOf[js.Any]
      else null
    })
    val props = SimpleButtonProps(SimpleButtonData("test-command", "New Text"), () => ())
    props should not be prevProps

    //then
    (buttonMock.focus _).expects().never()

    //when
    TestRenderer.act { () =>
      renderer.update(<(SimpleButton())(^.wrapped := props)())
    }
  }
}

object SimpleButtonSpec {

  @JSExportAll
  trait ButtonMock {

    def focus(): Unit
  }
}
