package scommons.client.ui

import org.scalajs.dom.ext.KeyCode
import scommons.client.ui.TextFieldSpec._
import scommons.react.test._

import scala.scalajs.js
import scala.scalajs.js.Dynamic.literal
import scala.scalajs.js.annotation.JSExportAll

class TextFieldSpec extends TestSpec with TestRendererUtils {

  it should "call onChange function when input is changed" in {
    //given
    val onChange = mockFunction[String, Unit]
    val props = TextFieldProps("test text", onChange)
    val comp = testRender(<(TextField())(^.wrapped := props)())
    comp.props.selectDynamic("value") shouldBe props.text
    val newValue = "new text"

    //then
    onChange.expects(newValue)

    //when
    comp.props.onChange(literal(target = literal(value = newValue)))
  }

  it should "call onEnter function when keyCode is Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = TextFieldProps("test text", _ => (), onEnter = onEnter)
    val comp = testRender(<(TextField())(^.wrapped := props)())
    comp.props.selectDynamic("value") shouldBe props.text

    //then
    onEnter.expects()

    //when
    comp.props.onKeyDown(literal(keyCode = KeyCode.Enter))
  }

  it should "not call onEnter function when keyCode is other than Enter" in {
    //given
    val onEnter = mockFunction[Unit]
    val props = TextFieldProps("test text", _ => (), onEnter = onEnter)
    val comp = testRender(<(TextField())(^.wrapped := props)())
    comp.props.selectDynamic("value") shouldBe props.text

    //then
    onEnter.expects().never()

    //when
    comp.props.onKeyDown(literal(keyCode = KeyCode.Up))
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
    val result = testRender(<(TextField())(^.wrapped := props)())

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
    val inputMock = mock[InputMock]
    (inputMock.value _).expects().returning("")

    //then
    (inputMock.focus _).expects()

    //when
    testRender(<(TextField())(^.wrapped := props)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
  }

  it should "focus input element if requestFocus changed from false to true" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    val inputMock = mock[InputMock]
    (inputMock.value _).expects().returning("")
    val renderer = createTestRenderer(<(TextField())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    val props = TextFieldProps("new test text", onChange = _ => (), requestFocus = true)
    props should not be prevProps

    //then
    (inputMock.focus _).expects()

    //when
    TestRenderer.act { () =>
      renderer.update(<(TextField())(^.wrapped := props)())
    }
  }

  it should "not focus input element if requestFocus not changed" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    val inputMock = mock[InputMock]
    (inputMock.value _).expects().returning("")
    val renderer = createTestRenderer(<(TextField())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    val props = TextFieldProps("new test text", onChange = _ => ())
    props should not be prevProps

    //then
    (inputMock.focus _).expects().never()

    //when
    TestRenderer.act { () =>
      renderer.update(<(TextField())(^.wrapped := props)())
    }
  }

  it should "select text if requestSelect = true" in {
    //given
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)
    val inputMock = mock[InputMock]
    (inputMock.value _).expects().returning(props.text)

    //then
    (inputMock.setSelectionRange _).expects(0, props.text.length)

    //when
    testRender(<(TextField())(^.wrapped := props)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
  }

  it should "select text if requestSelect changed from false to true" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => ())
    val inputMock = mock[InputMock]
    (inputMock.value _).expects().returning(prevProps.text)
    val renderer = createTestRenderer(<(TextField())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)
    props should not be prevProps

    //then
    (inputMock.value _).expects().returning(props.text)
    (inputMock.setSelectionRange _).expects(0, props.text.length)

    //when
    TestRenderer.act { () =>
      renderer.update(<(TextField())(^.wrapped := props)())
    }
  }

  it should "not select text if requestSelect not changed" in {
    //given
    val prevProps = TextFieldProps("test text", onChange = _ => (), requestSelect = true)
    val inputMock = mock[InputMock]
    (inputMock.value _).expects().returning(prevProps.text)
    (inputMock.setSelectionRange _).expects(0, prevProps.text.length)
    val renderer = createTestRenderer(<(TextField())(^.wrapped := prevProps)(), { el =>
      if (el.`type` == "input".asInstanceOf[js.Any]) inputMock.asInstanceOf[js.Any]
      else null
    })
    val props = TextFieldProps("new test text", onChange = _ => (), requestSelect = true)
    props should not be prevProps

    //then
    (inputMock.value _).expects().never()
    (inputMock.setSelectionRange _).expects(*, *).never()

    //when
    TestRenderer.act { () =>
      renderer.update(<(TextField())(^.wrapped := props)())
    }
  }
}

object TextFieldSpec {

  @JSExportAll
  trait InputMock {

    def value: String
    def setSelectionRange(start: Int, end: Int): Unit
    def focus(): Unit
  }
}
