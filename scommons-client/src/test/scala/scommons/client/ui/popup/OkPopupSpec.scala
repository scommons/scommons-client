package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.test.raw.TestReactDOM._
import scommons.client.ui.Buttons
import scommons.client.ui.popup.OkPopup.OkPopupState
import scommons.react.modal.NativeReactModal

class OkPopupSpec extends FlatSpec
  with Matchers
  with Inside
  with ShallowRendererUtils
  with MockFactory {

  it should "call onClose function when onOkCommand" in {
    //given
    val onClose = mockFunction[Unit]
    val props = getOkPopupProps("Test message", onClose = onClose)
    val component = shallowRender(E(OkPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onClose.expects()

    //when
    modalProps.actions.onCommand(Buttons.OK.command)
  }

  it should "render component with correct props" in {
    //given
    val props = getOkPopupProps("Test message")
    val component = E(OkPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertOkPopup(result, props)
  }

  it should "reset state with new props when componentWillReceiveProps" in {
    //given
    val prevProps = getOkPopupProps("Test message")
    val parentClass = React.createClass[OkPopupProps, Unit](self =>
      E.div()(
        E(OkPopup())(A.wrapped := self.props.wrapped)()
      )
    )
    val parentComp = renderIntoDocument(E(parentClass)(^.wrapped := prevProps)())
    val component = findRenderedComponentWithType(parentComp, OkPopup())
    val modal = findRenderedComponentWithType(component, Modal())
    val modalProps = getComponentProps[ModalProps](modal)
    modalProps.onOpen()
    val prevState = getComponentState[OkPopupState](component)
    prevState.opened shouldBe true
    val reactModal = findRenderedComponentWithType(component, NativeReactModal).portal
    val containerElement = findReactElement(parentComp).parentNode
    val props = getOkPopupProps("New message")

    //when
    ReactDOM.render(E(parentClass)(^.wrapped := props)(), containerElement)

    //then
    val state = getComponentState[OkPopupState](component)
    state.opened shouldBe false

    //cleanup
    unmountComponentAtNode(findDOMNode(reactModal).parentNode) shouldBe true
  }

  it should "set opened state to true when onOpen" in {
    //given
    val props = getOkPopupProps("Test message")
    val component = renderIntoDocument(E(OkPopup())(A.wrapped := props)())
    val modal = findRenderedComponentWithType(component, Modal())
    val modalProps = getComponentProps[ModalProps](modal)
    val reactModal = findRenderedComponentWithType(component, NativeReactModal).portal

    //when
    modalProps.onOpen()

    //then
    val state = getComponentState[OkPopupState](component)
    state.opened shouldBe true

    //cleanup
    unmountComponentAtNode(findDOMNode(reactModal).parentNode) shouldBe true
  }

  private def getOkPopupProps(message: String,
                              onClose: () => Unit = () => (),
                              show: Boolean = true): OkPopupProps = OkPopupProps(
    show = show,
    message = message,
    onClose = onClose
  )

  private def assertOkPopup(result: ComponentInstance, props: OkPopupProps): Unit = {
    val actionCommands = Set(Buttons.OK.command)

    assertComponent(result, Modal(), { modalProps: ModalProps =>
      inside(modalProps) { case ModalProps(show, header, buttons, actions, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe List(Buttons.OK)
        actions.enabledCommands shouldBe actionCommands
        onClose shouldBe props.onClose
        closable shouldBe true
      }
    }, { case List(modalChild) =>
      assertDOMComponent(modalChild, E.div(^.className := "row-fluid")(), { case List(p) =>
        assertDOMComponent(p, E.p()(props.message))
      })
    })
  }
}
