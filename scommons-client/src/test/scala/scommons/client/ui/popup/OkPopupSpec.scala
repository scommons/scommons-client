package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.Buttons
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.OkPopup.OkPopupState

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

  it should "render component with image" in {
    //given
    val props = getOkPopupProps("Test message", image = Some(IconCss.dialogInformation))
    val component = E(OkPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertOkPopup(result, props)
  }

  it should "render component without image" in {
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
    val prevState = getComponentState[OkPopupState](component)
    prevState.opened shouldBe true
    val containerElement = findReactElement(parentComp).parentNode
    val props = getOkPopupProps("New message")

    //when
    ReactDOM.render(E(parentClass)(^.wrapped := props)(), containerElement)

    //then
    val state = getComponentState[OkPopupState](component)
    state.opened shouldBe false
  }

  it should "set opened state to true when open" in {
    //given
    val props = getOkPopupProps("Test message")

    //when
    val component = renderIntoDocument(E(OkPopup())(A.wrapped := props)())

    //then
    val state = getComponentState[OkPopupState](component)
    state.opened shouldBe true

    val modalProps = getComponentProps[ModalProps](findRenderedComponentWithType(component, Modal()))
    modalProps.actions.focusedCommand shouldBe Some(Buttons.OK.command)
  }

  private def getOkPopupProps(message: String,
                              onClose: () => Unit = () => (),
                              image: Option[String] = None,
                              show: Boolean = true): OkPopupProps = OkPopupProps(
    show = show,
    message = message,
    onClose = onClose,
    image = image
  )

  private def assertOkPopup(result: ComponentInstance, props: OkPopupProps): Unit = {
    val actionCommands = Set(Buttons.OK.command)

    assertComponent(result, Modal(), { modalProps: ModalProps =>
      inside(modalProps) { case ModalProps(show, header, buttons, actions, onClose, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe List(Buttons.OK)
        actions.enabledCommands shouldBe actionCommands
        actions.focusedCommand shouldBe None
        onClose shouldBe props.onClose
        closable shouldBe true
      }
    }, { case List(modalChild) =>
      assertDOMComponent(modalChild, E.div(^.className := "row-fluid")(), { children =>
        val (img, p) = children match {
          case List(pElem) => (None, pElem)
          case List(imgElem, pElem) => (Some(imgElem), pElem)
        }
        props.image.foreach { image =>
          img should not be None
          assertDOMComponent(img.get, E.img(^.className := image, ^.src := "")())
        }
        assertDOMComponent(p, E.p()(props.message))
      })
    })
  }
}
