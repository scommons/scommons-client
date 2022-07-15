package scommons.client.ui.popup

import scommons.react.test._

class ModalBodySpec extends TestSpec with TestRendererUtils {

  it should "render component with provided children" in {
    //given
    val component = <(ModalBody())()(
      <.p()("some children")
    )

    //when
    val result = testRender(component)

    //then
    assertNativeComponent(result, <.div(^.className := "modal-body")(), inside(_) { case List(child) =>
      assertNativeComponent(child, <.p()("some children"))
    })
  }
}
