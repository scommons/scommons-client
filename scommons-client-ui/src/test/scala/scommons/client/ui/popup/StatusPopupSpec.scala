package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.test.TestSpec
import scommons.client.ui.popup.PopupCss._

class StatusPopupSpec extends TestSpec {

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
