package scommons.client.ui.popup

import scommons.client.ui.popup.PopupCss._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class StatusPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "do nothing when onClose/onOpen" in {
    //given
    val props = StatusPopupProps("test text", () => ())
    val comp = shallowRender(<(StatusPopup())(^.wrapped := props)())
    val popupProps = findComponentProps(comp, Popup)

    //when
    popupProps.onClose()
    popupProps.onOpen()
  }

  it should "render component with correct props" in {
    //given
    val props = StatusPopupProps("test text", () => ())
    val component = <(StatusPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, Popup)({
      case PopupProps(_, closable, focusable, _, overlayClass, popupClass) =>
        closable shouldBe true
        focusable shouldBe false
        overlayClass shouldBe "scommons-modal-no-overlay"
        popupClass shouldBe statusContent
    }, { case List(autoHide) =>
      assertComponent(autoHide, WithAutoHide)({ autoHideProps =>
        autoHideProps shouldBe WithAutoHideProps(props.onHide)
      }, { case List(child) =>
        child shouldBe props.text
      })
    })
  }
}
