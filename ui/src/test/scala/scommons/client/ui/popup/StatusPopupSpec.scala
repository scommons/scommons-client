package scommons.client.ui.popup

import scommons.client.ui.popup.PopupCss._
import scommons.client.ui.popup.StatusPopup._
import scommons.react._
import scommons.react.test._

class StatusPopupSpec extends TestSpec with TestRendererUtils {

  StatusPopup.popupComp = () => "Popup".asInstanceOf[ReactClass]
  StatusPopup.withAutoHideComp = () => "WithAutoHide".asInstanceOf[ReactClass]

  it should "do nothing when onClose/onOpen" in {
    //given
    val props = StatusPopupProps("test text", () => ())
    val comp = testRender(<(StatusPopup())(^.wrapped := props)())
    val popupProps = findComponentProps(comp, popupComp)

    //when
    popupProps.onClose()
    popupProps.onOpen()
  }

  it should "render component with correct props" in {
    //given
    val props = StatusPopupProps("test text", () => ())
    val component = <(StatusPopup())(^.wrapped := props)()

    //when
    val result = testRender(component)

    //then
    assertTestComponent(result, popupComp)({
      case PopupProps(_, closable, focusable, _, overlayClass, popupClass) =>
        closable shouldBe true
        focusable shouldBe false
        overlayClass shouldBe "scommons-modal-no-overlay"
        popupClass shouldBe statusContent
    }, inside(_) { case List(autoHide) =>
      assertTestComponent(autoHide, withAutoHideComp)({ autoHideProps =>
        autoHideProps shouldBe WithAutoHideProps(props.onHide)
      }, inside(_) { case List(child) =>
        child shouldBe props.text
      })
    })
  }
}
