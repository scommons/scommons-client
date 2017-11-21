package scommons.client.ui.panel

import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.test.raw.TestReactDOM._
import scommons.client.ui.panel.InputPopup.InputBoxState
import scommons.client.ui.{Buttons, TextField, TextFieldProps}
import scommons.react.modal.NativeReactModal

class InputPopupSpec extends FlatSpec
  with Matchers
  with Inside
  with ShallowRendererUtils
  with MockFactory {

  "onCancelCommand" should "call onCancel function" in {
    //given
    val onCancel = mockFunction[Unit]
    val props = getInputBoxProps("Test message", onCancel = onCancel)
    val component = shallowRender(E(InputPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onCancel.expects()

    //when
    modalProps.actions.onCommand(Buttons.CANCEL.command)
  }

  "onOkCommand" should "call onOk function" in {
    //given
    val onOk = mockFunction[String, Unit]
    val props = getInputBoxProps("Test message", initialValue = "initial value", onOk = onOk)
    val component = shallowRender(E(InputPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onOk.expects(props.initialValue)

    //when
    modalProps.actions.onCommand(Buttons.OK.command)
  }

  "rendering" should "render component with empty initial value" in {
    //given
    val props = getInputBoxProps("Test message", placeholder = Some("test placeholder"))
    val component = E(InputPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertInputBox(result, props)
  }

  it should "render component with non-empty initial value" in {
    //given
    val props = getInputBoxProps("Test message",
      placeholder = Some("test placeholder"),
      initialValue = "initial value"
    )
    val component = E(InputPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertInputBox(result, props)
  }

  "componentWillReceiveProps" should "reset state with new props" in {
    //given
    val prevProps = getInputBoxProps("Test message", initialValue = "old initial value")
    val parentClass = React.createClass[InputPopupProps, Unit](self =>
      E.div()(
        E(InputPopup())(A.wrapped := self.props.wrapped)()
      )
    )
    val parentComp = renderIntoDocument(E(parentClass)(^.wrapped := prevProps)())
    val component = findRenderedComponentWithType(parentComp, InputPopup())
    val modal = findRenderedComponentWithType(component, Modal())
    val modalProps = getComponentProps[ModalProps](modal)
    modalProps.onOpen()
    val prevState = getComponentState[InputBoxState](component)
    prevState.value shouldBe prevProps.initialValue
    prevState.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    prevState.requestFocus shouldBe true
    prevState.requestSelect shouldBe true
    val reactModal = findRenderedComponentWithType(component, NativeReactModal).portal
    val containerElement = findReactElement(parentComp).parentNode
    val props = getInputBoxProps("Test message", initialValue = "new initial value")

    //when
    ReactDOM.render(E(parentClass)(^.wrapped := props)(), containerElement)

    //then
    val state = getComponentState[InputBoxState](component)
    state.value shouldBe props.initialValue
    state.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    state.requestFocus shouldBe false
    state.requestSelect shouldBe false

    //cleanup
    unmountComponentAtNode(findDOMNode(reactModal).parentNode) shouldBe true
  }

  "onOpen" should "set requestFocus and requestSelect to true" in {
    //given
    val props = getInputBoxProps("Test message", initialValue = "initial value")
    val component = renderIntoDocument(E(InputPopup())(A.wrapped := props)())
    val modal = findRenderedComponentWithType(component, Modal())
    val modalProps = getComponentProps[ModalProps](modal)
    val reactModal = findRenderedComponentWithType(component, NativeReactModal).portal

    //when
    modalProps.onOpen()

    //then
    val state = getComponentState[InputBoxState](component)
    state.value shouldBe props.initialValue
    state.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)
    state.requestFocus shouldBe true
    state.requestSelect shouldBe true

    //cleanup
    unmountComponentAtNode(findDOMNode(reactModal).parentNode) shouldBe true
  }

  "onChange" should "enable OK command when new value is non-emtpy" in {
    //given
    val component = renderIntoDocument(E(InputPopup())(A.wrapped := getInputBoxProps("Test message"))())
    val modal = findRenderedComponentWithType(component, NativeReactModal).portal
    val textField = findRenderedComponentWithType(modal, TextField())
    val textFieldProps = getComponentProps[TextFieldProps](textField)
    val newValue = "new value"

    //when
    textFieldProps.onChange(newValue)

    //then
    val state = getComponentState[InputBoxState](component)
    state.value shouldBe newValue
    state.actionCommands shouldBe Set(Buttons.OK.command, Buttons.CANCEL.command)

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }

  it should "disable OK command when new value is emtpy" in {
    //given
    val component = renderIntoDocument(E(InputPopup())(A.wrapped := getInputBoxProps(
      "Test message", initialValue = "initial value"
    ))())
    val modal = findRenderedComponentWithType(component, NativeReactModal).portal
    val textField = findRenderedComponentWithType(modal, TextField())
    val textFieldProps = getComponentProps[TextFieldProps](textField)
    val newValue = ""

    //when
    textFieldProps.onChange(newValue)

    //then
    val state = getComponentState[InputBoxState](component)
    state.value shouldBe newValue
    state.actionCommands shouldBe Set(Buttons.CANCEL.command)

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }

  "onEnter" should "call onOk functions with new value" in {
    //given
    val onOk = mockFunction[String, Unit]
    val component = renderIntoDocument(E(InputPopup())(A.wrapped := getInputBoxProps(
      "Test message", onOk = onOk
    ))())
    val modal = findRenderedComponentWithType(component, NativeReactModal).portal
    val textField = findRenderedComponentWithType(modal, TextField())
    val textFieldProps = getComponentProps[TextFieldProps](textField)
    val newValue = "new value"
    textFieldProps.onChange(newValue)

    //then
    onOk.expects(newValue)

    //when
    textFieldProps.onEnter()

    //cleanup
    unmountComponentAtNode(findDOMNode(modal).parentNode) shouldBe true
  }

  private def getInputBoxProps(message: String,
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

  private def assertInputBox(result: ComponentInstance, props: InputPopupProps): Unit = {
    val actionCommands =
      if (props.initialValue.nonEmpty) Set(Buttons.OK.command, Buttons.CANCEL.command)
      else Set(Buttons.CANCEL.command)

    assertComponent(result, Modal(), { modalProps: ModalProps =>
      inside(modalProps) { case ModalProps(show, header, buttons, actions, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe List(Buttons.OK, Buttons.CANCEL)
        actions.actionCommands shouldBe actionCommands
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
