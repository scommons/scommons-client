package scommons.client.ui.popup

import scommons.client.test.TestSpec
import scommons.client.test.TestVirtualDOM._

class ModalBodySpec extends TestSpec {

  it should "render component with provided children" in {
    //given
    val component = E(ModalBody())()(
      E.p()("some children")
    )

    //when
    val result = shallowRender(component)

    //then
    assertDOMComponent(result, E.div(^.className := "modal-body")(), { case List(child) =>
      assertDOMComponent(child, E.p()("some children"))
    })
  }
}
