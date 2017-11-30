package scommons.client.ui.popup

import io.github.shogowada.scalajs.reactjs.VirtualDOM._
import scommons.client.TestSpec
import scommons.client.ui.popup.PopupCss._

class LoadingPopupSpec extends TestSpec {

  it should "render component with correct props" in {
    //given
    val props = LoadingPopupProps(show = true)
    val component = <(LoadingPopup())(^.wrapped := props)()

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
      assertDOMComponent(img, <.img(^.className := loadingImg, ^.src := "")())
    })
  }
}
