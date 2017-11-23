package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.{React, ReactDOM}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestUtils._
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ReactTestUtils._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.popup.YesNoCancelPopup.YesNoCancelPopupState
import scommons.client.ui.{Buttons, SimpleButtonData}

class YesNoCancelPopupSpec extends FlatSpec
  with Matchers
  with Inside
  with ShallowRendererUtils
  with MockFactory {

  it should "call onSelect(Yes) function when Yes selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(E(YesNoCancelPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onSelect.expects(Yes)

    //when
    modalProps.actions.onCommand(Yes.command)
  }

  it should "call onSelect(No) function when No selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(E(YesNoCancelPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onSelect.expects(No)

    //when
    modalProps.actions.onCommand(No.command)
  }

  it should "call onSelect(Cancel) function when Cancel selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(E(YesNoCancelPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onSelect.expects(Cancel)

    //when
    modalProps.actions.onCommand(Cancel.command)
  }

  it should "call onSelect(Cancel) function when onClose" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(E(YesNoCancelPopup())(A.wrapped := props)())
    val modalProps = getComponentProps[ModalProps](component)

    //then
    onSelect.expects(Cancel)

    //when
    modalProps.onClose()
  }

  it should "render component with image" in {
    //given
    val props = getYesNoCancelPopupProps("Test message", image = Some(IconCss.dialogInformation))
    val component = E(YesNoCancelPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertYesNoCancelPopup(result, props)
  }

  it should "render component without image" in {
    //given
    val props = getYesNoCancelPopupProps("Test message")
    val component = E(YesNoCancelPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertYesNoCancelPopup(result, props)
  }

  it should "reset state with new props when componentWillReceiveProps" in {
    //given
    val prevProps = getYesNoCancelPopupProps("Test message")
    val parentClass = React.createClass[YesNoCancelPopupProps, Unit](self =>
      E.div()(
        E(YesNoCancelPopup())(A.wrapped := self.props.wrapped)()
      )
    )
    val parentComp = renderIntoDocument(E(parentClass)(^.wrapped := prevProps)())
    val component = findRenderedComponentWithType(parentComp, YesNoCancelPopup())
    val prevState = getComponentState[YesNoCancelPopupState](component)
    prevState.opened shouldBe true
    val containerElement = findReactElement(parentComp).parentNode
    val props = getYesNoCancelPopupProps("New message")

    //when
    ReactDOM.render(E(parentClass)(^.wrapped := props)(), containerElement)

    //then
    val state = getComponentState[YesNoCancelPopupState](component)
    state.opened shouldBe false
  }

  it should "set opened state to true when open" in {
    //given
    val props = getYesNoCancelPopupProps("Test message")

    //when
    val component = renderIntoDocument(E(YesNoCancelPopup())(A.wrapped := props)())

    //then
    val state = getComponentState[YesNoCancelPopupState](component)
    state.opened shouldBe true

    val modalProps = getComponentProps[ModalProps](findRenderedComponentWithType(component, Modal()))
    modalProps.actions.focusedCommand shouldBe Some(props.selected.command)
  }

  private def getYesNoCancelPopupProps(message: String,
                                       onSelect: YesNoCancelOption => Unit = _ => (),
                                       selected: YesNoCancelOption = Yes,
                                       image: Option[String] = None,
                                       show: Boolean = true): YesNoCancelPopupProps = YesNoCancelPopupProps(
    show = show,
    message = message,
    onSelect = onSelect,
    selected = selected,
    image = image
  )

  private def assertYesNoCancelPopup(result: ComponentInstance, props: YesNoCancelPopupProps): Unit = {
    val expectedButtons = List(
      SimpleButtonData(Yes.command, "Yes", props.selected == Yes),
      SimpleButtonData(No.command, "No", props.selected == No),
      Buttons.CANCEL.copy(command = Cancel.command, primary = props.selected == Cancel)
    )
    val enabledCommands = Set(Yes.command, No.command, Cancel.command)

    assertComponent(result, Modal(), { modalProps: ModalProps =>
      inside(modalProps) { case ModalProps(show, header, buttons, actions, _, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe expectedButtons
        actions.enabledCommands shouldBe enabledCommands
        actions.focusedCommand shouldBe None
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
