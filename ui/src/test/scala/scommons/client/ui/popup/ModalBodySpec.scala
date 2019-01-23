package scommons.client.ui.popup

import scommons.react.test.TestSpec
import scommons.react.test.util.ShallowRendererUtils

class ModalBodySpec extends TestSpec with ShallowRendererUtils {

  it should "render component with provided children" in {
    //given
    val component = <(ModalBody())()(
      <.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := "modal-body")(), { case List(child) =>
      assertNativeComponent(child, <.p()("some children"))
    })
  }
}
