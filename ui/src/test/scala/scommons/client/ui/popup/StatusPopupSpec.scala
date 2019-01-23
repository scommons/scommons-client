package scommons.client.ui.popup

import scommons.client.ui.popup.PopupCss._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class StatusPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "render component with correct props" in {
    //given
    val props = StatusPopupProps("test text", show = true, () => ())
    val component = <(StatusPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, Popup)({
      case PopupProps(show, _, closable, focusable, _, overlayClass, popupClass) =>
        show shouldBe props.show
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
