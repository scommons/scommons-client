package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.{Buttons, TextField, TextFieldProps}

class InputPopupSpec extends TestSpec {

  it should "call onCancel function when cancel command" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = getInputPopupProps("Test message", onCancel = onCancel)
    val component = shallowRender(<(InputPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(Buttons.CANCEL.command)
  }

  it should "call onOk function when ok command" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = shallowRender(<(InputPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onOk.expects(props.initialValue)

    //when
    modalProps.actions.onCommand(Buttons.OK.command)
  }

  it should "call onOk functions with new value when onEnter" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = shallowRender(<(InputPopup())(^.wrapped := props)())
    val textFieldProps = findComponentProps(component, TextField)
    val newValue = "new value"
    textFieldProps.onChange(newValue)

    //then
    onOk.expects(newValue)

    //when
    textFieldProps.onEnter()
  }

  it should "enable OK command when new value is non-emtpy" in {
    //given
    val props = getInputPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val prevTextProps = findComponentProps(comp, TextField)
    val newValue = "new value"

    //when
    prevTextProps.onChange(newValue)

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

  it should "reset requestFocus and value when componentWillReceiveProps" in {
    //given
    val prevProps = getInputPopupProps("Test message", initialValue = "some value")
    val renderer = createRenderer()
    renderer.render(<(InputPopup())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    val textProps = findComponentProps(comp, TextField)
    textProps.text shouldBe prevProps.initialValue
    textProps.requestFocus shouldBe false
    textProps.requestSelect shouldBe false
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.enabledCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    modalProps.onOpen()
    val compV2 = renderer.getRenderOutput()
    val textPropsV2 = findComponentProps(compV2, TextField)
    textPropsV2.requestFocus shouldBe true
    textPropsV2.requestSelect shouldBe true
    val props = getInputPopupProps("New message")

    //when
    renderer.render(<(InputPopup())(^.wrapped := props)())

    //then
    val compV3 = renderer.getRenderOutput()
    val modalPropsV3 = findComponentProps(compV3, Modal)
    modalPropsV3.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)

    val textPropsV3 = findComponentProps(compV3, TextField)
    textPropsV3.text shouldBe props.initialValue
    textPropsV3.requestFocus shouldBe false
    textPropsV3.requestSelect shouldBe false
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

  private def assertInputPopup(result: ComponentInstance, props: InputPopupProps): Unit = {
    val actionCommands =
      if (props.initialValue.nonEmpty) Set(Buttons.OK.command, Buttons.CANCEL.command)
      else Set(Buttons.CANCEL.command)

    assertComponent(result, Modal(), { modalProps: ModalProps =>
      inside(modalProps) { case ModalProps(show, header, buttons, actions, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe List(Buttons.OK, Buttons.CANCEL)
        actions.enabledCommands shouldBe actionCommands
        onClose shouldBe props.onCancel
        closable shouldBe true
      }
    }, { case List(modalChild) =>
      assertDOMComponent(modalChild, <.div(^.className := "row-fluid")(), { case List(p, div) =>
        assertDOMComponent(p, <.p()(props.message))
        assertDOMComponent(div, <.div(^.className := "control-group")(), { case List(textField) =>
          assertComponent(textField, TextField(), { textFieldProps: TextFieldProps =>
            inside(textFieldProps) {
              case TextFieldProps(text, _, requestFocus, requestSelect, className, placeholder, _) =>
                text shouldBe props.initialValue
                requestFocus shouldBe false
                requestSelect shouldBe false
                className shouldBe Some("span12")
                placeholder shouldBe props.placeholder
            }
          })
        })
      })
    })
  }
}
