package scommons.client.ui.popup

import scommons.client.ui.popup.LoadingPopup.popupComp
import scommons.client.ui.popup.PopupCss._
import scommons.react._
import scommons.react.test._

class LoadingPopupSpec extends TestSpec with TestRendererUtils {

  LoadingPopup.popupComp = () => "Popup".asInstanceOf[ReactClass]

  it should "do nothing when onClose/onOpen" in {
    //given
    val comp = testRender(<(LoadingPopup())()())
    val popupProps = findComponentProps(comp, popupComp)

    //when
    popupProps.onClose()
    popupProps.onOpen()
  }

  it should "render component" in {
    //when
    val result = testRender(<(LoadingPopup())()())

    //then
    assertTestComponent(result, popupComp)({
      case PopupProps(_, closable, focusable, _, overlayClass, popupClass) =>
        closable shouldBe false
        focusable shouldBe false
        overlayClass shouldBe loadingOverlay
        popupClass shouldBe loadingContent
    }, inside(_) { case List(img) =>
      assertNativeComponent(img, <.img(^.className := loadingImg, ^.src := "")())
    })
  }
}
