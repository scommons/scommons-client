package scommons.client.ui.popup

import scommons.client.ui.Buttons
import scommons.client.ui.icon.IconCss
import scommons.client.ui.popup.OkPopup._
import scommons.react.test._

class OkPopupSpec extends TestSpec with TestRendererUtils {

  OkPopup.modalComp = mockUiComponent("Modal")

  it should "call onClose function when onOkCommand" in {
    //given
    val onClose = mockFunction[Unit]
    val props = getOkPopupProps("Test message", onClose = onClose)
    val component = testRender(<(OkPopup())(^.wrapped := props)())
    val modalProps = findComponentProps(component, modalComp)

    //then
    onClose.expects()

    //when
    modalProps.actions.onCommand(_ => ())(Buttons.OK.command)
  }

  it should "render component with image" in {
    //given
    val props = getOkPopupProps("Test message", image = Some(IconCss.dialogInformation))
    val component = <(OkPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertOkPopup(result, props)
  }

  it should "render component without image" in {
    //given
    val props = getOkPopupProps("Test message")
    val component = <(OkPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertOkPopup(result, props)
  }

  it should "set focusedCommand when onOpen" in {
    //given
    val props = getOkPopupProps("Test message")
    val renderer = createTestRenderer(<(OkPopup())(^.wrapped := props)())
    val comp = renderer.root
    val modalProps = findComponentProps(comp, modalComp)
    modalProps.actions.focusedCommand shouldBe None

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.root
    val updatedModalProps = findComponentProps(updatedComp, modalComp)
    updatedModalProps.actions.focusedCommand shouldBe Some(Buttons.OK.command)
  }

  private def getOkPopupProps(message: String,
                              onClose: () => Unit = () => (),
                              image: Option[String] = None): OkPopupProps = OkPopupProps(
    message = message,
    onClose = onClose,
    image = image
  )

  private def assertOkPopup(result: TestInstance, props: OkPopupProps): Unit = {
    val actionCommands = Set(Buttons.OK.command)

    assertTestComponent(result, modalComp)({
      case ModalProps(header, buttons, actions, _, onClose, closable, _) =>
        header shouldBe None
        buttons shouldBe List(Buttons.OK)
        actions.enabledCommands shouldBe actionCommands
        actions.focusedCommand shouldBe None
        onClose shouldBe props.onClose
        closable shouldBe true
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
