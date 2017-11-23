package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.popup.InputPopup.InputPopupState
import scommons.client.ui.{Buttons, TextField, TextFieldProps}

class InputPopupSpec extends FlatSpec
  with Matchers
  with Inside
  with ShallowRendererUtils
  with MockFactory {

  it should "call onCancel function when cancel command" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = getInputPopupProps("Test message", onCancel = onCancel)
    val component = shallowRender(E(InputPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(Buttons.CANCEL.command)
  }

  it should "call onOk function when ok command" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = shallowRender(E(InputPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onOk.expects(props.initialValue)

    //when
    modalProps.actions.onCommand(Buttons.OK.command)
  }

  it should "call onOk functions with new value when onEnter" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputPopupProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = shallowRender(E(InputPopup())(A.wrapped := props)())
    val textField = findComponentWithType(component, TextField())
    val textFieldProps = getComponentProps[TextFieldProps](textField)
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
    renderer.render(E(InputPopup())(A.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val prevTextProps = getComponentProps[TextFieldProps](findComponentWithType(comp, TextField()))
    val newValue = "new value"

    //when
    prevTextProps.onChange(newValue)

    //then
    val updatedComp = renderer.getRenderOutput()
    val textProps = getComponentProps[TextFieldProps](findComponentWithType(updatedComp, TextField()))
    textProps.text shouldBe newValue

    val modalProps = getComponentProps[ModalProps](findComponentWithType(updatedComp, Modal()))
    modalProps.actions.enabledCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
  }

  it should "disable OK command when new value is emtpy" in {
    //given
    val props = getInputPopupProps("Test message", initialValue = "initial value")
    val renderer = createRenderer()
    renderer.render(E(InputPopup())(A.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val prevTextProps = getComponentProps[TextFieldProps](findComponentWithType(comp, TextField()))
    val newValue = ""

    //when
    prevTextProps.onChange(newValue)

    //then
    val updatedComp = renderer.getRenderOutput()
    val textProps = getComponentProps[TextFieldProps](findComponentWithType(updatedComp, TextField()))
    textProps.text shouldBe newValue

    val modalProps = getComponentProps[ModalProps](findComponentWithType(updatedComp, Modal()))
    modalProps.actions.enabledCommands shouldBe Set(Buttons.CANCEL.command)
  }

  it should "render component with empty initial value" in {
    //given
    val props = getInputPopupProps("Test message", placeholder = Some("test placeholder"))
    val component = E(InputPopup())(A.wrapped := props)()

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
    val component = E(InputPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertInputPopup(result, props)
  }

  it should "reset state with new props when componentWillReceiveProps" in {
    //given
    val prevProps = getInputPopupProps("Test message", initialValue = "old initial value")
    val parentClass = React.createClass[InputPopupProps, Unit](self =>
      E.div()(
        E(InputPopup())(A.wrapped := self.props.wrapped)()
      )
    )
    val parentComp = renderIntoDocument(E(parentClass)(^.wrapped := prevProps)())
    val component = findRenderedComponentWithType(parentComp, InputPopup())
    val prevState = getComponentState[InputPopupState](component)
    prevState.value shouldBe prevProps.initialValue
    prevState.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    prevState.opened shouldBe true
    val containerElement = findReactElement(parentComp).parentNode
    val props = getInputPopupProps("Test message", initialValue = "new initial value")

    //when
    ReactDOM.render(E(parentClass)(^.wrapped := props)(), containerElement)

    //then
    val state = getComponentState[InputPopupState](component)
    state.value shouldBe props.initialValue
    state.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    state.opened shouldBe false
  }

  it should "set opened state to true when open" in {
    //given
    val props = getInputPopupProps("Test message", initialValue = "initial value")

    //when
    val component = renderIntoDocument(E(InputPopup())(A.wrapped := props)())

    //then
    val state = getComponentState[InputPopupState](component)
    state.value shouldBe props.initialValue
    state.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    state.opened shouldBe true
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
      assertDOMComponent(modalChild, E.div(^.className := "row-fluid")(), { case List(p, div) =>
        assertDOMComponent(p, E.p()(props.message))
        assertDOMComponent(div, E.div(^.className := "control-group")(), { case List(textField) =>
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
