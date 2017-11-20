package scommons.client.ui.panel

import org.scalatest.{FlatSpec, Inside, Matchers}
import scommons.client.test.ShallowRendererUtils
import scommons.client.test.TestVirtualDOM._
import scommons.client.ui.panel.PopupPanelCss._

class LoadingPopupSpec extends FlatSpec
  with Matchers
  with Inside
  with ShallowRendererUtils {

  "rendering" should "render component with correct props" in {
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
