package scommons.client.ui.popup

import scommons.client.ui.popup.InputPopup._
import scommons.client.ui.{Buttons, TextFieldProps}
import scommons.react.test._

class InputPopupSpec extends TestSpec with TestRendererUtils {

  InputPopup.modalComp = mockUiComponent("Modal")
  InputPopup.textFieldComp = mockUiComponent("TextField")

  it should "call onCancel when cancel command" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = getInputPopupProps("Test message", onCancel = onCancel)
    val component = testRender(<(InputPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.CANCEL.command)
  }

  it should "call onOk with initial value when ok command" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = testRender(<(InputPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onOk.expects(props.initialValue)

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.OK.command)
  }

  it should "call onOk with new value when ok command" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val renderer = createTestRenderer(<(InputPopup())(^.wrapped := props)())
    val newValue = "new value"
    findComponentProps(renderer.root, textFieldComp).onChange(newValue)

    //then
    onOk.expects(newValue)

    //when
    findComponentProps(renderer.root, modalComp).actions.onCommand(_ => ())(Buttons.OK.command)
  }

  it should "call onOk with initial value when onEnter" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val comp = testRender(<(InputPopup())(^.wrapped := props)())

    //then
    onOk.expects(props.initialValue)

    //when
    findComponentProps(comp, textFieldComp).onEnter()
  }

  it should "call onOk with new value when onEnter" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val renderer = createTestRenderer(<(InputPopup())(^.wrapped := props)())
    val newValue = "new value"
    findComponentProps(renderer.root, textFieldComp).onChange(newValue)

    //then
    onOk.expects(newValue)

    //when
    findComponentProps(renderer.root, textFieldComp).onEnter()
  }

  it should "enable OK command when new value is non-emtpy" in {
    //given
    val props = getInputPopupProps("Test message")
    val renderer = createTestRenderer(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.root
    val newValue = "new value"

    //when
    findComponentProps(comp, textFieldComp).onChange(newValue)

    //then
    val updatedComp = renderer.root
    val textProps = findComponentProps(updatedComp, textFieldComp)
    textProps.text shouldBe newValue

    val modalProps = findComponentProps(updatedComp, modalComp)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
  }

  it should "disable OK command when new value is emtpy" in {
    //given
    val props = getInputPopupProps("Test message", initialValue = "initial value")
    val renderer = createTestRenderer(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.root
    val prevTextProps = findComponentProps(comp, textFieldComp)
    val newValue = ""

    //when
    prevTextProps.onChange(newValue)

    //then
    val updatedComp = renderer.root
    val textProps = findComponentProps(updatedComp, textFieldComp)
    textProps.text shouldBe newValue

    val modalProps = findComponentProps(updatedComp, modalComp)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)
  }

  it should "render component with empty initial value" in {
    //given
    val props = getInputPopupProps("Test message", placeholder = Some("test placeholder"))
    val component = <(InputPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

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
    val result = testRender(component)

    //then
    assertInputPopup(result, props)
  }

  it should "set requestFocus when onOpen" in {
    //given
    val props = getInputPopupProps("Test message")
    val renderer = createTestRenderer(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)
    val textProps = findComponentProps(comp, textFieldComp)
    textProps.requestFocus shouldBe false
    textProps.requestSelect shouldBe false

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.root
    val updatedTextProps = findComponentProps(updatedComp, textFieldComp)
    updatedTextProps.requestFocus shouldBe true
    updatedTextProps.requestSelect shouldBe true
  }

  private def getInputPopupProps(message: String,
                                 onOk: String => Unit = _ => (),
                                 onCancel: () => Unit = () => (),
                                 placeholder: Option[String] = None,
                                 initialValue: String = ""): InputPopupProps = InputPopupProps(
    message = message,
    onOk = onOk,
    onCancel = onCancel,
    placeholder = placeholder,
    initialValue = initialValue
  )

  private def assertInputPopup(result: TestInstance, props: InputPopupProps): Unit = {
    val actionCommands =
      if (props.initialValue.nonEmpty) Set(Buttons.OK.command, Buttons.CANCEL.command)
      else Set(Buttons.CANCEL.command)

    assertTestComponent(result, modalComp)({
      case ModalProps(header, buttons, actions, _, onClose, closable, _) =>
        header shouldBe None
        buttons shouldBe List(Buttons.OK, Buttons.CANCEL)
        actions.enabledCommands shouldBe actionCommands
        onClose shouldBe props.onCancel
        closable shouldBe true
    }, inside(_) { case List(modalChild) =>
      assertNativeComponent(modalChild, <.div(^.className := "row-fluid")(), inside(_) { case List(p, div) =>
        assertNativeComponent(p, <.p()(props.message))
        assertNativeComponent(div, <.div(^.className := "control-group")(), inside(_) { case List(textField) =>
          assertTestComponent(textField, textFieldComp) {
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
