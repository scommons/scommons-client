package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._
import scommons.client.ui.popup.PopupCss._

class StatusPopupSpec extends TestSpec {

  it should "render component with correct props" in {
    //given
    val props = StatusPopupProps("test text", show = true, () => ())
    val component = E(StatusPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, Popup(), { popupProps: PopupProps =>
      inside(popupProps) { case PopupProps(show, _, closable, _, overlayClass, popupClass) =>
        show shouldBe props.show
        closable shouldBe true
        overlayClass shouldBe "scommons-status-no-overlay"
        popupClass shouldBe statusContent
      }
    }, { case List(autoHide) =>
      assertComponent(autoHide, WithAutoHide(), { autoHideProps: WithAutoHideProps =>
        autoHideProps shouldBe WithAutoHideProps(props.onHide)
      }, { case List(child) =>
        child shouldBe props.text
      })
    })
  }
}
