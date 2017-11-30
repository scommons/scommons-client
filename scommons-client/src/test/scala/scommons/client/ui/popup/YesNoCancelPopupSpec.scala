package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.{Buttons, SimpleButtonData}

class YesNoCancelPopupSpec extends TestSpec {

  it should "call onSelect(Yes) function when Yes selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(<(YesNoCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onSelect.expects(Yes)

    //when
    modalProps.actions.onCommand(Yes.command)
  }

  it should "call onSelect(No) function when No selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(<(YesNoCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onSelect.expects(No)

    //when
    modalProps.actions.onCommand(No.command)
  }

  it should "call onSelect(Cancel) function when Cancel selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(<(YesNoCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onSelect.expects(Cancel)

    //when
    modalProps.actions.onCommand(Cancel.command)
  }

  it should "call onSelect(Cancel) function when onClose" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(<(YesNoCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onSelect.expects(Cancel)

    //when
    modalProps.onClose()
  }

  it should "render component with image" in {
    //given
    val props = getYesNoCancelPopupProps("Test message", image = Some(IconCss.dialogInformation))
    val component = <(YesNoCancelPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertYesNoCancelPopup(result, props)
  }

  it should "render component without image" in {
    //given
    val props = getYesNoCancelPopupProps("Test message")
    val component = <(YesNoCancelPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertYesNoCancelPopup(result, props)
  }

  it should "set focusedCommand when onOpen" in {
    //given
    val props = getYesNoCancelPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(<(YesNoCancelPopup())(^.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.focusedCommand shouldBe None

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.getRenderOutput()
    val updatedModalProps = findComponentProps(updatedComp, Modal)
    updatedModalProps.actions.focusedCommand shouldBe Some(props.selected.command)
  }

  it should "reset focusedCommand when componentWillReceiveProps" in {
    //given
    val prevProps = getYesNoCancelPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(<(YesNoCancelPopup())(^.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.focusedCommand shouldBe None
    modalProps.onOpen()
    val compV2 = renderer.getRenderOutput()
    val modalPropsV2 = findComponentProps(compV2, Modal)
    modalPropsV2.actions.focusedCommand shouldBe Some(prevProps.selected.command)
    val props = getYesNoCancelPopupProps("New message")

    //when
    renderer.render(<(YesNoCancelPopup())(^.wrapped := props)())

    //then
    val compV3 = renderer.getRenderOutput()
    val modalPropsV3 = findComponentProps(compV3, Modal)
    modalPropsV3.actions.focusedCommand shouldBe None
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
      assertDOMComponent(modalChild, <.div(^.className := "row-fluid")(), { children =>
        val (img, p) = children match {
          case List(pElem) => (None, pElem)
          case List(imgElem, pElem) => (Some(imgElem), pElem)
        }
        props.image.foreach { image =>
          img should not be None
          assertDOMComponent(img.get, <.img(^.className := image, ^.src := "")())
        }
        assertDOMComponent(p, <.p()(props.message))
      })
    })
  }
}
