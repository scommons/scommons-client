package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._
import scommons.client.ui.popup.PopupCss._

class LoadingPopupSpec extends TestSpec {

  it should "render component with correct props" in {
    //given
    val props = LoadingPopupProps(show = true)
    val component = E(LoadingPopup())(A.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, Popup(), { popupProps: PopupProps =>
      inside(popupProps) { case PopupProps(show, _, closable, _, overlayClass, popupClass) =>
        show shouldBe props.show
        closable shouldBe false
        overlayClass shouldBe loadingOverlay
        popupClass shouldBe loadingContent
      }
    }, { case List(img) =>
      assertDOMComponent(img, E.img(^.className := loadingImg, ^.src := "")())
    })
  }
}
