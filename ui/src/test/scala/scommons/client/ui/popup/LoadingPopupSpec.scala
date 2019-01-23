package scommons.client.ui.popup

import scommons.client.ui.popup.PopupCss._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class LoadingPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "render component with correct props" in {
    //given
    val props = LoadingPopupProps(show = true)
    val component = <(LoadingPopup())(^.wrapped := props)()

    //when
    val result = shallowRender(component)

    //then
    assertComponent(result, Popup)({
      case PopupProps(show, _, closable, focusable, _, overlayClass, popupClass) =>
        show shouldBe props.show
        closable shouldBe false
        focusable shouldBe false
        overlayClass shouldBe loadingOverlay
        popupClass shouldBe loadingContent
    }, { case List(img) =>
      assertNativeComponent(img, <.img(^.className := loadingImg, ^.src := "")())
    })
  }
}
