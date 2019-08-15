package scommons.client.ui.popup

import scommons.client.ui.popup.PopupCss._
import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class LoadingPopupSpec extends TestSpec with ShallowRendererUtils {

  it should "do nothing when onClose/onOpen" in {
    //given
    val comp = shallowRender(<(LoadingPopup())()())
    val popupProps = findComponentProps(comp, Popup)

    //when
    popupProps.onClose()
    popupProps.onOpen()
  }

  it should "render component" in {
    //when
    val result = shallowRender(<(LoadingPopup())()())

    //then
    assertComponent(result, Popup)({
      case PopupProps(_, closable, focusable, _, overlayClass, popupClass) =>
        closable shouldBe false
        focusable shouldBe false
        overlayClass shouldBe loadingOverlay
        popupClass shouldBe loadingContent
    }, { case List(img) =>
      assertNativeComponent(img, <.img(^.className := loadingImg, ^.src := "")())
    })
  }
}
