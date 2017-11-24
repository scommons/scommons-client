package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._
import scommons.client.test.raw.ShallowRenderer.ComponentInstance
import scommons.client.ui.Buttons
import scommons.client.util.ActionsData

class ModalSpec extends TestSpec {

  it should "render closable modal with header" in {
    //given
    val props = getModalProps()
    val component = E(Modal())(A.wrapped := props)(
      E.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertModal(result, props)
  }

  it should "render non-closable modal with header" in {
    //given
    val props = getModalProps(closable = false)
    val component = E(Modal())(A.wrapped := props)(
      E.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertModal(result, props)
  }

  it should "render modal without header" in {
    //given
    val props = getModalProps(header = None)
    val component = E(Modal())(A.wrapped := props)(
      E.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, Popup(), { popupProps: PopupProps =>
      popupProps shouldBe PopupProps(
        props.show,
        props.onClose,
        closable = props.closable,
        props.onOpen
      )
    }, { case List(body, footer) =>
      assertComponent(body, ModalBody(), { _: Unit => () }, { case List(child) =>
        assertDOMComponent(child, E.p()("some children"))
      })
      assertComponent(footer, ModalFooter(), { footerProps: ModalFooterProps =>
        footerProps shouldBe ModalFooterProps(props.buttons, props.actions)
      })
    })
  }

  private def assertModal(result: ComponentInstance, props: ModalProps): Unit = {
    assertComponent(result, Popup(), { popupProps: PopupProps =>
      popupProps shouldBe PopupProps(
        props.show,
        props.onClose,
        closable = props.closable,
        props.onOpen
      )
    }, { case List(header, body, footer) =>
      assertComponent(header, ModalHeader(), { headerProps: ModalHeaderProps =>
        headerProps shouldBe ModalHeaderProps(props.header.get, props.onClose, closable = props.closable)
      })
      assertComponent(body, ModalBody(), { _: Unit => () }, { case List(child) =>
        assertDOMComponent(child, E.p()("some children"))
      })
      assertComponent(footer, ModalFooter(), { footerProps: ModalFooterProps =>
        footerProps shouldBe ModalFooterProps(props.buttons, props.actions)
      })
    })
  }

  private def getModalProps(header: Option[String] = Some("test header"),
                            onClose: () => Unit = () => (),
                            closable: Boolean = true,
                            onOpen: () => Unit = () => ()): ModalProps = ModalProps(
    show = true,
    header,
    List(Buttons.OK, Buttons.CANCEL),
    ActionsData(Set(Buttons.OK.command, Buttons.CANCEL.command), _ => ()),
    onClose,
    closable,
    onOpen
  )
}
