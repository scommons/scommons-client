package scommons.client.ui.popup

import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.{Buttons, SimpleButtonData}
import scommons.react.test.TestSpec
import scommons.react.test.raw.ShallowInstance
import scommons.react.test.util.ShallowRendererUtils

class YesNoCancelPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "call onSelect(Yes) function when Yes selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoCancelPopupProps("Test message", onSelect = onSelect)
    val component = shallowRender(<(YesNoCancelPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

    //then
    onSelect.expects(Yes)

    //when
    modalProps.actions.onCommand(_ => ())(Yes.command)
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
    modalProps.actions.onCommand(_ => ())(No.command)
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
    modalProps.actions.onCommand(_ => ())(Cancel.command)
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

  private def assertYesNoCancelPopup(result: ShallowInstance, props: YesNoCancelPopupProps): Unit = {
    val expectedButtons = List(
      SimpleButtonData(Yes.command, "Yes", props.selected == Yes),
      SimpleButtonData(No.command, "No", props.selected == No),
      Buttons.CANCEL.copy(command = Cancel.command, primary = props.selected == Cancel)
    )
    val enabledCommands = Set(Yes.command, No.command, Cancel.command)

    assertComponent(result, Modal)({
      case ModalProps(show, header, buttons, actions, _, _, closable, _) =>
        show shouldBe props.show
        header shouldBe None
        buttons shouldBe expectedButtons
        actions.enabledCommands shouldBe enabledCommands
        actions.focusedCommand shouldBe None
        closable shouldBe true
    }, { case List(modalChild) =>
      assertNativeComponent(modalChild, <.div(^.className := "row-fluid")(), { children =>
        val (img, p) = children match {
          case List(pElem) => (None, pElem)
          case List(imgElem, pElem) => (Some(imgElem), pElem)
        }
        props.image.foreach { image =>
          img should not be None
          assertNativeComponent(img.get, <.img(^.className := image, ^.src := "")())
        }
        assertNativeComponent(p, <.p()(props.message))
      })
    })
  }
}
