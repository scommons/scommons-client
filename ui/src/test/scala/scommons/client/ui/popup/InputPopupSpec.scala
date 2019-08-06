package scommons.client.ui.popup

import scommons.client.ui.{Buttons, TextField, TextFieldProps}
import scommons.react.test.TestSpec
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class InputPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "call onCancel when cancel command" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = getInputPopupProps("Test message", onCancel = onCancel)
    val component = shallowRender(<(InputPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.CANCEL.command)
  }

  it should "call onOk with initial value when ok command" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = shallowRender(<(InputPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onOk.expects(props.initialValue)

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.OK.command)
  }

  it should "call onOk with new value when ok command" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := props)())
    val newValue = "new value"
    findComponentProps(renderer.getRenderOutput(), TextField).onChange(newValue)

    //then
    onOk.expects(newValue)

    //when
    findComponentProps(renderer.getRenderOutput(), Modal).actions.onCommand(_ => ())(Buttons.OK.command)
  }

  it should "call onOk with initial value when onEnter" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val comp = shallowRender(<(InputPopup())(^.wrapped := props)())

    //then
    onOk.expects(props.initialValue)

    //when
    findComponentProps(comp, TextField).onEnter()
  }

  it should "call onOk with new value when onEnter" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := props)())
    val newValue = "new value"
    findComponentProps(renderer.getRenderOutput(), TextField).onChange(newValue)

    //then
    onOk.expects(newValue)

    //when
    findComponentProps(renderer.getRenderOutput(), TextField).onEnter()
  }

  it should "enable OK command when new value is non-emtpy" in {
    //given
    val props = getInputPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val newValue = "new value"

    //when
    findComponentProps(comp, TextField).onChange(newValue)

    //then
    val updatedComp = renderer.getRenderOutput()
    val textProps = findComponentProps(updatedComp, TextField)
    textProps.text shouldBe newValue

    val modalProps = findComponentProps(updatedComp, Modal)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
  }

  it should "disable OK command when new value is emtpy" in {
    //given
    val props = getInputPopupProps("Test message", initialValue = "initial value")
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val prevTextProps = findComponentProps(comp, TextField)
    val newValue = ""

    //when
    prevTextProps.onChange(newValue)

    //then
    val updatedComp = renderer.getRenderOutput()
    val textProps = findComponentProps(updatedComp, TextField)
    textProps.text shouldBe newValue

    val modalProps = findComponentProps(updatedComp, Modal)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)
  }

  it should "render component with empty initial value" in {
    //given
    val props = getInputPopupProps("Test message", placeholder = Some("test placeholder"))
    val component = <(InputPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertInputPopup(result, props)
  }

  it should "render component with non-empty initial value" in {
    //given
    val props = getInputPopupProps("Test message",
      placeholder = Some("test placeholder"),
      initialValue = "initial value"
    )
    val component = <(InputPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertInputPopup(result, props)
  }

  it should "set requestFocus when onOpen" in {
    //given
    val props = getInputPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    val textProps = findComponentProps(comp, TextField)
    textProps.requestFocus shouldBe false
    textProps.requestSelect shouldBe false

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.getRenderOutput()
    val updatedTextProps = findComponentProps(updatedComp, TextField)
    updatedTextProps.requestFocus shouldBe true
    updatedTextProps.requestSelect shouldBe true
  }

  private def getInputPopupProps(message: String,
                                 onOk: String => Unit = _ => (),
                                 onCancel: () => Unit = () => (),
                                 placeholder: Option[String] = None,
                                 initialValue: String = "",
                                 show: Boolean = true): InputPopupProps = InputPopupProps(
    show = show,
    message = message,
    onOk = onOk,
    onCancel = onCancel,
    placeholder = placeholder,
    initialValue = initialValue
  )

  private def assertInputPopup(result: ShallowInstance, props: InputPopupProps): Unit = {
    val actionCommands =
      if (props.initialValue.nonEmpty) Set(Buttons.OK.command, Buttons.CANCEL.command)
      else Set(Buttons.CANCEL.command)

    assertComponent(result, Modal)({
      case ModalProps(show, header, buttons, actions, _, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe List(Buttons.OK, Buttons.CANCEL)
        actions.enabledCommands shouldBe actionCommands
        onClose shouldBe props.onCancel
        closable shouldBe true
    }, { case List(modalChild) =>
      assertNativeComponent(modalChild, <.div(^.className := "row-fluid")(), { case List(p, div) =>
        assertNativeComponent(p, <.p()(props.message))
        assertNativeComponent(div, <.div(^.className := "control-group")(), { case List(textField) =>
          assertComponent(textField, TextField) {
            case TextFieldProps(text, _, requestFocus, requestSelect, className, placeholder, _, readOnly) =>
              text shouldBe props.initialValue
              requestFocus shouldBe false
              requestSelect shouldBe false
              className shouldBe Some("span12")
              placeholder shouldBe props.placeholder
              readOnly shouldBe false
          }
        })
      })
    })
  }
}
