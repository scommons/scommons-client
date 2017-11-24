package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.Buttons
import scommons.client.ui.icon.IconCss

class OkPopupSpec extends TestSpec {

  it should "call onClose function when onOkCommand" in {
    //given
    val onClose = mockFunction[Unit]
    val props = getOkPopupProps("Test message", onClose = onClose)
    val component = shallowRender(E(OkPopup())(A.wrapped := props)())
    val modalProps = findComponentProps(component, Modal)

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

  it should "set focusedCommand when onOpen" in {
    //given
    val props = getOkPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(E(OkPopup())(A.wrapped := props)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.focusedCommand shouldBe None

    //when
    modalProps.onOpen()

    //then
    val updatedComp = renderer.getRenderOutput()
    val updatedModalProps = findComponentProps(updatedComp, Modal)
    updatedModalProps.actions.focusedCommand shouldBe Some(Buttons.OK.command)
  }

  it should "reset focusedCommand when componentWillReceiveProps" in {
    //given
    val prevProps = getOkPopupProps("Test message")
    val renderer = createRenderer()
    renderer.render(E(OkPopup())(A.wrapped := prevProps)())
    val comp = renderer.getRenderOutput()
    val modalProps = findComponentProps(comp, Modal)
    modalProps.actions.focusedCommand shouldBe None
    modalProps.onOpen()
    val compV2 = renderer.getRenderOutput()
    val modalPropsV2 = findComponentProps(compV2, Modal)
    modalPropsV2.actions.focusedCommand shouldBe Some(Buttons.OK.command)
    val props = getOkPopupProps("New message")

    //when
    renderer.render(E(OkPopup())(A.wrapped := props)())

    //then
    val compV3 = renderer.getRenderOutput()
    val modalPropsV3 = findComponentProps(compV3, Modal)
    modalPropsV3.actions.focusedCommand shouldBe None
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
