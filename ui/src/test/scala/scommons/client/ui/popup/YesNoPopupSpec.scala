package scommons.client.ui.popup

import scommons.client.ui.SimpleButtonData
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.YesNoCancelOption._
import scommons.client.ui.popup.YesNoPopup._
import scommons.react.test._

class YesNoPopupSpec extends TestSpec with TestRendererUtils {

  YesNoPopup.modalComp = mockUiComponent("Modal")

  it should "call onSelect(Yes) function when Yes selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoPopupProps("Test message", onSelect = onSelect)
    val component = testRender(<(YesNoPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onSelect.expects(Yes)

    //when
    modalProps.actions.onCommand(_ => ())(Yes.command)
  }

  it should "call onSelect(No) function when No selected" in {
    //given
    val onSelect = mockFunction[YesNoCancelOption, Unit]
    val props = getYesNoPopupProps("Test message", onSelect = onSelect)
    val component = testRender(<(YesNoPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onSelect.expects(No)

    //when
    modalProps.actions.onCommand(_ => ())(No.command)
  }

  it should "render component with image" in {
    //given
    val props = getYesNoPopupProps("Test message", image = Some(IconCss.dialogInformation))
    val component = <(YesNoPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertYesNoPopup(result, props)
  }

  it should "render component without image" in {
    //given
    val props = getYesNoPopupProps("Test message")
    val component = <(YesNoPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertYesNoPopup(result, props)
  }

  it should "set focusedCommand when onOpen" in {
    //given
    val props = getYesNoPopupProps("Test message")
    val renderer = createTestRenderer(<(YesNoPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)
    modalProps.actions.focusedCommand shouldBe None

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.root
    val updatedModalProps = findComponentProps(updatedComp, modalComp)
    updatedModalProps.actions.focusedCommand shouldBe Some(props.selected.command)
  }

  private def getYesNoPopupProps(message: String,
                                 onSelect: YesNoCancelOption => Unit = _ => (),
                                 selected: YesNoCancelOption = Yes,
                                 image: Option[String] = None): YesNoPopupProps = YesNoPopupProps(
    message = message,
    onSelect = onSelect,
    selected = selected,
    image = image
  )

  private def assertYesNoPopup(result: TestInstance, props: YesNoPopupProps): Unit = {
    val expectedButtons = List(
      SimpleButtonData(Yes.command, "Yes", props.selected == Yes),
      SimpleButtonData(No.command, "No", props.selected == No)
    )
    val enabledCommands = Set(Yes.command, No.command)

    assertTestComponent(result, modalComp)({
      case ModalProps(header, buttons, actions, _, _, closable, _) =>
        header shouldBe None
        buttons shouldBe expectedButtons
        actions.enabledCommands shouldBe enabledCommands
        actions.focusedCommand shouldBe None
        closable shouldBe false
    }, inside(_) { case List(modalChild) =>
      assertNativeComponent(modalChild, <.div(^.className := "row-fluid")(), { children =>
        val (img, p) = inside(children) {
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
