package scommons.client.ui.popup

import org.scalatest.{Assertion, Succeeded}
import scommons.client.ui.Buttons
import scommons.client.ui.popup.Modal._
import scommons.client.util.ActionsData
import scommons.react.test._

class ModalSpec extends TestSpec with TestRendererUtils {

  Modal.popupComp = mockUiComponent("Popup")
  Modal.modalHeaderComp = mockUiComponent("ModalHeader")
  Modal.modalBodyComp = mockUiComponent("ModalBody")
  Modal.modalFooterComp = mockUiComponent("ModalFooter")

  it should "render closable modal with header" in {
    //given
    val props = getModalProps()
    val component = <(Modal())(^.wrapped := props)(
      <.p()("some children")
    )

    //when
    val result = testRender(component)

    //then
    assertModal(result, props)
  }

  it should "render non-closable modal with header" in {
    //given
    val props = getModalProps(closable = false)
    val component = <(Modal())(^.wrapped := props)(
      <.p()("some children")
    )

    //when
    val result = testRender(component)

    //then
    assertModal(result, props)
  }

  it should "render modal without header" in {
    //given
    val props = getModalProps(header = None)
    val component = <(Modal())(^.wrapped := props)(
      <.p()("some children")
    )

    //when
    val result = testRender(component)

    //then
    assertTestComponent(result, popupComp)({ popupProps =>
      popupProps shouldBe PopupProps(
        onClose = props.onClose,
        closable = props.closable,
        onOpen = props.onOpen
      )
    }, inside(_) { case List(body, footer) =>
      assertTestComponent(body, modalBodyComp)({ _ => Succeeded }, inside(_) { case List(child) =>
        assertNativeComponent(child, <.p()("some children"))
      })
      assertTestComponent(footer, modalFooterComp) { footerProps =>
        footerProps shouldBe ModalFooterProps(props.buttons, props.actions, props.dispatch)
      }
    })
  }

  private def assertModal(result: TestInstance, props: ModalProps): Assertion = {
    assertTestComponent(result, popupComp)({ popupProps =>
      popupProps shouldBe PopupProps(
        onClose = props.onClose,
        closable = props.closable,
        onOpen = props.onOpen
      )
    }, inside(_) { case List(header, body, footer) =>
      assertTestComponent(header, modalHeaderComp) { headerProps: ModalHeaderProps =>
        headerProps shouldBe ModalHeaderProps(props.header.get, props.onClose, closable = props.closable)
      }
      assertTestComponent(body, modalBodyComp)({ _ => Succeeded }, inside(_) { case List(child) =>
        assertNativeComponent(child, <.p()("some children"))
      })
      assertTestComponent(footer, modalFooterComp) { footerProps =>
        footerProps shouldBe ModalFooterProps(props.buttons, props.actions, props.dispatch)
      }
    })
  }

  private def getModalProps(header: Option[String] = Some("test header"),
                            onClose: () => Unit = () => (),
                            closable: Boolean = true,
                            onOpen: () => Unit = () => ()): ModalProps = ModalProps(
    header,
    List(Buttons.OK, Buttons.CANCEL),
    ActionsData.empty.copy(enabledCommands = Set(Buttons.OK.command, Buttons.CANCEL.command)),
    _ => (),
    onClose,
    closable,
    onOpen
  )
}
